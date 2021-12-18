package com.xkglow.xkcommand.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.R;

public class UnpairedDeviceView extends LinearLayout {
    TextView deviceNameText;

    public UnpairedDeviceView(Context context) {
        super(context);
        inflate(getContext(), R.layout.unpaired_device_view, this);
        deviceNameText = findViewById(R.id.device_name);
    }

    public void setDeviceName(String name) {
        deviceNameText.setText(name);
    }
}
