package com.xkglow.xkcommand.View;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xkglow.xkcommand.DevicePairActivity;
import com.xkglow.xkcommand.R;

public class DeviceList extends ScrollView {

    public DeviceList(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(getContext(), R.layout.device_list, this);

        LinearLayout pairedLayout = findViewById(R.id.paired_device_layout);
        PairedDeviceView device1 = findViewById(R.id.device_1);
        device1.setCurrent(true);
        PairedDeviceView device2 = findViewById(R.id.device_2);
        device2.setCurrent(false);
        UnpairedDeviceView device3 = findViewById(R.id.device_3);
        device3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DevicePairActivity.class));
            }
        });

        LinearLayout unpairedLayout = findViewById(R.id.unpaired_device_layout);

    }
}
