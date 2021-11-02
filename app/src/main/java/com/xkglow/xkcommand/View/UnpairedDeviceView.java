package com.xkglow.xkcommand.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.R;

public class UnpairedDeviceView extends LinearLayout {
    public UnpairedDeviceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.unpaired_device_view, this);
    }
}
