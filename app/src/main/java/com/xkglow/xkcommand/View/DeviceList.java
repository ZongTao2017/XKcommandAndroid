package com.xkglow.xkcommand.View;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xkglow.xkcommand.DevicePairActivity;
import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.R;

public class DeviceList extends ScrollView {

    public DeviceList(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(getContext(), R.layout.device_list, this);

        LinearLayout pairedLayout = findViewById(R.id.paired_device_layout);
        PairedDeviceView device1 = findViewById(R.id.device_1);
        PairedDeviceView device2 = findViewById(R.id.device_2);
        UnpairedDeviceView device3 = findViewById(R.id.device_3);

        device1.setCurrent(true);
        device1.setDeviceName(AppGlobal.getDevice(0).systemData.name);
        device1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                device1.setCurrent(true);
                device2.setCurrent(false);
                AppGlobal.setCurrentDevice(0);
            }
        });

        device2.setCurrent(false);
        device2.setDeviceName(AppGlobal.getDevice(1).systemData.name);
        device2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                device1.setCurrent(false);
                device2.setCurrent(true);
                AppGlobal.setCurrentDevice(1);
            }
        });

        device3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DevicePairActivity.class));
            }
        });

        LinearLayout unpairedLayout = findViewById(R.id.unpaired_device_layout);

    }
}
