package com.xkglow.xkcommand.View;

import static com.xkglow.xkcommand.Helper.Helper.ICON_RATIO;
import static com.xkglow.xkcommand.Helper.Helper.ICON_SIZE;
import static com.xkglow.xkcommand.Helper.Helper.ICON_SIZE_TAB;
import static com.xkglow.xkcommand.Helper.Helper.ICON_SIZE_TAB_H;
import static com.xkglow.xkcommand.Helper.Helper.PADDING;
import static com.xkglow.xkcommand.Helper.Helper.STATUS_RATIO;
import static com.xkglow.xkcommand.Helper.Helper.dpToPx;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.DeviceData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.R;

public class DeviceControlView extends LinearLayout {
    DeviceData deviceData;
    ControlButton button1, button2, button3, button4, button5, button6, button7, button8;
    PowerView power;

    public DeviceControlView(@NonNull Context context, int w, int h, DeviceData deviceData) {
        super(context);

        Activity activity = (Activity) context;
        this.deviceData = deviceData;
        final int padding = dpToPx(context, 20);
        int width, height;
        float icon = ICON_SIZE;
        boolean horizontal = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (horizontal) {
            width = w - Helper.dpToPx(context, 160);
            height = h;
            if (Helper.checkIfTablet(activity)) {
                icon = ICON_SIZE_TAB_H;
                height = h - Helper.dpToPx(context, 100);
            }
            inflate(context, R.layout.device_control_view_horizontal, this);
        } else {
            width = w;
            height = h - Helper.dpToPx(context, 80);
            if (Helper.checkIfTablet(activity)) {
                height = h - Helper.dpToPx(context, 100);
                icon = ICON_SIZE_TAB;
            }
            inflate(context, R.layout.device_control_view, this);
        }

        int paddingV = (int) (height * PADDING / (PADDING * 5 + icon * 4));
        int iconSize = (int) (height * icon / (PADDING * 5 + icon * 4));
        int paddingH = (width - iconSize * 2) / 3;

        if (horizontal) {
            paddingV = (int) (height * PADDING / (PADDING * 2 + icon * 2));
            iconSize = (int) (height * icon / (PADDING * 2 + icon * 2));
            paddingH = (width - iconSize * 4) / 4;
        }

        int realIconSize = (int) (iconSize * ICON_RATIO);
        int resize = (iconSize - realIconSize) / 2;

        button1 = findViewById(R.id.button_1);
        button1.setIconSize(iconSize);

        button2 = findViewById(R.id.button_2);
        button2.setIconSize(iconSize);

        button3 = findViewById(R.id.button_3);
        button3.setIconSize(iconSize);

        button4 = findViewById(R.id.button_4);
        button4.setIconSize(iconSize);
        button4.setError(true);

        button5 = findViewById(R.id.button_5);
        button5.setIconSize(iconSize);

        button6 = findViewById(R.id.button_6);
        button6.setIconSize(iconSize);

        button7 = findViewById(R.id.button_7);
        button7.setIconSize(iconSize);

        button8 = findViewById(R.id.button_8);
        button8.setIconSize(iconSize);

        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
        layoutParams1.leftMargin = paddingH + resize;
        layoutParams1.topMargin = paddingV + resize;
        if (Helper.checkIfTablet(activity)) {
            layoutParams1.leftMargin = width / 2 - paddingV / 2 - iconSize + resize;
        }
        if (horizontal) {
            layoutParams1.leftMargin = resize + padding;
            layoutParams1.topMargin = (int) (paddingV * 1.5 + iconSize + resize);
            if (Helper.checkIfTablet(activity)) {
                layoutParams1.topMargin = height / 2 + paddingH / 2 + resize;
            }
        }
        button1.setLayoutParams(layoutParams1);

        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
        layoutParams2.leftMargin = paddingH * 2 + iconSize + resize;
        layoutParams2.topMargin = paddingV + resize;
        if (Helper.checkIfTablet(activity)) {
            layoutParams2.leftMargin = width / 2 + paddingV / 2 + resize;
        }
        if (horizontal) {
            layoutParams2.leftMargin = resize + padding;
            layoutParams2.topMargin = (int) (paddingV * 0.5 + resize);
            if (Helper.checkIfTablet(activity)) {
                layoutParams2.topMargin = height / 2 - paddingH / 2 - iconSize + resize;
            }
        }
        button2.setLayoutParams(layoutParams2);

        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
        layoutParams3.leftMargin = paddingH + resize;
        layoutParams3.topMargin = paddingV * 2 + iconSize + resize;
        if (Helper.checkIfTablet(activity)) {
            layoutParams3.leftMargin = width / 2 - paddingV / 2 - iconSize + resize;
        }
        if (horizontal) {
            layoutParams3.leftMargin = paddingH + iconSize + resize + padding;
            layoutParams3.topMargin = (int) (paddingV * 1.5 + iconSize + resize);
            if (Helper.checkIfTablet(activity)) {
                layoutParams3.topMargin = height / 2 + paddingH / 2 + resize;
            }
        }
        button3.setLayoutParams(layoutParams3);

        FrameLayout.LayoutParams layoutParams4 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
        layoutParams4.leftMargin = paddingH * 2 + iconSize + resize;
        layoutParams4.topMargin = paddingV * 2 + iconSize + resize;
        if (Helper.checkIfTablet(activity)) {
            layoutParams4.leftMargin = width / 2 + paddingV / 2 + resize;
        }
        if (horizontal) {
            layoutParams4.leftMargin = paddingH + iconSize + resize + padding;
            layoutParams4.topMargin = (int) (paddingV * 0.5 + resize);
            if (Helper.checkIfTablet(activity)) {
                layoutParams4.topMargin = height / 2 - paddingH / 2 - iconSize + resize;
            }
        }
        button4.setLayoutParams(layoutParams4);

        FrameLayout.LayoutParams layoutParams5 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
        layoutParams5.leftMargin = paddingH + resize;
        layoutParams5.topMargin = paddingV * 3 + iconSize * 2 + resize;
        if (Helper.checkIfTablet(activity)) {
            layoutParams5.leftMargin = width / 2 - paddingV / 2 - iconSize + resize;
        }
        if (horizontal) {
            layoutParams5.leftMargin = paddingH * 2 + iconSize * 2 + resize + padding;
            layoutParams5.topMargin = (int) (paddingV * 1.5 + iconSize + resize);
            if (Helper.checkIfTablet(activity)) {
                layoutParams5.topMargin = height / 2 + paddingH / 2 + resize;
            }
        }
        button5.setLayoutParams(layoutParams5);

        FrameLayout.LayoutParams layoutParams6 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
        layoutParams6.leftMargin = paddingH * 2 + iconSize + resize;
        layoutParams6.topMargin = paddingV * 3 + iconSize * 2 + resize;
        if (Helper.checkIfTablet(activity)) {
            layoutParams6.leftMargin = width / 2 + paddingV / 2 + resize;
        }
        if (horizontal) {
            layoutParams6.leftMargin = paddingH * 2 + iconSize * 2 + resize + padding;
            layoutParams6.topMargin = (int) (paddingV * 0.5 + resize);
            if (Helper.checkIfTablet(activity)) {
                layoutParams6.topMargin = height / 2 - paddingH / 2 - iconSize + resize;
            }
        }
        button6.setLayoutParams(layoutParams6);

        FrameLayout.LayoutParams layoutParams7 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
        layoutParams7.leftMargin = paddingH + resize;
        layoutParams7.topMargin = paddingV * 4 + iconSize * 3 + resize;
        if (Helper.checkIfTablet(activity)) {
            layoutParams7.leftMargin = width / 2 - paddingV / 2 - iconSize + resize;
        }
        if (horizontal) {
            layoutParams7.leftMargin = paddingH * 3 + iconSize * 3 + resize + padding;
            layoutParams7.topMargin = (int) (paddingV * 1.5 + iconSize + resize);
            if (Helper.checkIfTablet(activity)) {
                layoutParams7.topMargin = height / 2 + paddingH / 2 + resize;
            }
        }
        button7.setLayoutParams(layoutParams7);

        FrameLayout.LayoutParams layoutParams8 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
        layoutParams8.leftMargin = paddingH * 2 + iconSize + resize;
        layoutParams8.topMargin = paddingV * 4 + iconSize * 3 + resize;
        if (Helper.checkIfTablet(activity)) {
            layoutParams8.leftMargin = width / 2 + paddingV / 2 + resize;
        }
        if (horizontal) {
            layoutParams8.leftMargin = paddingH * 3 + iconSize * 3 + resize + padding;
            layoutParams8.topMargin = (int) (paddingV * 0.5 + resize);
            if (Helper.checkIfTablet(activity)) {
                layoutParams8.topMargin = height / 2 - paddingH / 2 - iconSize + resize;
            }
        }
        button8.setLayoutParams(layoutParams8);

        power = findViewById(R.id.power);
        int size = dpToPx(getContext(), 60);
        FrameLayout.LayoutParams powerLayoutParams = new FrameLayout.LayoutParams(size, size);
        if (horizontal) {
            powerLayoutParams.leftMargin = (int) (iconSize * 2 + paddingH * 1.5f - size / 2) + padding;
            powerLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        } else {
            powerLayoutParams.gravity = Gravity.CENTER;
        }
        power.setLayoutParams(powerLayoutParams);

        if (!Helper.checkIfTablet(activity)) {
            ImageView image = findViewById(R.id.status_bar);
            FrameLayout.LayoutParams layoutParams9 = (FrameLayout.LayoutParams) image.getLayoutParams();
            LinearLayout statusLayout = findViewById(R.id.status_layout);
            FrameLayout.LayoutParams layoutParams10 = (FrameLayout.LayoutParams) statusLayout.getLayoutParams();
            if (horizontal) {
                paddingV = (int) (height * PADDING / (PADDING * 2 + icon * 2));
                iconSize = (int) (height * icon / (PADDING * 2 + icon * 2));
                paddingH = (width - iconSize * 4) / 4;
                layoutParams9.height = (int) ((iconSize * 2 + paddingV) * STATUS_RATIO);
                layoutParams10.height = (int) ((iconSize * 2 + paddingV) * STATUS_RATIO);
            } else {
                layoutParams9.width = (int) ((iconSize * 2 + paddingH) * STATUS_RATIO);
                layoutParams10.width = (int) ((iconSize * 2 + paddingH) * STATUS_RATIO);
            }
            image.setLayoutParams(layoutParams9);
            statusLayout.setLayoutParams(layoutParams10);
        }

        reset();
    }

    public void reset() {
        deviceData = AppGlobal.findConnectedDevice(deviceData);
        if (deviceData != null) {
            button1.setData(deviceData, deviceData.getButton(1));
            button2.setData(deviceData, deviceData.getButton(2));
            button3.setData(deviceData, deviceData.getButton(3));
            button4.setData(deviceData, deviceData.getButton(4));
            button5.setData(deviceData, deviceData.getButton(5));
            button6.setData(deviceData, deviceData.getButton(6));
            button7.setData(deviceData, deviceData.getButton(7));
            button8.setData(deviceData, deviceData.getButton(8));
            if (deviceData.isPowerOn()) {
                power.setPowerClickable(true);
            } else {
                power.setPowerClickable(false);
            }
        }
    }

    public void powerOff() {
        button1.turnOff();
        button2.turnOff();
        button3.turnOff();
        button4.turnOff();
        button5.turnOff();
        button6.turnOff();
        button7.turnOff();
        button8.turnOff();
    }
}
