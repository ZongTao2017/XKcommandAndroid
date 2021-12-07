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
import com.xkglow.xkcommand.Helper.ChannelData;

public class EditChannelActivity extends Activity {
    FrameLayout minus, plus;
    ImageView minusImage, plusImage;
    ChannelData channelData;
    TextView maxCurrentText, channelName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_channel);

        channelData = AppGlobal.getCurrentDevice().getChannel(getIntent().getIntExtra("channel", 0));

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGlobal.getCurrentDevice().setChannel(channelData);
                AppGlobal.writeChannelAmpLimit(channelData.id - 1, channelData.maxCurrent);
                finish();
            }
        });

        TextView title = findViewById(R.id.channel_id);
        title.setText("Channel " + channelData.id);
        channelName = findViewById(R.id.channel_name);

        LinearLayout channelNameLayout = findViewById(R.id.channel_name_layout);
        channelNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditChannelActivity.this, EditNameActivity.class);
                intent.putExtra("channel", channelData);
                startActivity(intent);
            }
        });

        minus = findViewById(R.id.minus);
        minusImage = findViewById(R.id.minus_image);
        plus = findViewById(R.id.plus);
        plusImage = findViewById(R.id.plus_image);
        maxCurrentText = findViewById(R.id.current);
        maxCurrentText.setText(channelData.maxCurrent + "A");

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (channelData.maxCurrent > 3) {
                    channelData.maxCurrent -= 1;
                    maxCurrentText.setText(channelData.maxCurrent + "A");
                    AppGlobal.getCurrentDevice().setChannel(channelData);
                }
                setButtons();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = 20;
                if (channelData.id > 4) {
                    max = 35;
                }
                if (channelData.maxCurrent < max) {
                    channelData.maxCurrent += 1;
                    maxCurrentText.setText(channelData.maxCurrent + "A");
                    AppGlobal.getCurrentDevice().setChannel(channelData);
                }
                setButtons();
            }
        });
    }

    private void setButtons() {
        int max = 20;
        if (channelData.id > 4) {
            max = 35;
        }
        if (channelData.maxCurrent == 3) {
            minusImage.setColorFilter(getResources().getColor(R.color.gray));
            minus.setClickable(false);
        } else if (channelData.maxCurrent == max) {
            plusImage.setColorFilter(getResources().getColor(R.color.gray));
            plus.setClickable(false);
        } else {
            plusImage.setColorFilter(getResources().getColor(R.color.blue));
            minusImage.setColorFilter(getResources().getColor(R.color.blue));
            plus.setClickable(true);
            minus.setClickable(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        channelData = AppGlobal.getCurrentDevice().getChannel(channelData.id);
        channelName.setText(channelData.name);
    }
}
