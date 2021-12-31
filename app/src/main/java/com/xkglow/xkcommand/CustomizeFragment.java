package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.DeviceData;
import com.xkglow.xkcommand.Helper.DeviceState;

import java.util.Timer;
import java.util.TimerTask;

public class CustomizeFragment extends Fragment {
    TextView textDisconnected;
    private Timer mTimer;
    private DeviceConnectionTimerTask mTimerTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customize, container, false);
        FrameLayout controller = view.findViewById(R.id.controller);
        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SelectButtonActivity.class));
            }
        });
        FrameLayout sensor = view.findViewById(R.id.sensor);
        sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SelectSensorActivity.class));
            }
        });
        FrameLayout channel = view.findViewById(R.id.channel);
        channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SelectChannelActivity.class));
            }
        });
        FrameLayout system = view.findViewById(R.id.system);
        system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditSystemActivity.class));
            }
        });
        textDisconnected = view.findViewById(R.id.not_connected);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    public void updateDeviceConnection() {
        if (getContext() == null) return;
        DeviceData deviceData = AppGlobal.getCurrentDevice();
        boolean horizontal = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (!horizontal) {
            if (deviceData.deviceState == DeviceState.OFFLINE ||
                    deviceData.deviceState == DeviceState.DISCONNECTED) {
                textDisconnected.setVisibility(View.VISIBLE);
            } else {
                textDisconnected.setVisibility(View.GONE);
            }
        } else {
            textDisconnected.setVisibility(View.GONE);
        }
    }

    private void startTimer() {
        mTimer = new Timer();
        mTimerTask = new DeviceConnectionTimerTask();
        mTimer.schedule(mTimerTask, 0, 500);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
    }

    private class DeviceConnectionTimerTask extends TimerTask {
        @Override
        public void run() {
            Activity activity = getActivity();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDeviceConnection();
                    }
                });
            }
        }
    }
}
