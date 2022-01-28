package com.xkglow.xkcommand.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.xkglow.xkcommand.EditButtonActivity;
import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.DeviceData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.R;

import org.greenrobot.eventbus.EventBus;

public class ControlButton extends FrameLayout {
    private Context context;
    private DeviceData deviceData;
    private ButtonData buttonData;
    private ImageView imagePressed, imageUnpressed, imageIllumination;
    private ImageView icon, image;
    private TextView textView;
    private boolean released;
    private boolean editButton;
    private FrameLayout warning;
    private boolean error;
    private boolean isTouching;

    public ControlButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        inflate(context, R.layout.button_view, this);

        released = false;
        editButton = false;
        error = false;
        isTouching = false;

        imagePressed = findViewById(R.id.background_image_pressed);
        imageUnpressed = findViewById(R.id.background_image_unpressed);
        imageIllumination = findViewById(R.id.illumination_image);

        textView = findViewById(R.id.text);
        Typeface face = ResourcesCompat.getFont(context, R.font.impact);;
        textView.setTypeface(face);

        icon = findViewById(R.id.foreground_icon);
        image = findViewById(R.id.foreground_image);
        image.setClipToOutline(true);

        warning = findViewById(R.id.warning);
    }

    public void setError(boolean error) {
        if (this.error != error) {
            this.error = error;
            if (error) {
                warning.setVisibility(VISIBLE);
            } else {
                warning.setVisibility(GONE);
            }
            warning.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                            .setCancelable(true)
                            .setMessage(AppGlobal.getCurrentDevice().getChannel(1).name + " exceeded max output current and has been shut off. Please turn off the system and check.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    alertDialog.show();
                }
            });
        }
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

        FrameLayout.LayoutParams layoutParams3 = (LayoutParams) warning.getLayoutParams();
        layoutParams3.topMargin = (int) (iconSize * 0.25);
        layoutParams3.rightMargin = (int) (iconSize * 0.25);
        warning.setLayoutParams(layoutParams3);
    }

    public void setData(DeviceData deviceData, ButtonData buttonData) {
        textView.setVisibility(GONE);
        icon.setVisibility(GONE);
        image.setVisibility(GONE);
        this.deviceData = deviceData;
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
            if (error) {
                imageIllumination.setColorFilter(AppGlobal.getCurrentDevice().getSystemData().buttonWarningColor);
            } else {
                imageIllumination.setColorFilter(AppGlobal.getCurrentDevice().getSystemData().buttonColor);
            }
        } else {
            imageUnpressed.setVisibility(View.VISIBLE);
            imagePressed.setVisibility(View.GONE);
            imageIllumination.setVisibility(View.GONE);
        }
    }

    public void turnOff() {
        buttonData.isPressed = false;
        imageIllumination.setVisibility(View.GONE);
    }

    public void turnOn() {
        buttonData.isPressed = true;
        imageIllumination.setVisibility(View.VISIBLE);
        if (error) {
            imageIllumination.setColorFilter(AppGlobal.getCurrentDevice().getSystemData().buttonWarningColor);
        } else {
            imageIllumination.setColorFilter(AppGlobal.getCurrentDevice().getSystemData().buttonColor);
        }
    }

    public void press() {
        if (!isTouching) {
            imageUnpressed.setVisibility(View.GONE);
            imagePressed.setVisibility(View.VISIBLE);
        }
    }

    public void release() {
        if (!isTouching) {
            imageUnpressed.setVisibility(View.VISIBLE);
            imagePressed.setVisibility(View.GONE);
        }
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
            isTouching = true;
            float currentVolt = (deviceData.deviceInfoBytes[4] & 0xff) * 0.1f;
            float maxVolt = (deviceData.userSettingsBytes[0] & 0xff) * 0.2f;
            if (currentVolt < maxVolt && Math.abs(currentVolt - maxVolt) > 0.1f) {
                final AlertDialog alertDialog = new AlertDialog.Builder(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setCancelable(true)
                        .setMessage("Current volt is lower than auto cutoff volt.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
                return false;
            }
            imageUnpressed.setVisibility(View.GONE);
            imagePressed.setVisibility(View.VISIBLE);
            Helper.vibrate(context);
            if (released) {
                buttonData.isPressed = true;
                imageIllumination.setVisibility(View.VISIBLE);
                if (error) {
                    imageIllumination.setColorFilter(AppGlobal.getCurrentDevice().getSystemData().buttonWarningColor);
                } else {
                    imageIllumination.setColorFilter(AppGlobal.getCurrentDevice().getSystemData().buttonColor);
                }
                AppGlobal.writeButtonOnOff(buttonData.id, true);
            } else {
                if (!buttonData.isPressed) {
                    buttonData.isPressed = true;
                    imageIllumination.setVisibility(View.VISIBLE);
                    if (error) {
                        imageIllumination.setColorFilter(AppGlobal.getCurrentDevice().getSystemData().buttonWarningColor);
                    } else {
                        imageIllumination.setColorFilter(AppGlobal.getCurrentDevice().getSystemData().buttonColor);
                    }
                    AppGlobal.writeButtonOnOff(buttonData.id, true);
                } else {
                    buttonData.isPressed = false;
                    imageIllumination.setVisibility(View.GONE);
                    AppGlobal.writeButtonOnOff(buttonData.id, false);
                }
            }
            deviceData.setButton(buttonData);
            if (!released) EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.TURN_ON_OFF, deviceData.address));
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            imageUnpressed.setVisibility(View.VISIBLE);
            imagePressed.setVisibility(View.GONE);
            if (released) {
                buttonData.isPressed = false;
                imageIllumination.setVisibility(View.GONE);
                deviceData.setButton(buttonData);
                Helper.vibrate(context);
                AppGlobal.writeButtonOnOff(buttonData.id, false);
            }
            isTouching = false;
        }
        return true;
    }

    public void setEditButton() {
        editButton = true;
    }
}
