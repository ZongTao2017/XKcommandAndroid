package com.xkglow.xkcommand.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.xkglow.xkcommand.EditButtonActivity;
import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.R;

import org.greenrobot.eventbus.EventBus;

public class ControlButton extends FrameLayout {
    private Context context;
    private ButtonData buttonData;
    private ImageView imagePressed, imageUnpressed, imageIllumination;
    private ImageView icon, image;
    private TextView textView;
    private boolean released;
    private boolean editButton;
    private boolean enabled;

    public ControlButton(@NonNull Context context) {
        super(context);

        this.context = context;
        inflate(context, R.layout.button_view, this);

        released = false;
        editButton = false;

        imagePressed = findViewById(R.id.background_image_pressed);
        imageUnpressed = findViewById(R.id.background_image_unpressed);
        imageIllumination = findViewById(R.id.illumination_image);

        textView = findViewById(R.id.text);
        Typeface face = ResourcesCompat.getFont(context, R.font.impact);;
        textView.setTypeface(face);

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

        FrameLayout.LayoutParams layoutParams1 = (LayoutParams) icon.getLayoutParams();
        layoutParams1.width = (int) (iconSize * 0.6);
        layoutParams1.height = (int) (iconSize * 0.6);
        icon.setLayoutParams(layoutParams1);

        FrameLayout.LayoutParams layoutParams2 = (LayoutParams) image.getLayoutParams();
        layoutParams2.width = (int) (iconSize * 0.75);
        layoutParams2.height = (int) (iconSize * 0.75);
        image.setLayoutParams(layoutParams2);
    }

    public void setButtonData(ButtonData buttonData) {
        textView.setVisibility(GONE);
        icon.setVisibility(GONE);
        image.setVisibility(GONE);
        this.buttonData = buttonData;
        this.released = buttonData.momentary;
        if (buttonData.type == 0) {

        } else if (buttonData.type == 1) {
            textView.setVisibility(VISIBLE);
            if (buttonData.text == null) {
                textView.setText("Button " + buttonData.id);
            } else {
                textView.setText(buttonData.text);
            }
        } else if (buttonData.type == 2) {
            icon.setVisibility(VISIBLE);
            icon.setImageResource(buttonData.iconResourceId);
        } else {
            image.setVisibility(VISIBLE);
            Glide.with(context).load(buttonData.imagePath).into(image);
        }
        if (buttonData.isPressed && !editButton) {
            imageUnpressed.setVisibility(View.GONE);
            imagePressed.setVisibility(View.VISIBLE);
            imageIllumination.setVisibility(View.VISIBLE);
            imageIllumination.setColorFilter(AppGlobal.getSystemData().buttonColor);
        } else {
            imageUnpressed.setVisibility(View.VISIBLE);
            imagePressed.setVisibility(View.GONE);
            imageIllumination.setVisibility(View.GONE);
        }
    }

    public void turnOff() {
        buttonData.isPressed = false;
        imageUnpressed.setVisibility(View.VISIBLE);
        imagePressed.setVisibility(View.GONE);
        imageIllumination.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                buttonData.isPressed = true;
                imageIllumination.setVisibility(View.VISIBLE);
                imageIllumination.setColorFilter(AppGlobal.getSystemData().buttonColor);
            } else {
                if (!buttonData.isPressed) {
                    buttonData.isPressed = true;
                    imageIllumination.setVisibility(View.VISIBLE);
                    imageIllumination.setColorFilter(AppGlobal.getSystemData().buttonColor);
                } else {
                    buttonData.isPressed = false;
                    imageIllumination.setVisibility(View.GONE);
                }
            }
            AppGlobal.setButton(buttonData);
            if (!released) EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.TURN_ON_OFF));
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            imageUnpressed.setVisibility(View.VISIBLE);
            imagePressed.setVisibility(View.GONE);
            if (released) {
                buttonData.isPressed = false;
                imageIllumination.setVisibility(View.GONE);
                AppGlobal.setButton(buttonData);
            }
        }
        return true;
    }

    public void setEditButton() {
        editButton = true;
    }
}
