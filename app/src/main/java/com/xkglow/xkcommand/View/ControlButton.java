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

import com.xkglow.xkcommand.EditButtonActivity;
import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.R;

public class ControlButton extends FrameLayout {
    private int id;
    private ImageView imagePressed;
    private ImageView imageUnpressed;
    private ImageView imageIllumination;
    private TextView textView;
    private boolean released;
    private boolean pressed;
    private boolean powerOn;
    private boolean editButton;

    public ControlButton(@NonNull Context context) {
        super(context);

        released = false;
        pressed = false;
        powerOn = true;
        editButton = false;

        FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imagePressed = new ImageView(context);
        imageUnpressed = new ImageView(context);
        imageIllumination = new ImageView(context);
        textView = new TextView(context);

        imageUnpressed.setImageResource(R.drawable.button_unpressed);
        imageUnpressed.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageUnpressed.setLayoutParams(imageLayoutParams);
        addView(imageUnpressed);

        imagePressed.setImageResource(R.drawable.button_pressed);
        imagePressed.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imagePressed.setLayoutParams(imageLayoutParams);
        addView(imagePressed);
        imagePressed.setVisibility(GONE);

        imageIllumination.setImageResource(R.drawable.button_illumination);
        imageIllumination.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageIllumination.setLayoutParams(imageLayoutParams);
        addView(imageIllumination);
        imageIllumination.setVisibility(GONE);

        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textLayoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(textLayoutParams);
        textView.setTextColor(0xffffffff);
        textView.setTextSize(Helper.dpToPx(context, 10));
        textView.setGravity(Gravity.CENTER);
        addView(textView);
    }

    public void setButtonId(int id) {
        this.id = id;
        textView.setText(id + "");
    }

    public void setReleased(boolean released) {
        this.released = released;
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
                ButtonData buttonData = AppGlobal.getButton(id);
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