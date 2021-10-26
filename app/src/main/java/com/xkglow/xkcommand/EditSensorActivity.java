package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.SensorData;

public class EditSensorActivity extends Activity {
    SensorData sensorData;
    TextView sensorNameTextView, brightnessTextView, dimTextView;
    LinearLayout radioButtonDim, radioButtonTrigger, channel1, channel2, channel3, channel4, channel5, channel6, channel7, channel8;
    FrameLayout minus, plus;
    ImageView radioButtonDimImage, radioButtonTriggerImage, channelImage1, channelImage2, channelImage3, channelImage4, channelImage5, channelImage6, channelImage7, channelImage8, minusImage, plusImage;
    LinearLayout dimLayout, channelLayout;
    ActionView actionView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);
        sensorData = (SensorData) getIntent().getSerializableExtra("sensor");

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView sensorId = findViewById(R.id.sensor_id);
        sensorId.setText(sensorData.id);

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
        dimTextView = findViewById(R.id.dim_text);

        dimLayout = findViewById(R.id.button_back_light);
        channelLayout = findViewById(R.id.channel_layout);
        actionView = findViewById(R.id.action_view);
        brightnessTextView = findViewById(R.id.brightness);

        if (sensorData.function == 0) {
            setButtonBackLight();
        } else {
            setButtonTrigger();
        }
        setChannel(channelImage1, sensorData.channels[0]);
        setChannel(channelImage2, sensorData.channels[1]);
        setChannel(channelImage3, sensorData.channels[2]);
        setChannel(channelImage4, sensorData.channels[3]);
        setChannel(channelImage5, sensorData.channels[4]);
        setChannel(channelImage6, sensorData.channels[5]);
        setChannel(channelImage7, sensorData.channels[6]);
        setChannel(channelImage8, sensorData.channels[7]);
        actionView.setSensorData(sensorData);
        brightnessTextView.setText(sensorData.brightness + "%");

        radioButtonDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.function = 0;
                AppGlobal.setSensor(sensorData);
                setButtonBackLight();
            }
        });
        radioButtonTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.function = 1;
                AppGlobal.setSensor(sensorData);
                setButtonTrigger();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData.brightness > 0) {
                    sensorData.brightness = sensorData.brightness - 10;
                    brightnessTextView.setText(sensorData.brightness + "%");
                    AppGlobal.setSensor(sensorData);
                }
                setBackColorButtons();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorData.brightness < 100) {
                    sensorData.brightness = sensorData.brightness + 10;
                    brightnessTextView.setText(sensorData.brightness + "%");
                    AppGlobal.setSensor(sensorData);
                }
                setBackColorButtons();
            }
        });

        channel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[0] = !sensorData.channels[0];
                AppGlobal.setSensor(sensorData);
                setChannel(channelImage1, sensorData.channels[0]);
            }
        });
        channel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[1] = !sensorData.channels[1];
                AppGlobal.setSensor(sensorData);
                setChannel(channelImage2, sensorData.channels[1]);
            }
        });
        channel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[2] = !sensorData.channels[2];
                AppGlobal.setSensor(sensorData);
                setChannel(channelImage3, sensorData.channels[2]);
            }
        });
        channel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[3] = !sensorData.channels[3];
                AppGlobal.setSensor(sensorData);
                setChannel(channelImage4, sensorData.channels[3]);
            }
        });
        channel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[4] = !sensorData.channels[4];
                AppGlobal.setSensor(sensorData);
                setChannel(channelImage5, sensorData.channels[4]);
            }
        });
        channel6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[5] = !sensorData.channels[5];
                AppGlobal.setSensor(sensorData);
                setChannel(channelImage6, sensorData.channels[5]);
            }
        });
        channel7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[6] = !sensorData.channels[6];
                AppGlobal.setSensor(sensorData);
                setChannel(channelImage7, sensorData.channels[6]);
            }
        });
        channel8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.channels[7] = !sensorData.channels[7];
                AppGlobal.setSensor(sensorData);
                setChannel(channelImage8, sensorData.channels[7]);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorNameTextView.setText(sensorData.name);
        setBackColorButtons();
        if (AppGlobal.hasDim() && sensorData.function == 1) {
            dimTextView.setTextColor(getResources().getColor(R.color.gray));
            radioButtonDim.setClickable(false);
        }
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
        actionView.setVisibility(View.GONE);
        channelLayout.setVisibility(View.GONE);
    }

    private void setButtonTrigger() {
        radioButtonDimImage.setImageResource(R.drawable.radio_unselected);
        radioButtonTriggerImage.setImageResource(R.drawable.radio_selected);
        dimLayout.setVisibility(View.GONE);
        actionView.setVisibility(View.VISIBLE);
        channelLayout.setVisibility(View.VISIBLE);
    }

    private void setBackColorButtons() {
        if (sensorData.brightness == 0) {
            minusImage.setColorFilter(getResources().getColor(R.color.gray));
            minus.setClickable(false);
        } else if (sensorData.brightness == 100) {
            plusImage.setColorFilter(getResources().getColor(R.color.gray));
            plus.setClickable(false);
        } else {
            plusImage.setColorFilter(getResources().getColor(R.color.blue));
            minusImage.setColorFilter(getResources().getColor(R.color.blue));
            plus.setClickable(true);
            minus.setClickable(true);
        }
    }
}
