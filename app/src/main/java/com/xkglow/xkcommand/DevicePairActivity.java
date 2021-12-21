package com.xkglow.xkcommand;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.DeviceData;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.View.DevicePairView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DevicePairActivity extends Activity {
    public static final int REQUEST_LOCATION = 110;

    public static final String TAG = "DevicePairFragment";
    private DevicePairView mDevicePairView;
    private Timer mAnimateTimer;
    private AnimateTimerTask mAnimateTimerTask;
    private Timer mAnimateCircleTimer;
    private AnimateCircleTimerTask mAnimateCircleTimerTask;
    private Timer mAnimateFullTimer;
    private AnimateFullTimerTask mAnimateFullTimerTask;

    private Button mDoneButton;
    private Button mCancelButton;
    private TextView mDoneTextView;
    private LinearLayout mInfoLayout;
    private ImageView mLockImageView;

    private DeviceData mCurrentPairingDevice;
    private int mFullCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_pair);
        checkPermission();

        mDevicePairView = findViewById(R.id.device_pair_view);
        mDoneButton = findViewById(R.id.done);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCancelButton = findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPairingDevice != null) {
                    AppGlobal.disconnect(mCurrentPairingDevice, true);
                }
                finish();
            }
        });
        mDoneTextView = findViewById(R.id.deviceConnected);
        mInfoLayout = findViewById(R.id.infoLayout);
        mLockImageView = findViewById(R.id.lock);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentPairingDevice = AppGlobal.getCurrentPairingDevice();
        startTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimers();
    }

    private void donePair() {
        runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    mDoneButton.setVisibility(View.VISIBLE);
                    mCancelButton.setVisibility(View.GONE);
                    mDoneTextView.setVisibility(View.VISIBLE);
                    mInfoLayout.setVisibility(View.GONE);
                    mLockImageView.setVisibility(View.VISIBLE);
                }
            }
        );
    }

    private void startTimers() {
        mFullCount = 0;
        mAnimateTimer = new Timer();
        mAnimateTimerTask = new AnimateTimerTask();
        mAnimateTimer.schedule(mAnimateTimerTask, 0, 200);
        mAnimateCircleTimer = new Timer();
        mAnimateCircleTimerTask = new AnimateCircleTimerTask();
        mAnimateCircleTimer.schedule(mAnimateCircleTimerTask, 1000, 1);
        mAnimateFullTimer = new Timer();
        mAnimateFullTimerTask = new AnimateFullTimerTask();
        mAnimateFullTimer.schedule(mAnimateFullTimerTask, 0, 20);
    }

    private void stopTimers() {
        mFullCount = 0;
        if (mAnimateTimer != null) {
            mAnimateTimer.cancel();
        }
        if (mAnimateTimerTask != null) {
            mAnimateTimerTask.cancel();
        }
        if (mAnimateCircleTimer != null) {
            mAnimateCircleTimer.cancel();
        }
        if (mAnimateCircleTimerTask != null) {
            mAnimateCircleTimerTask.cancel();
        }
        if (mAnimateFullTimer != null) {
            mAnimateFullTimer.cancel();
        }
        if (mAnimateFullTimerTask != null) {
            mAnimateFullTimerTask.cancel();
        }
    }

    private class AnimateTimerTask extends TimerTask {
        @Override
        public void run() {

        }
    }

    private class AnimateCircleTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCurrentPairingDevice == null) {
                        mCurrentPairingDevice = AppGlobal.getFirstScanUnpairedDevice();
                        if (mCurrentPairingDevice != null) {
                            AppGlobal.setCurrentPairingDevice(mCurrentPairingDevice);
                        }
                    }
                    mDevicePairView.animateCircle();
                }
            });
        }
    }

    private class AnimateFullTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mCurrentPairingDevice != null) {
                Log.d("Pairing Device", Integer.toString(mCurrentPairingDevice.signalPercent));
                if (AppGlobal.hasDeviceGatt(mCurrentPairingDevice.address) &&
                        mCurrentPairingDevice.signalPercent == 100) {
                    mFullCount++;
                    mDevicePairView.setProgress(100, mFullCount);
                    if (mFullCount >= 100) {
                        Log.d("Pairing Device", "Done");
                        stopTimers();
                        donePair();
                    }
                } else {
                    mFullCount = 0;
                    mDevicePairView.setProgress(mCurrentPairingDevice.signalPercent, 0);
                }
            } else {
                Log.e("Pairing Device", "no device");
                mFullCount = 0;
                mDevicePairView.setProgress(0, 0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {

        }
        if (permissions.size() > 0) {
            String[] list = new String[permissions.size()];
            int i = 0;
            for (String per : permissions)
                list[i++] = per;
            ActivityCompat.requestPermissions(this, list, REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}
