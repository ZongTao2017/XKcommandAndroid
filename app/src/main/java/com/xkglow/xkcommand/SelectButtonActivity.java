package com.xkglow.xkcommand;

import static com.xkglow.xkcommand.Helper.Helper.ICON_RATIO;
import static com.xkglow.xkcommand.Helper.Helper.ICON_SIZE;
import static com.xkglow.xkcommand.Helper.Helper.PADDING;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.View.ControlButton;

public class SelectButtonActivity extends Activity {
    ControlButton button1, button2, button3, button4, button5, button6, button7, button8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_button);

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FrameLayout frameLayout = findViewById(R.id.buttons);
        boolean horizontal = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
        if (horizontal) {
            layoutParams.leftMargin = Helper.dpToPx(SelectButtonActivity.this, 70);
            layoutParams.rightMargin = Helper.dpToPx(SelectButtonActivity.this, 70);
            layoutParams.topMargin = Helper.dpToPx(SelectButtonActivity.this, 50);
            layoutParams.bottomMargin = Helper.dpToPx(SelectButtonActivity.this, 30);
        } else {
            layoutParams.leftMargin = Helper.dpToPx(SelectButtonActivity.this, 0);
            layoutParams.rightMargin = Helper.dpToPx(SelectButtonActivity.this, 0);
            layoutParams.topMargin = Helper.dpToPx(SelectButtonActivity.this, 70);
            layoutParams.bottomMargin = Helper.dpToPx(SelectButtonActivity.this, 70);
        }
        frameLayout.setLayoutParams(layoutParams);

        frameLayout.post(new Runnable() {
            @Override
            public void run() {
                int height = frameLayout.getHeight();
                int width = frameLayout.getWidth();
                boolean horizontal = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

                int paddingV = height * PADDING / (PADDING * 5 + ICON_SIZE * 4);
                int iconSize = height * ICON_SIZE / (PADDING * 5 + ICON_SIZE * 4);
                int paddingH = (width - iconSize * 2) / 3;

                if (horizontal) {
                    paddingV = height * PADDING / (PADDING * 3 + ICON_SIZE * 2);
                    iconSize = height * ICON_SIZE / (PADDING * 3 + ICON_SIZE * 2);
                    paddingH = (width - iconSize * 4) / 5;
                }

                int realIconSize = (int) (iconSize * ICON_RATIO);
                int resize = (iconSize - realIconSize) / 2;

                button1 = new ControlButton(SelectButtonActivity.this);
                button1.setEditButton();
                button1.setIconSize(iconSize);
                button1.setButtonData(AppGlobal.getCurrentDevice().getButton(1));

                button2 = new ControlButton(SelectButtonActivity.this);
                button2.setEditButton();
                button2.setIconSize(iconSize);
                button2.setButtonData(AppGlobal.getCurrentDevice().getButton(2));

                button3 = new ControlButton(SelectButtonActivity.this);
                button3.setEditButton();
                button3.setIconSize(iconSize);
                button3.setButtonData(AppGlobal.getCurrentDevice().getButton(3));

                button4 = new ControlButton(SelectButtonActivity.this);
                button4.setEditButton();
                button4.setIconSize(iconSize);
                button4.setButtonData(AppGlobal.getCurrentDevice().getButton(4));

                button5 = new ControlButton(SelectButtonActivity.this);
                button5.setEditButton();
                button5.setIconSize(iconSize);
                button5.setButtonData(AppGlobal.getCurrentDevice().getButton(5));

                button6 = new ControlButton(SelectButtonActivity.this);
                button6.setEditButton();
                button6.setIconSize(iconSize);
                button6.setButtonData(AppGlobal.getCurrentDevice().getButton(6));

                button7 = new ControlButton(SelectButtonActivity.this);
                button7.setEditButton();
                button7.setIconSize(iconSize);
                button7.setButtonData(AppGlobal.getCurrentDevice().getButton(7));

                button8 = new ControlButton(SelectButtonActivity.this);
                button8.setEditButton();
                button8.setIconSize(iconSize);
                button8.setButtonData(AppGlobal.getCurrentDevice().getButton(8));

                FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams1.leftMargin = paddingH + resize;
                layoutParams1.topMargin = paddingV + resize;
                if (horizontal) {
                    layoutParams1.leftMargin = resize + paddingH;
                    layoutParams1.topMargin = paddingV * 2 + iconSize + resize;
                }
                button1.setLayoutParams(layoutParams1);
                frameLayout.addView(button1);

                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams2.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams2.topMargin = paddingV + resize;
                if (horizontal) {
                    layoutParams2.leftMargin = resize + paddingH;
                    layoutParams2.topMargin = paddingV + resize;
                }
                button2.setLayoutParams(layoutParams2);
                frameLayout.addView(button2);

                FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams3.leftMargin = paddingH + resize;
                layoutParams3.topMargin = paddingV * 2 + iconSize + resize;
                if (horizontal) {
                    layoutParams3.leftMargin = paddingH * 2 + iconSize + resize;
                    layoutParams3.topMargin = paddingV * 2 + iconSize + resize;
                }
                button3.setLayoutParams(layoutParams3);
                frameLayout.addView(button3);

                FrameLayout.LayoutParams layoutParams4 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams4.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams4.topMargin = paddingV * 2 + iconSize + resize;
                if (horizontal) {
                    layoutParams4.leftMargin = paddingH * 2 + iconSize + resize;
                    layoutParams4.topMargin = paddingV + resize;
                }
                button4.setLayoutParams(layoutParams4);
                frameLayout.addView(button4);

                FrameLayout.LayoutParams layoutParams5 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams5.leftMargin = paddingH + resize;
                layoutParams5.topMargin = paddingV * 3 + iconSize * 2 + resize;
                if (horizontal) {
                    layoutParams5.leftMargin = paddingH * 3 + iconSize * 2 + resize;
                    layoutParams5.topMargin = paddingV * 2 + iconSize + resize;
                }
                button5.setLayoutParams(layoutParams5);
                frameLayout.addView(button5);

                FrameLayout.LayoutParams layoutParams6 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams6.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams6.topMargin = paddingV * 3 + iconSize * 2 + resize;
                if (horizontal) {
                    layoutParams6.leftMargin = paddingH * 3 + iconSize * 2 + resize;
                    layoutParams6.topMargin = paddingV + resize;
                }
                button6.setLayoutParams(layoutParams6);
                frameLayout.addView(button6);

                FrameLayout.LayoutParams layoutParams7 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams7.leftMargin = paddingH + resize;
                layoutParams7.topMargin = paddingV * 4 + iconSize * 3 + resize;
                if (horizontal) {
                    layoutParams7.leftMargin = paddingH * 4 + iconSize * 3 + resize;
                    layoutParams7.topMargin = paddingV * 2 + iconSize + resize;
                }
                button7.setLayoutParams(layoutParams7);
                frameLayout.addView(button7);

                FrameLayout.LayoutParams layoutParams8 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams8.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams8.topMargin = paddingV * 4 + iconSize * 3 + resize;
                if (horizontal) {
                    layoutParams8.leftMargin = paddingH * 4 + iconSize * 3 + resize;
                    layoutParams8.topMargin = paddingV + resize;
                }
                button8.setLayoutParams(layoutParams8);
                frameLayout.addView(button8);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (button1 != null) {
            button1.setButtonData(AppGlobal.getCurrentDevice().getButton(1));
        }
        if (button2 != null) {
            button2.setButtonData(AppGlobal.getCurrentDevice().getButton(2));
        }
        if (button3 != null) {
            button3.setButtonData(AppGlobal.getCurrentDevice().getButton(3));
        }
        if (button4 != null) {
            button4.setButtonData(AppGlobal.getCurrentDevice().getButton(4));
        }
        if (button5 != null) {
            button5.setButtonData(AppGlobal.getCurrentDevice().getButton(5));
        }
        if (button6 != null) {
            button6.setButtonData(AppGlobal.getCurrentDevice().getButton(6));
        }
        if (button7 != null) {
            button7.setButtonData(AppGlobal.getCurrentDevice().getButton(7));
        }
        if (button8 != null) {
            button8.setButtonData(AppGlobal.getCurrentDevice().getButton(8));
        }
    }
}
