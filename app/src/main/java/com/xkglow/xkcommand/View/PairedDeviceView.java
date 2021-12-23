package com.xkglow.xkcommand.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.DeviceState;
import com.xkglow.xkcommand.R;

public class PairedDeviceView extends LinearLayout {
    LinearLayout topLayout, bottomLayout;
    TextView deviceNameText, deviceStateText;
    DeviceState deviceState;
    ImageView signalImage;
    boolean isCurrent;

    public PairedDeviceView(Context context) {
        super(context);
        inflate(getContext(), R.layout.paired_device_view, this);
        topLayout = findViewById(R.id.top_layout);
        bottomLayout = findViewById(R.id.bottom_layout);
        deviceNameText = findViewById(R.id.device_name);
        signalImage = findViewById(R.id.signal);
        deviceStateText = findViewById(R.id.device_state);
    }

    public void setCurrent(boolean flag) {
        isCurrent = flag;
        update();
    }

    public void setDeviceName(String name) {
        deviceNameText.setText(name);
    }

    public void setDeviceState(DeviceState state) {
        deviceState = state;
        deviceStateText.setText(state.name());
        update();
    }

    public void setSignal(int percent) {
        if (percent == 0) {
            signalImage.setImageResource(R.drawable.blt0);
        } else if (percent < 33) {
            signalImage.setImageResource(R.drawable.blt1);
        } else if (percent < 66) {
            signalImage.setImageResource(R.drawable.blt2);
        } else {
            signalImage.setImageResource(R.drawable.blt3);
        }
    }

    private void update() {
//        if (deviceState == DeviceState.OFFLINE) {
//            topLayout.setBackgroundColor(getResources().getColor(R.color.gray_2));
//            bottomLayout.setBackgroundColor(getResources().getColor(R.color.gray_1));
//        } else {
            if (isCurrent) {
                topLayout.setBackgroundColor(getResources().getColor(R.color.blue_1));
                bottomLayout.setBackgroundColor(getResources().getColor(R.color.blue_2));
            } else {
                topLayout.setBackgroundColor(getResources().getColor(R.color.blue_3));
                bottomLayout.setBackgroundColor(getResources().getColor(R.color.blue_4));
            }
//        }
    }
}
