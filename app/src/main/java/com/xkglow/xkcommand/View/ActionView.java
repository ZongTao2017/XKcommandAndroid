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
    private int channelId;

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
                if (sensorData != null) {
                    sensorData.actions[channelId] = 0;
                    AppGlobal.setSensor(sensorData);
                }
                if (buttonData != null) {
                    buttonData.actions[channelId] = 0;
                    AppGlobal.setButton(buttonData);
                }
                setRadioImages();
            }
        });
        radioButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData != null) {
                    sensorData.actions[channelId] = 1;
                    AppGlobal.setSensor(sensorData);
                }
                if (buttonData != null) {
                    buttonData.actions[channelId] = 1;
                    AppGlobal.setButton(buttonData);
                }
                setRadioImages();
            }
        });
        radioButton3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData != null) {
                    sensorData.actions[channelId] = 2;
                    AppGlobal.setSensor(sensorData);
                }
                if (buttonData != null) {
                    buttonData.actions[channelId] = 1;
                    AppGlobal.setButton(buttonData);
                }
                setRadioImages();
            }
        });
        radioButton4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData != null) {
                    sensorData.actions[channelId] = 3;
                    AppGlobal.setSensor(sensorData);
                }
                if (buttonData != null) {
                    buttonData.actions[channelId] = 3;
                    AppGlobal.setButton(buttonData);
                }
                setRadioImages();
            }
        });
    }

    public void setSensorData(SensorData sensorData, int channelId) {
        this.sensorData = sensorData;
        this.channelId = channelId;
        setRadioImages();
    }

    public void setButtonData(ButtonData buttonData, int channelId) {
        this.buttonData = buttonData;
        this.channelId = channelId;
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
        if (sensorData != null) {
            switch (sensorData.actions[channelId]) {
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
        if (buttonData != null) {
            switch (buttonData.actions[channelId]) {
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
}
