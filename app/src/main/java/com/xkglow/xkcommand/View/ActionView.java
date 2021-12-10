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
    private int[] channelIds;

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
                    for (int channelId : channelIds) {
                        sensorData.actions[channelId] = 1;
                    }
                    AppGlobal.getCurrentDevice().setSensor(sensorData);
                }
                if (buttonData != null) {
                    for (int channelId : channelIds) {
                        buttonData.actions[channelId] = 1;
                    }
                    AppGlobal.getCurrentDevice().setButton(buttonData);
                }
                setRadioImages();
            }
        });
        radioButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData != null) {
                    for (int channelId : channelIds) {
                        sensorData.actions[channelId] = 2;
                    }
                    AppGlobal.getCurrentDevice().setSensor(sensorData);
                }
                if (buttonData != null) {
                    for (int channelId : channelIds) {
                        buttonData.actions[channelId] = 2;
                    }
                    AppGlobal.getCurrentDevice().setButton(buttonData);
                }
                setRadioImages();
            }
        });
        radioButton3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData != null) {
                    for (int channelId : channelIds) {
                        sensorData.actions[channelId] = 3;
                    }
                    AppGlobal.getCurrentDevice().setSensor(sensorData);
                }
                if (buttonData != null) {
                    for (int channelId : channelIds) {
                        buttonData.actions[channelId] = 3;
                    }
                    AppGlobal.getCurrentDevice().setButton(buttonData);
                }
                setRadioImages();
            }
        });
        radioButton4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData != null) {
                    for (int channelId : channelIds) {
                        sensorData.actions[channelId] = 4;
                    }
                    AppGlobal.getCurrentDevice().setSensor(sensorData);
                }
                if (buttonData != null) {
                    for (int channelId : channelIds) {
                        buttonData.actions[channelId] = 4;
                    }
                    AppGlobal.getCurrentDevice().setButton(buttonData);
                }
                setRadioImages();
            }
        });
    }

    public void setSensorData(SensorData sensorData, int[] channelIds) {
        this.sensorData = sensorData;
        this.channelIds = channelIds;
        setRadioImages();
    }

    public void setButtonData(ButtonData buttonData, int[] channelIds) {
        this.buttonData = buttonData;
        this.channelIds = channelIds;
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
            switch (sensorData.actions[channelIds[0]]) {
                case 1:
                    radioButtonImage1.setImageResource(R.drawable.radio_selected);
                    break;
                case 2:
                    radioButtonImage2.setImageResource(R.drawable.radio_selected);
                    break;
                case 3:
                    radioButtonImage3.setImageResource(R.drawable.radio_selected);
                    break;
                case 4:
                    radioButtonImage4.setImageResource(R.drawable.radio_selected);
                    break;
            }
        }
        if (buttonData != null) {
            switch (buttonData.actions[channelIds[0]]) {
                case 1:
                    radioButtonImage1.setImageResource(R.drawable.radio_selected);
                    break;
                case 2:
                    radioButtonImage2.setImageResource(R.drawable.radio_selected);
                    break;
                case 3:
                    radioButtonImage3.setImageResource(R.drawable.radio_selected);
                    break;
                case 4:
                    radioButtonImage4.setImageResource(R.drawable.radio_selected);
                    break;
            }
        }
    }
}
