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

import com.bumptech.glide.Glide;
import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.View.ActionView;
import com.xkglow.xkcommand.View.SwitchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class EditButtonActivity extends Activity {
    ButtonData buttonData;
    TextView noChannelSelectedTextView;
    LinearLayout channel1, channel2, channel3, channel4, channel5, channel6, channel7, channel8, syncAll;
    ImageView radioButtonSelectIconImage, radioButtonSelectImageImage, radioButtonSelectTextImage, radioButtonSelectNAImage;
    ImageView channelImage1, channelImage2, channelImage3, channelImage4, channelImage5, channelImage6, channelImage7, channelImage8;
    LinearLayout channelLayout, actionLayout;
    SwitchView syncSwitch, momentarySwitch;

    ImageView buttonIcon, buttonImage;
    TextView buttonText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_button);
        buttonData = (ButtonData) getIntent().getSerializableExtra("button");

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGlobal.writeButtonData(buttonData.id);
                finish();
            }
        });

        TextView buttonId = findViewById(R.id.button_id);
        buttonId.setText("Button " + buttonData.id);

        radioButtonSelectIconImage = findViewById(R.id.radio_button_icon);
        radioButtonSelectImageImage = findViewById(R.id.radio_button_image);
        radioButtonSelectTextImage = findViewById(R.id.radio_button_text);
        radioButtonSelectNAImage = findViewById(R.id.radio_button_na);
        momentarySwitch = findViewById(R.id.switch_momentary);
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
        channelLayout = findViewById(R.id.channel_layout);
        actionLayout = findViewById(R.id.action_layout);
        noChannelSelectedTextView = findViewById(R.id.no_channel_selected);
        syncAll = findViewById(R.id.sync_all_channels_layout);

        buttonIcon = findViewById(R.id.button_icon);
        buttonImage = findViewById(R.id.button_image);
        buttonText = findViewById(R.id.button_text);

        syncSwitch.setOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onSwitchOn(boolean showMessage) {
                buttonData.sync = true;
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }

            @Override
            public void onSwitchOff(boolean showMessage) {
                buttonData.sync = false;
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        momentarySwitch.setOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onSwitchOn(boolean showMessage) {
                buttonData.momentary = true;
                AppGlobal.getCurrentDevice().setButton(buttonData);
            }

            @Override
            public void onSwitchOff(boolean showMessage) {
                buttonData.momentary = false;
                AppGlobal.getCurrentDevice().setButton(buttonData);
            }
        });

        View radioButtonIconSelect = findViewById(R.id.button_icon_select);
        radioButtonIconSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonData.iconResourceId == 0) {
                    Intent intent = new Intent(EditButtonActivity.this, EditButtonIconActivity.class);
                    intent.putExtra("button", buttonData);
                    startActivity(intent);
                } else {
                    buttonData.type = 2;
                    AppGlobal.getCurrentDevice().setButton(buttonData);
                }
                setButtons();
            }
        });

        View radioButtonImageSelect = findViewById(R.id.button_image_select);
        radioButtonImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonData.imagePath == null) {
                    Intent intent = new Intent(EditButtonActivity.this, EditButtonImageActivity.class);
                    intent.putExtra("button", buttonData);
                    startActivity(intent);
                } else {
                    buttonData.type = 3;
                    AppGlobal.getCurrentDevice().setButton(buttonData);
                }
                setButtons();
            }
        });

        View radioButtonTextSelect = findViewById(R.id.button_text_select);
        radioButtonTextSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonData.text == null) {
                    Intent intent = new Intent(EditButtonActivity.this, EditNameActivity.class);
                    intent.putExtra("button", buttonData);
                    startActivity(intent);
                } else {
                    buttonData.type = 1;
                    AppGlobal.getCurrentDevice().setButton(buttonData);
                }
                setButtons();
            }
        });

        View radioButtonNASelect = findViewById(R.id.button_na_select);
        radioButtonNASelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.type = 0;
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setButtons();
            }
        });

        View radioButtonIconEdit = findViewById(R.id.button_icon_edit);
        radioButtonIconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditButtonActivity.this, EditButtonIconActivity.class);
                intent.putExtra("button", buttonData);
                startActivity(intent);
            }
        });

        View radioButtonImageEdit = findViewById(R.id.button_image_edit);
        radioButtonImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditButtonActivity.this, EditButtonImageActivity.class);
                intent.putExtra("button", buttonData);
                startActivity(intent);
            }
        });

        View radioButtonTextEdit = findViewById(R.id.button_text_edit);
        radioButtonTextEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditButtonActivity.this, EditNameActivity.class);
                intent.putExtra("button", buttonData);
                startActivity(intent);
            }
        });

        channel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.channels[0] = !buttonData.channels[0];
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        channel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.channels[1] = !buttonData.channels[1];
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        channel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.channels[2] = !buttonData.channels[2];
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        channel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.channels[3] = !buttonData.channels[3];
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        channel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.channels[4] = !buttonData.channels[4];
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        channel6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.channels[5] = !buttonData.channels[5];
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        channel7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.channels[6] = !buttonData.channels[6];
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        channel8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonData.channels[7] = !buttonData.channels[7];
                AppGlobal.getCurrentDevice().setButton(buttonData);
                setChannels();
            }
        });

        setChannelNames();
    }

    @Override
    protected void onResume() {
        super.onResume();
        momentarySwitch.post(new Runnable() {
            @Override
            public void run() {
                setMomentarySwitch();
            }
        });
        syncSwitch.post(new Runnable() {
            @Override
            public void run() {
                setSyncSwitch();
            }
        });
        buttonData = AppGlobal.getCurrentDevice().getButton(buttonData.id);
        if (buttonData.iconResourceId != 0) {
            buttonIcon.setImageResource(buttonData.iconResourceId);
        }
        if (buttonData.imagePath != null) {
            Glide.with(EditButtonActivity.this).load(buttonData.imagePath).into(buttonImage);
        }
        buttonText.setText(buttonData.text);
        setButtons();
    }

    private void setButtons() {
        radioButtonSelectIconImage.setImageResource(R.drawable.radio_unselected);
        radioButtonSelectImageImage.setImageResource(R.drawable.radio_unselected);
        radioButtonSelectTextImage.setImageResource(R.drawable.radio_unselected);
        radioButtonSelectNAImage.setImageResource(R.drawable.radio_unselected);
        switch (buttonData.type) {
            case 0:
                radioButtonSelectNAImage.setImageResource(R.drawable.radio_selected);
                break;
            case 1:
                radioButtonSelectTextImage.setImageResource(R.drawable.radio_selected);
                break;
            case 2:
                radioButtonSelectIconImage.setImageResource(R.drawable.radio_selected);
                break;
            case 3:
                radioButtonSelectImageImage.setImageResource(R.drawable.radio_selected);
                break;
        }
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

    private void setMomentarySwitch() {
        if (buttonData.momentary) {
            momentarySwitch.switchOn(false, false);
        } else {
            momentarySwitch.switchOff(false, false);
        }
    }

    private void setSyncSwitch() {
        if (buttonData.sync) {
            syncSwitch.switchOn(false, false);
        } else {
            syncSwitch.switchOff(false, false);
        }
        setChannels();
    }

    private void setChannels() {
        actionLayout.removeAllViews();
        boolean flag = false;
        setChannel(channelImage1, buttonData.channels[0]);
        if (buttonData.channels[0]) {
            flag = true;
            if (!buttonData.sync) {
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(1).name);
                actionView.setButtonData(buttonData, new int[]{ 0 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage2, buttonData.channels[1]);
        if (buttonData.channels[1]) {
            flag = true;
            if (!buttonData.sync) {
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(2).name);
                actionView.setButtonData(buttonData, new int[]{ 1 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage3, buttonData.channels[2]);
        if (buttonData.channels[2]) {
            flag = true;
            if (!buttonData.sync) {
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(3).name);
                actionView.setButtonData(buttonData, new int[]{ 2 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage4, buttonData.channels[3]);
        if (buttonData.channels[3]) {
            flag = true;
            if (!buttonData.sync) {
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(4).name);
                actionView.setButtonData(buttonData, new int[]{ 3 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage5, buttonData.channels[4]);
        if (buttonData.channels[4]) {
            flag = true;
            if (!buttonData.sync) {
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(5).name);
                actionView.setButtonData(buttonData, new int[]{ 4 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage6, buttonData.channels[5]);
        if (buttonData.channels[5]) {
            flag = true;
            if (!buttonData.sync) {
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(6).name);
                actionView.setButtonData(buttonData, new int[]{ 5 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage7, buttonData.channels[6]);
        if (buttonData.channels[6]) {
            flag = true;
            if (!buttonData.sync) {
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(7).name);
                actionView.setButtonData(buttonData, new int[]{ 6 });
                actionLayout.addView(actionView);
            }
        }
        setChannel(channelImage8, buttonData.channels[7]);
        if (buttonData.channels[7]) {
            flag = true;
            if (!buttonData.sync) {
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - " + AppGlobal.getCurrentDevice().getChannel(8).name);
                actionView.setButtonData(buttonData, new int[]{ 7 });
                actionLayout.addView(actionView);
            }
        }
        if (flag) {
            syncAll.setVisibility(View.VISIBLE);
            noChannelSelectedTextView.setVisibility(View.GONE);
            actionLayout.setVisibility(View.VISIBLE);
            if (buttonData.sync) {
                int number = 0;
                for (boolean channel : buttonData.channels) {
                    if (channel) number++;
                }
                int[] actions = new int[number];
                int n = 0;
                for (int i = 0; i < buttonData.channels.length; i++) {
                    if (buttonData.channels[i]) {
                        actions[n++] = i;
                    }
                }
                ActionView actionView = new ActionView(EditButtonActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = Helper.dpToPx(EditButtonActivity.this, 30);
                actionView.setLayoutParams(layoutParams);
                actionView.setActionName("ACTION - ALL CHANNELS");
                actionView.setButtonData(buttonData, actions);
                actionLayout.addView(actionView);
            }
        } else {
            syncAll.setVisibility(View.GONE);
            noChannelSelectedTextView.setVisibility(View.VISIBLE);
            actionLayout.setVisibility(View.GONE);
        }
    }

    private void setChannel(ImageView imageView, boolean select) {
        if (select) {
            imageView.setImageResource(R.drawable.check_selected);
        } else {
            imageView.setImageResource(R.drawable.check_unselected);
        }
    }
}
