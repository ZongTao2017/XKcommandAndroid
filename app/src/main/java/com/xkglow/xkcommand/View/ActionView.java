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

import java.util.ArrayList;

public class ActionView extends LinearLayout {
    private SensorData sensorData;
    private ButtonData buttonData;
    private ArrayList<LinearLayout> radioButtons;
    private ArrayList<ImageView> radioButtonImages;
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

        radioButtons = new ArrayList<>();
        LinearLayout radioButton1 = findViewById(R.id.radio_button_1);
        radioButtons.add(radioButton1);
        LinearLayout radioButton2 = findViewById(R.id.radio_button_2);
        radioButtons.add(radioButton2);
        LinearLayout radioButton3 = findViewById(R.id.radio_button_3);
        radioButtons.add(radioButton3);
        LinearLayout radioButton4 = findViewById(R.id.radio_button_4);
        radioButtons.add(radioButton4);
        LinearLayout radioButton5 = findViewById(R.id.radio_button_5);
        radioButtons.add(radioButton5);
        LinearLayout radioButton6 = findViewById(R.id.radio_button_6);
        radioButtons.add(radioButton6);
        LinearLayout radioButton7 = findViewById(R.id.radio_button_7);
        radioButtons.add(radioButton7);
        LinearLayout radioButton8 = findViewById(R.id.radio_button_8);
        radioButtons.add(radioButton8);
        LinearLayout radioButton9 = findViewById(R.id.radio_button_9);
        radioButtons.add(radioButton9);
        LinearLayout radioButton10 = findViewById(R.id.radio_button_10);
        radioButtons.add(radioButton10);
        LinearLayout radioButton11 = findViewById(R.id.radio_button_11);
        radioButtons.add(radioButton11);
        LinearLayout radioButton12 = findViewById(R.id.radio_button_12);
        radioButtons.add(radioButton12);
        LinearLayout radioButton13 = findViewById(R.id.radio_button_13);
        radioButtons.add(radioButton13);
        LinearLayout radioButton14 = findViewById(R.id.radio_button_14);
        radioButtons.add(radioButton14);
        LinearLayout radioButton15 = findViewById(R.id.radio_button_15);
        radioButtons.add(radioButton15);

        radioButtonImages = new ArrayList<>();
        ImageView radioButtonImage1 = findViewById(R.id.radio_button_image_1);
        radioButtonImages.add(radioButtonImage1);
        ImageView radioButtonImage2 = findViewById(R.id.radio_button_image_2);
        radioButtonImages.add(radioButtonImage2);
        ImageView radioButtonImage3 = findViewById(R.id.radio_button_image_3);
        radioButtonImages.add(radioButtonImage3);
        ImageView radioButtonImage4 = findViewById(R.id.radio_button_image_4);
        radioButtonImages.add(radioButtonImage4);
        ImageView radioButtonImage5 = findViewById(R.id.radio_button_image_5);
        radioButtonImages.add(radioButtonImage5);
        ImageView radioButtonImage6 = findViewById(R.id.radio_button_image_6);
        radioButtonImages.add(radioButtonImage6);
        ImageView radioButtonImage7 = findViewById(R.id.radio_button_image_7);
        radioButtonImages.add(radioButtonImage7);
        ImageView radioButtonImage8 = findViewById(R.id.radio_button_image_8);
        radioButtonImages.add(radioButtonImage8);
        ImageView radioButtonImage9 = findViewById(R.id.radio_button_image_9);
        radioButtonImages.add(radioButtonImage9);
        ImageView radioButtonImage10 = findViewById(R.id.radio_button_image_10);
        radioButtonImages.add(radioButtonImage10);
        ImageView radioButtonImage11 = findViewById(R.id.radio_button_image_11);
        radioButtonImages.add(radioButtonImage11);
        ImageView radioButtonImage12 = findViewById(R.id.radio_button_image_12);
        radioButtonImages.add(radioButtonImage12);
        ImageView radioButtonImage13 = findViewById(R.id.radio_button_image_13);
        radioButtonImages.add(radioButtonImage13);
        ImageView radioButtonImage14 = findViewById(R.id.radio_button_image_14);
        radioButtonImages.add(radioButtonImage14);
        ImageView radioButtonImage15 = findViewById(R.id.radio_button_image_15);
        radioButtonImages.add(radioButtonImage15);

        actionNameTextView = findViewById(R.id.action_name);

        for (int i = 0; i < radioButtons.size(); i++) {
            LinearLayout radioButton = radioButtons.get(i);
            final int index = i;
            radioButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sensorData != null) {
                        for (int channelId : channelIds) {
                            sensorData.actions[channelId] = index + 1;
                            if (index == 7)
                                sensorData.actions[channelId] = 230;
                            if (index == 8)
                                sensorData.actions[channelId] = 204;
                            if (index == 9)
                                sensorData.actions[channelId] = 179;
                            if (index == 10)
                                sensorData.actions[channelId] = 153;
                            if (index == 11)
                                sensorData.actions[channelId] = 128;
                            if (index == 12)
                                sensorData.actions[channelId] = 102;
                            if (index == 13)
                                sensorData.actions[channelId] = 77;
                            if (index == 14)
                                sensorData.actions[channelId] = 51;
                        }
                        AppGlobal.getCurrentDevice().setSensor(sensorData);
                    }
                    if (buttonData != null) {
                        for (int channelId : channelIds) {
                            buttonData.actions[channelId] = index + 1;
                        }
                        AppGlobal.getCurrentDevice().setButton(buttonData);
                    }
                    setRadioImages();
                }
            });
        }
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
        for (ImageView radioButtonImage : radioButtonImages) {
            radioButtonImage.setImageResource(R.drawable.radio_unselected);
        }

        if (sensorData != null) {
            int index = sensorData.actions[channelIds[0]];
            if (index == 230) index = 7;
            else if (index == 204) index = 8;
            else if (index == 179) index = 9;
            else if (index == 153) index = 10;
            else if (index == 128) index = 11;
            else if (index == 102) index = 12;
            else if (index == 77) index = 13;
            else if (index == 51) index = 14;
            else index = index - 1;
            radioButtonImages.get(index).setImageResource(R.drawable.radio_selected);
        }
        if (buttonData != null) {
            int index = buttonData.actions[channelIds[0]];
            if (index == 230) index = 7;
            else if (index == 204) index = 8;
            else if (index == 179) index = 9;
            else if (index == 153) index = 10;
            else if (index == 128) index = 11;
            else if (index == 102) index = 12;
            else if (index == 77) index = 13;
            else if (index == 51) index = 14;
            else index = index - 1;
            radioButtonImages.get(index).setImageResource(R.drawable.radio_selected);
        }
    }
}
