package com.xkglow.xkcommand.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.xkglow.xkcommand.EditButtonActivity;
import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.R;

public class ControlButton extends FrameLayout {
    private Context context;
    private ButtonData buttonData;
    private ImageView imagePressed, imageUnpressed, imageIllumination;
    private ImageView icon, image;
    private TextView textView;
    private boolean released;
    private boolean pressed;
    private boolean powerOn;
    private boolean editButton;

    public ControlButton(@NonNull Context context) {
        super(context);

        this.context = context;
        inflate(context, R.layout.button_view, this);

        released = false;
        pressed = false;
        powerOn = true;
        editButton = false;

        imagePressed = findViewById(R.id.background_image_pressed);
        imageUnpressed = findViewById(R.id.background_image_unpressed);
        imageIllumination = findViewById(R.id.illumination_image);
        textView = findViewById(R.id.text);

        icon = findViewById(R.id.foreground_icon);
        image = findViewById(R.id.foreground_image);
        image.setClipToOutline(true);
    }

    public void setIconSize(int iconSize) {
        FrameLayout foregroundLayout = findViewById(R.id.foreground_layout);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) foregroundLayout.getLayoutParams();
        layoutParams.width = iconSize;
        layoutParams.height = iconSize;
        foregroundLayout.setLayoutParams(layoutParams);
    }

    public void setButtonData(ButtonData buttonData) {
        textView.setVisibility(GONE);
        icon.setVisibility(GONE);
        image.setVisibility(GONE);
        this.buttonData = buttonData;
        this.released = buttonData.momentary;
        if (buttonData.type == 0) {
            textView.setVisibility(VISIBLE);
            textView.setText(buttonData.id + "");
        } else if (buttonData.type == 1) {
            textView.setVisibility(VISIBLE);
            textView.setText(buttonData.text);
        } else if (buttonData.type == 2) {
            icon.setVisibility(VISIBLE);
            icon.setImageResource(buttonData.iconResourceId);
        } else {
            image.setVisibility(VISIBLE);
            Glide.with(context).load(buttonData.imagePath).into(image);
        }
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
        this.pressed = false;
        imageUnpressed.setVisibility(View.VISIBLE);
        imagePressed.setVisibility(View.GONE);
        imageIllumination.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!powerOn) return false;
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            if (editButton) {
                Intent intent = new Intent(getContext(), EditButtonActivity.class);
                intent.putExtra("button", buttonData);
                getContext().startActivity(intent);
                return false;
            }
            imageUnpressed.setVisibility(View.GONE);
            imagePressed.setVisibility(View.VISIBLE);
            if (released) {
                imageIllumination.setVisibility(View.VISIBLE);
            } else {
                if (!pressed) {
                    imageIllumination.setVisibility(View.VISIBLE);
                } else {
                    imageIllumination.setVisibility(View.GONE);
                }
                pressed = !pressed;
            }
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            imageUnpressed.setVisibility(View.VISIBLE);
            imagePressed.setVisibility(View.GONE);
            if (released) {
                imageIllumination.setVisibility(View.GONE);
            }
        }
        return true;
    }

    public void setEditButton() {
        editButton = true;
    }
}
