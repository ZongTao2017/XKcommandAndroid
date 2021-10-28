package com.xkglow.xkcommand.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.SensorData;
import com.xkglow.xkcommand.R;

public class ActionView extends LinearLayout {
    private SensorData sensorData;
    private ButtonData buttonData;
    private LinearLayout radioButton1, radioButton2, radioButton3, radioButton4;
    private ImageView radioButtonImage1, radioButtonImage2, radioButtonImage3, radioButtonImage4;
    private TextView actionNameTextView;

    public ActionView(Context context) {
        super(context);
        init();
    }

    public ActionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.action_layout, this);

        radioButton1 = findViewById(R.id.radio_button_1);
        radioButton2 = findViewById(R.id.radio_button_2);
        radioButton3 = findViewById(R.id.radio_button_3);
        radioButton4 = findViewById(R.id.radio_button_4);
        radioButtonImage1 = findViewById(R.id.radio_button_image_1);
        radioButtonImage2 = findViewById(R.id.radio_button_image_2);
        radioButtonImage3 = findViewById(R.id.radio_button_image_3);
        radioButtonImage4 = findViewById(R.id.radio_button_image_4);
        actionNameTextView = findViewById(R.id.action_name);

        radioButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioImages();
                if (sensorData != null) {
                    sensorData.action = 0;
                    AppGlobal.setSensor(sensorData);
                }
                if (buttonData != null) {
                    buttonData.action = 0;
                    AppGlobal.setButton(buttonData);
                }
            }
        });
        radioButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.action = 1;
                setRadioImages();
                AppGlobal.setSensor(sensorData);
            }
        });
        radioButton3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.action = 2;
                setRadioImages();
                AppGlobal.setSensor(sensorData);
            }
        });
        radioButton4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.action = 3;
                setRadioImages();
                AppGlobal.setSensor(sensorData);
            }
        });
    }

    public void setSensorData(SensorData sensorData) {
        this.sensorData = sensorData;
        setRadioImages();
    }

    public void setButtonData(ButtonData buttonData) {
        this.buttonData = buttonData;
        setRadioImages();
    }

    public void setActionName(String actionName) {
        actionNameTextView.setText(actionName);
    }

    private void setRadioImages() {
        radioButtonImage1.setImageResource(R.drawable.radio_unselected);
        radioButtonImage2.setImageResource(R.drawable.radio_unselected);
        radioButtonImage3.setImageResource(R.drawable.radio_unselected);
        radioButtonImage4.setImageResource(R.drawable.radio_unselected);
        switch (sensorData.action) {
            case 0:
                radioButtonImage1.setImageResource(R.drawable.radio_selected);
                break;
            case 1:
                radioButtonImage2.setImageResource(R.drawable.radio_selected);
                break;
            case 2:
                radioButtonImage3.setImageResource(R.drawable.radio_selected);
                break;
            case 3:
                radioButtonImage4.setImageResource(R.drawable.radio_selected);
                break;
        }
    }
}
