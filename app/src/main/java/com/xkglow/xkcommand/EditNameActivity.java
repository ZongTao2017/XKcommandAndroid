package com.xkglow.xkcommand;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.SensorData;

public class EditNameActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        FrameLayout back = findViewById(R.id.back);

        final SensorData sensorData = (SensorData) getIntent().getSerializableExtra("sensor");
        EditText editText = findViewById(R.id.edit_name);
        editText.setText(sensorData.name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (!name.isEmpty()) {
                    sensorData.name = name;
                    AppGlobal.setSensor(sensorData);
                }
                finish();
            }
        });
    }
}
