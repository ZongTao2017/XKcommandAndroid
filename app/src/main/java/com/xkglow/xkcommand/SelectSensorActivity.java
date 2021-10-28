package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;

public class SelectSensorActivity extends Activity {
    TextView sensorName1, sensorName2, sensorName3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sensor);

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FrameLayout sensor1 = findViewById(R.id.sensor_1);
        sensor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSensorActivity.this, EditSensorActivity.class);
                intent.putExtra("sensor", AppGlobal.getSensors()[0]);
                startActivity(intent);
            }
        });

        FrameLayout sensor2 = findViewById(R.id.sensor_2);
        sensor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSensorActivity.this, EditSensorActivity.class);
                intent.putExtra("sensor", AppGlobal.getSensors()[1]);
                startActivity(intent);
            }
        });

        FrameLayout sensor3 = findViewById(R.id.sensor_3);
        sensor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSensorActivity.this, EditSensorActivity.class);
                intent.putExtra("sensor", AppGlobal.getSensors()[2]);
                startActivity(intent);
            }
        });

        sensorName1 = findViewById(R.id.sensor_1_name);
        sensorName2 = findViewById(R.id.sensor_2_name);
        sensorName3 = findViewById(R.id.sensor_3_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorName1.setText(AppGlobal.getSensors()[0].name);
        sensorName2.setText(AppGlobal.getSensors()[1].name);
        sensorName3.setText(AppGlobal.getSensors()[2].name);
    }
}
