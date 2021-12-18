package com.xkglow.xkcommand;

import android.animation.ValueAnimator;
import android.app.Activity;
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
import java.util.Timer;
import java.util.TimerTask;

public class ControlFragment extends Fragment {
    ArrayList<DeviceControlView> deviceControlViews;
    int width, height;
    LinearLayout contentLayout;
    FrameLayout frameLayout, previous, next;
    ViewPagerIndicator viewPagerIndicator;
    private Timer mDeviceInfoTimer;
    private DeviceInfoTimerTask mDeviceInfoTimerTask;

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

        frameLayout = view.findViewById(R.id.frame_layout);
        contentLayout = view.findViewById(R.id.content_layout);
        contentLayout.post(new Runnable() {
            @Override
            public void run() {
                setDevices();
            }
        });

        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = AppGlobal.findConnectedDeviceIndex(AppGlobal.getCurrentDevice()) - 1;
                AppGlobal.setCurrentDevice(current);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.CHANGE_DEVICE_LIST, current));
            }
        });
        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = AppGlobal.findConnectedDeviceIndex(AppGlobal.getCurrentDevice()) + 1;
                AppGlobal.setCurrentDevice(current);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.CHANGE_DEVICE_LIST, current));
            }
        });

        viewPagerIndicator = view.findViewById(R.id.view_pager_indicator);

        return view;
    }

    @Subscribe(sticky = true)
    public void onEvent(MessageEvent event) {
        switch (event.type) {
            case ADD_DEVICE:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDevices();
                    }
                });
                break;
            case CHANGE_DEVICE:
                int index1 = (int) event.data;
                animateTo(index1);
                break;
            case TURN_ON_OFF:
                int index2 = (int) event.data;
                DeviceControlView deviceControlView = deviceControlViews.get(index2);
                deviceControlView.resetPower();
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animator.start();
            }
        });
        resetArrows();
        viewPagerIndicator.setCurrent(index);
    }

    private void resetArrows() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int current = AppGlobal.findConnectedDeviceIndex(AppGlobal.getCurrentDevice());
                int count = AppGlobal.getConnectedDevices().size();
                if (count < 2) {
                    previous.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                } else {
                    previous.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
                if (current == 0) {
                    previous.setVisibility(View.GONE);
                }
                if (current == count - 1) {
                    next.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (deviceControlViews != null) {
            for (DeviceControlView deviceControlView : deviceControlViews) {
                deviceControlView.reset();
            }
        }
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    private void setDevices() {
        contentLayout.removeAllViews();
        deviceControlViews = new ArrayList<>();
        if (frameLayout.getWidth() != 0) {
            width = frameLayout.getWidth();
        }
        if (frameLayout.getHeight() != 0) {
            height = frameLayout.getHeight();
        }
        for (int i = 0; i < AppGlobal.getConnectedDevices().size(); i++) {
            DeviceData deviceData = AppGlobal.getConnectedDevice(i);
            DeviceControlView deviceControlView = new DeviceControlView(getContext(), width, height, deviceData);
            deviceControlView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            contentLayout.addView(deviceControlView);
            deviceControlViews.add(deviceControlView);
        }
        int index = AppGlobal.findConnectedDeviceIndex(AppGlobal.getCurrentDevice());
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentLayout.getLayoutParams();
        params.leftMargin = -index * width;
        contentLayout.setLayoutParams(params);

        viewPagerIndicator.setDotsCount(AppGlobal.getConnectedDevices().size());
        viewPagerIndicator.setCurrent(AppGlobal.getCurrentDeviceIndex());

        resetArrows();
    }

    private void startTimer() {
        mDeviceInfoTimer = new Timer();
        mDeviceInfoTimerTask = new DeviceInfoTimerTask();
        mDeviceInfoTimer.schedule(mDeviceInfoTimerTask, 0, 500);
    }

    private void stopTimer() {
        if (mDeviceInfoTimer != null) {
            mDeviceInfoTimer.cancel();
        }
        if (mDeviceInfoTimerTask != null) {
            mDeviceInfoTimerTask.cancel();
        }
    }

    private class DeviceInfoTimerTask extends TimerTask {
        @Override
        public void run() {
            Activity activity = getActivity();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (deviceControlViews != null) {
                            for (DeviceControlView deviceControlView : deviceControlViews) {
                                deviceControlView.updateDeviceInfo();
                            }
                        }
                    }
                });
            }
        }
    }
}
