package com.xkglow.xkcommand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ChannelData;

import java.util.List;

public class SelectChannelActivity extends Activity {
    TextView channelName1, channelName2, channelName3, channelName4, channelName5, channelName6, channelName7, channelName8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_channel);

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout channel1 = findViewById(R.id.channel_1);
        channel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChannelActivity.this, EditChannelActivity.class);
                intent.putExtra("channel", 1);
                startActivity(intent);
            }
        });
        LinearLayout channel2 = findViewById(R.id.channel_2);
        channel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChannelActivity.this, EditChannelActivity.class);
                intent.putExtra("channel", 2);
                startActivity(intent);
            }
        });
        LinearLayout channel3 = findViewById(R.id.channel_3);
        channel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChannelActivity.this, EditChannelActivity.class);
                intent.putExtra("channel", 3);
                startActivity(intent);
            }
        });
        LinearLayout channel4 = findViewById(R.id.channel_4);
        channel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChannelActivity.this, EditChannelActivity.class);
                intent.putExtra("channel", 4);
                startActivity(intent);
            }
        });
        LinearLayout channel5 = findViewById(R.id.channel_5);
        channel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChannelActivity.this, EditChannelActivity.class);
                intent.putExtra("channel", 5);
                startActivity(intent);
            }
        });
        LinearLayout channel6 = findViewById(R.id.channel_6);
        channel6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChannelActivity.this, EditChannelActivity.class);
                intent.putExtra("channel", 6);
                startActivity(intent);
            }
        });
        LinearLayout channel7 = findViewById(R.id.channel_7);
        channel7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChannelActivity.this, EditChannelActivity.class);
                intent.putExtra("channel", 7);
                startActivity(intent);
            }
        });
        LinearLayout channel8 = findViewById(R.id.channel_8);
        channel8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChannelActivity.this, EditChannelActivity.class);
                intent.putExtra("channel", 8);
                startActivity(intent);
            }
        });

        channelName1 = findViewById(R.id.channel_name_1);
        channelName2 = findViewById(R.id.channel_name_2);
        channelName3 = findViewById(R.id.channel_name_3);
        channelName4 = findViewById(R.id.channel_name_4);
        channelName5 = findViewById(R.id.channel_name_5);
        channelName6 = findViewById(R.id.channel_name_6);
        channelName7 = findViewById(R.id.channel_name_7);
        channelName8 = findViewById(R.id.channel_name_8);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChannelData[] channels = AppGlobal.getCurrentDevice().getChannels();
        channelName1.setText(channels[0].name);
        channelName2.setText(channels[1].name);
        channelName3.setText(channels[2].name);
        channelName4.setText(channels[3].name);
        channelName5.setText(channels[4].name);
        channelName6.setText(channels[5].name);
        channelName7.setText(channels[6].name);
        channelName8.setText(channels[7].name);
    }
}
