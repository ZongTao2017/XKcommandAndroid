package com.xkglow.xkcommand;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.DeviceData;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.View.DeviceControlView;
import com.xkglow.xkcommand.View.ViewPagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ControlFragment extends Fragment {
    ArrayList<DeviceControlView> deviceControlViews;
    int width, height;
    LinearLayout contentLayout;
    FrameLayout previous, next;
    ViewPagerIndicator viewPagerIndicator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        contentLayout = view.findViewById(R.id.content_layout);
        contentLayout.post(new Runnable() {
            @Override
            public void run() {
                deviceControlViews = new ArrayList<>();
                height = frameLayout.getHeight();
                width = frameLayout.getWidth();
                for (int i = 0; i < AppGlobal.getDevices().size(); i++) {
                    DeviceData deviceData = AppGlobal.getDevice(i);
                    DeviceControlView deviceControlView = new DeviceControlView(getContext(), width, height, deviceData);
                    deviceControlView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    contentLayout.addView(deviceControlView);
                    deviceControlViews.add(deviceControlView);
                }
                int index = AppGlobal.findDeviceIndex(AppGlobal.getCurrentDevice());
                final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentLayout.getLayoutParams();
                params.leftMargin = -index * width;
                contentLayout.setLayoutParams(params);
            }
        });

        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = AppGlobal.findDeviceIndex(AppGlobal.getCurrentDevice()) - 1;
                AppGlobal.setCurrentDevice(current);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.CHANGE_DEVICE_LIST, current));
            }
        });
        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = AppGlobal.findDeviceIndex(AppGlobal.getCurrentDevice()) + 1;
                AppGlobal.setCurrentDevice(current);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.CHANGE_DEVICE_LIST, current));
            }
        });
        resetArrows();

        viewPagerIndicator = view.findViewById(R.id.view_pager_indicator);
        viewPagerIndicator.setDotsCount(AppGlobal.getDevices().size());
        viewPagerIndicator.setCurrent(AppGlobal.getCurrentDeviceIndex());

        return view;
    }

    @Subscribe(sticky = true)
    public void onEvent(MessageEvent event) {
        switch (event.type) {
            case CHANGE_DEVICE:
                int index1 = (int) event.data;
                animateTo(index1);
                break;
            case TURN_ON_OFF:
                int index2 = (int) event.data;
                DeviceControlView deviceControlView = deviceControlViews.get(index2);
                deviceControlView.reset();
                break;
            case POWER_OFF:
                DeviceControlView currentDevice = deviceControlViews.get(AppGlobal.getCurrentDeviceIndex());
                currentDevice.powerOff();
        }
    }

    private void animateTo(int index) {
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentLayout.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, -index * width);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                params.leftMargin = (Integer) valueAnimator.getAnimatedValue();
                contentLayout.requestLayout();
            }
        });
        animator.setDuration(300);
        animator.start();
        resetArrows();
        viewPagerIndicator.setCurrent(index);
    }

    private void resetArrows() {
        int current = AppGlobal.findDeviceIndex(AppGlobal.getCurrentDevice());
        int count = AppGlobal.getDevices().size();
        previous.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        if (current == 0) {
            previous.setVisibility(View.GONE);
        }
        if (current == count - 1) {
            next.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (deviceControlViews != null) {
            for (DeviceControlView deviceControlView : deviceControlViews) {
                deviceControlView.reset();
            }
        }
    }
}
