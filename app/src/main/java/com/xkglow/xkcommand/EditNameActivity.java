package com.xkglow.xkcommand;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.ChannelData;
import com.xkglow.xkcommand.Helper.SensorData;
import com.xkglow.xkcommand.Helper.SystemData;

public class EditNameActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        FrameLayout back = findViewById(R.id.back);
        TextView title = findViewById(R.id.title);
        EditText editText = findViewById(R.id.edit_name);

        final SensorData sensorData = (SensorData) getIntent().getSerializableExtra("sensor");
        if (sensorData != null) {
            editText.setHint("Type sensor name");
            editText.setText(sensorData.name);
            title.setText("Sensor Name");
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editText.getText().toString();
                    if (!name.isEmpty()) {
                        sensorData.name = name;
                        AppGlobal.getCurrentDevice().setSensor(sensorData);
                    }
                    finish();
                }
            });
        }

        final ButtonData buttonData = (ButtonData) getIntent().getSerializableExtra("button");
        if (buttonData != null) {
            editText.setHint("Button " + buttonData.id);
            editText.setText(buttonData.text);
            title.setText("Button Name");
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editText.getText().toString();
                    if (!name.isEmpty()) {
                        buttonData.type = 1;
                        buttonData.text = name;
                        AppGlobal.getCurrentDevice().setButton(buttonData);
                    }
                    finish();
                }
            });
        }

        final ChannelData channelData = (ChannelData) getIntent().getSerializableExtra("channel");
        if (channelData != null) {
            editText.setHint("Type channel name");
            editText.setText(channelData.name);
            title.setText("Channel Name");
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editText.getText().toString();
                    if (!name.isEmpty()) {
                        channelData.name = name;
                        AppGlobal.getCurrentDevice().setChannel(channelData);
                    }
                    finish();
                }
            });
        }

        final SystemData systemData = (SystemData) getIntent().getSerializableExtra("system");
        if (systemData != null) {
            editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
            editText.setHint("Type controller name");
            editText.setText(systemData.name);
            title.setText("Controller Name");
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editText.getText().toString();
                    if (!name.isEmpty()) {
                        systemData.name = name;
                        AppGlobal.getCurrentDevice().setSystem(systemData);
                    }
                    finish();
                }
            });
        }
    }
}
