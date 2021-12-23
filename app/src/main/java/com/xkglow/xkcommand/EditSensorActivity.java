package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.Helper.SensorData;
import com.xkglow.xkcommand.View.ActionView;
import com.xkglow.xkcommand.View.SwitchView;

public class EditSensorActivity extends Activity {
    SensorData sensorData;
    TextView sensorNameTextView, brightnessTextView, noChannelSelectedTextView, functionAction, functionDim, channelDescription;
    LinearLayout radioButtonDim, radioButtonTrigger, channel1, channel2, channel3, channel4, channel5, channel6, channel7, channel8, syncAll;
    FrameLayout minus, plus;
    ImageView radioButtonDimImage, radioButtonTriggerImage, channelImage1, channelImage2, channelImage3, channelImage4, channelImage5, channelImage6, channelImage7, channelImage8, minusImage, plusImage;
    LinearLayout dimLayout, channelLayout, actionLayout;
    SwitchView syncSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);
        sensorData = (SensorData) getIntent().getSerializableExtra("sensor");

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGlobal.writeSensorData(sensorData.id);
                finish();
            }
        });

        TextView sensorId = findViewById(R.id.sensor_id);
        sensorId.setText("Sensor " + sensorData.id);

        LinearLayout editSensorName = findViewById(R.id.edit_sensor_name);
        editSensorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditSensorActivity.this, EditNameActivity.class);
                intent.putExtra("sensor", sensorData);
                startActivity(intent);
            }
        });

        sensorNameTextView = findViewById(R.id.sensor_name);
        radioButtonDim = findViewById(R.id.radio_button_dim);
        radioButtonDimImage = findViewById(R.id.radio_button_image_dim);
        radioButtonTrigger = findViewById(R.id.radio_button_trigger);
        radioButtonTriggerImage = findViewById(R.id.radio_button_image_trigger);
        functionDim = findViewById(R.id.sensor_function_dim);
        functionAction = findViewById(R.id.sensor_function_action);
        syncSwitch = findViewById(R.id.switch_sync);
        channel1 = findViewById(R.id.channel_1);
        channelImage1 = findViewById(R.id.channel_image_1);
        channel2 = findViewById(R.id.channel_2);
        channelImage2 = findViewById(R.id.channel_image_2);
        channel3 = findViewById(R.id.channel_3);
        channelImage3 = findViewById(R.id.channel_image_3);
        channel4 = findViewById(R.id.channel_4);
        channelImage4 = findViewById(R.id.channel_image_4);
        channel5 = findViewById(R.id.channel_5);
        channelImage5 = findViewById(R.id.channel_image_5);
        channel6 = findViewById(R.id.channel_6);
        channelImage6 = findViewById(R.id.channel_image_6);
        channel7 = findViewById(R.id.channel_7);
        channelImage7 = findViewById(R.id.channel_image_7);
        channel8 = findViewById(R.id.channel_8);
        channelImage8 = findViewById(R.id.channel_image_8);
        minus = findViewById(R.id.minus);
        minusImage = findViewById(R.id.minus_image);
        plus = findViewById(R.id.plus);
        plusImage = findViewById(R.id.plus_image);

        dimLayout = findViewById(R.id.button_back_light);
        channelLayout = findViewById(R.id.channel_layout);
        actionLayout = findViewById(R.id.action_layout);
        brightnessTextView = findViewById(R.id.brightness);
        noChannelSelectedTextView = findViewById(R.id.no_channel_selected);
        channelDescription = findViewById(R.id.channel_description);
        syncAll = findViewById(R.id.sync_all_channels_layout);

        setChannels();
        if (sensorData.function == 0) {
            setButtonBackLight();
        } else {
            setButtonTrigger();
        }

        brightnessTextView.setText(sensorData.brightness + "%");

        radioButtonDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.function = 0;
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setButtonBackLight();
            }
        });
        radioButtonTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.function = 1;
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setButtonTrigger();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData.brightness > 0) {
                    sensorData.brightness = sensorData.brightness - 5;
                    brightnessTextView.setText(sensorData.brightness + "%");
                    AppGlobal.getCurrentDevice().setSensor(sensorData);
                }
                setBackColorButtons();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData.brightness < 100) {
                    sensorData.brightness = sensorData.brightness + 5;
                    brightnessTextView.setText(sensorData.brightness + "%");
                    AppGlobal.getCurrentDevice().setSensor(sensorData);
                }
                setBackColorButtons();
            }
        });

        channel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[0] = !sensorData.channels[0];
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });
        channel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[1] = !sensorData.channels[1];
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });
        channel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[2] = !sensorData.channels[2];
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });
        channel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[3] = !sensorData.channels[3];
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });
        channel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[4] = !sensorData.channels[4];
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });
        channel6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[5] = !sensorData.channels[5];
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });
        channel7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[6] = !sensorData.channels[6];
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });
        channel8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[7] = !sensorData.channels[7];
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });

        syncSwitch.setOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onSwitchOn(boolean showMessage) {
                sensorData.sync = true;
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }

            @Override
            public void onSwitchOff(boolean showMessage) {
                sensorData.sync = false;
                AppGlobal.getCurrentDevice().setSensor(sensorData);
                setChannels();
            }
        });

        setChannelNames();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorData = AppGlobal.getCurrentDevice().getSensor(sensorData.id);
        sensorNameTextView.setText(sensorData.name);
        setBackColorButtons();
        if (AppGlobal.getCurrentDevice().hasDim() && sensorData.function == 1) {
            radioButtonDim.setAlpha(0.4f);
            radioButtonDim.setClickable(false);
        }
        if (sensorData.function == 0) {
            functionDim.setVisibility(View.VISIBLE);
            functionAction.setVisibility(View.GONE);
        }
        if (sensorData.function == 1) {
            functionDim.setVisibility(View.GONE);
            functionAction.setVisibility(View.VISIBLE);
        }
        syncSwitch.post(new Runnable() {
            @Override
            public void run() {
                setSyncSwitch();
            }
        });
    }

    private void setChannelNames() {
        TextView channelName1 = findViewById(R.id.channel_text_1);
        channelName1.setText(AppGlobal.getCurrentDevice().getChannel(1).name);
        TextView channelName2 = findViewById(R.id.channel_text_2);
        channelName2.setText(AppGlobal.getCurrentDevice().getChannel(2).name);
        TextView channelName3 = findViewById(R.id.channel_text_3);
        channelName3.setText(AppGlobal.getCurrentDevice().getChannel(3).name);
        TextView channelName4 = findViewById(R.id.channel_text_4);
        channelName4.setText(AppGlobal.getCurrentDevice().getChannel(4).name);
        TextView channelName5 = findViewById(R.id.channel_text_5);
        channelName5.setText(AppGlobal.getCurrentDevice().getChannel(5).name);
        TextView channelName6 = findViewById(R.id.channel_text_6);
        channelName6.setText(AppGlobal.getCurrentDevice().getChannel(6).name);
        TextView channelName7 = findViewById(R.id.channel_text_7);
        channelName7.setText(AppGlobal.getCurrentDevice().getChannel(7).name);
        TextView channelName8 = findViewById(R.id.channel_text_8);
        channelName8.setText(AppGlobal.getCurrentDevice().getChannel(8).name);
    }

    private void setChannel(ImageView imageView, boolean select) {
        if (select) {
            imageView.setImageResource(R.drawable.check_selected);
        } else {
            imageView.setImageResource(R.drawable.check_unselected);
        }
    }

    private void setButtonBackLight() {
        radioButtonDimImage.setImageResource(R.drawable.radio_selected);
        radioButtonTriggerImage.setImageResource(R.drawable.radio_unselected);
        dimLayout.setVisibility(View.VISIBLE);
        actionLayout.setVisibility(View.GONE);
        channelLayout.setVisibility(View.GONE);
        functionDim.setVisibility(View.VISIBLE);
        functionAction.setVisibility(View.GONE);
    }

    private void setButtonTrigger() {
        radioButtonDimImage.setImageResource(R.drawable.radio_unselected);
        radioButtonTriggerImage.setImageResource(R.drawable.radio_selected);
        dimLayout.setVisibility(View.GONE);
        actionLayout.setVisibility(View.VISIBLE);
        channelLayout.setVisibility(View.VISIBLE);
        functionDim.setVisibility(View.GONE);
        functionAction.setVisibility(View.VISIBLE);
    }

    private void setBackColorButtons() {
        if (sensorData.brightness <= 20) {
            minusImage.setColorFilter(getResources().getColor(R.color.gray));
            minus.setClickable(false);
        } else if (sensorData.brightness >= 100) {
            plusImage.setColorFilter(getResources().getColor(R.color.gray));
            plus.setClickable(false);
        } else {
            plusImage.setColorFilter(getResources().getColor(R.color.blue));
            minusImage.setColorFilter(getResources().getColor(R.color.blue));
            plus.setClickable(true);
            minus.setClickable(true);
        }
    }

    private void setSyncSwitch() {
        if (sensorData.sync) {
            syncSwitch.switchOn(false, false);
        } else {
            syncSwitch.switchOff(false, false);
        }
        setChannels();
    }

    private void setChannels() {
        actionLayout.removeAllViews();
        boolean flag = false;
        setChannel(channelImage1, sensorData.channels[0]);
        if (sensorData.channels[0]) {
            flag = true;
            if (!sensorData.sync) {
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(1).name);
                actionView.setSensorData(sensorData, new int[] { 0 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage2, sensorData.channels[1]);
        if (sensorData.channels[1]) {
            flag = true;
            if (!sensorData.sync) {
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(2).name);
                actionView.setSensorData(sensorData, new int[] { 1 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage3, sensorData.channels[2]);
        if (sensorData.channels[2]) {
            flag = true;
            if (!sensorData.sync) {
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(3).name);
                actionView.setSensorData(sensorData, new int[] { 2 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage4, sensorData.channels[3]);
        if (sensorData.channels[3]) {
            flag = true;
            if (!sensorData.sync) {
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(4).name);
                actionView.setSensorData(sensorData, new int[] { 3 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage5, sensorData.channels[4]);
        if (sensorData.channels[4]) {
            flag = true;
            if (!sensorData.sync) {
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(5).name);
                actionView.setSensorData(sensorData, new int[] { 4 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage6, sensorData.channels[5]);
        if (sensorData.channels[5]) {
            flag = true;
            if (!sensorData.sync) {
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(6).name);
                actionView.setSensorData(sensorData, new int[] { 5 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage7, sensorData.channels[6]);
        if (sensorData.channels[6]) {
            flag = true;
            if (!sensorData.sync) {
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(7).name);
                actionView.setSensorData(sensorData, new int[] { 6 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage8, sensorData.channels[7]);
        if (sensorData.channels[7]) {
            flag = true;
            if (!sensorData.sync) {
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(8).name);
                actionView.setSensorData(sensorData, new int[] { 7 });
                actionLayout.addView(actionView);
            }
        }
        if (flag) {
            channelDescription.setVisibility(View.VISIBLE);
            syncAll.setVisibility(View.VISIBLE);
            noChannelSelectedTextView.setVisibility(View.GONE);
            actionLayout.setVisibility(View.VISIBLE);
            if (sensorData.sync) {
                int number = 0;
                for (boolean channel : sensorData.channels) {
                    if (channel) number++;
                }
                int[] actions = new int[number];
                int n = 0;
                for (int i = 0; i < sensorData.channels.length; i++) {
                    if (sensorData.channels[i]) {
                        actions[n++] = i;
                    }
                }
                ActionView actionView = new ActionView(EditSensorActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditSensorActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - ALL CHANNELS");
                actionView.setSensorData(sensorData, actions);
                actionLayout.addView(actionView);
            }
        } else {
            channelDescription.setVisibility(View.GONE);
            syncAll.setVisibility(View.GONE);
            noChannelSelectedTextView.setVisibility(View.VISIBLE);
            actionLayout.setVisibility(View.GONE);
        }
        if (sensorData.function == 0) {
            actionLayout.setVisibility(View.GONE);
        }
    }
}
