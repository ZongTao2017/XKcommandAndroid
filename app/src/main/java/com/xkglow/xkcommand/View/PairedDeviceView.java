package com.xkglow.xkcommand.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.R;

public class PairedDeviceView extends LinearLayout {
    LinearLayout topLayout, bottomLayout;
    public PairedDeviceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.paired_device_view, this);
        topLayout = findViewById(R.id.top_layout);
        bottomLayout = findViewById(R.id.bottom_layout);
    }

    public void setCurrent(boolean flag) {
        if (flag) {
            topLayout.setBackgroundColor(getResources().getColor(R.color.blue_1));
            bottomLayout.setBackgroundColor(getResources().getColor(R.color.blue_2));
        } else {
            topLayout.setBackgroundColor(getResources().getColor(R.color.blue_3));
            bottomLayout.setBackgroundColor(getResources().getColor(R.color.blue_4));
        }
    }
}
