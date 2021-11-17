package com.xkglow.xkcommand.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.R;

import org.greenrobot.eventbus.EventBus;

public class PowerView extends FrameLayout {
    boolean clickable;
    ImageView powerBgImageView, powerBgImageView2, powerImageView;

    public PowerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.power_view, this);
        powerBgImageView = findViewById(R.id.background_image_unpressed);
        powerBgImageView2 = findViewById(R.id.background_image_pressed);
        powerImageView = findViewById(R.id.foreground_image);
    }

    public void setPowerClickable(boolean clickable) {
        this.clickable = clickable;
        if (clickable) {
            powerBgImageView.setVisibility(VISIBLE);
            powerBgImageView2.setVisibility(GONE);
            powerImageView.setAlpha(1f);
        } else {
            powerBgImageView.setVisibility(GONE);
            powerBgImageView2.setVisibility(VISIBLE);
            powerImageView.setAlpha(0.2f);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN && clickable) {
            setPowerClickable(false);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.POWER_OFF));
            return true;
        }
        return false;
    }
}
