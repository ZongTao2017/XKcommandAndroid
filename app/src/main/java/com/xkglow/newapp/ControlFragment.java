package com.xkglow.newapp;

import static com.xkglow.newapp.Helper.Helper.ICON_RATIO;
import static com.xkglow.newapp.Helper.Helper.ICON_SIZE;
import static com.xkglow.newapp.Helper.Helper.PADDING;
import static com.xkglow.newapp.Helper.Helper.dpToPx;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabWidget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ControlFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        final FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
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
                    paddingH = (width - iconSize * 4) / 4;
                }

                int realIconSize = (int) (iconSize * ICON_RATIO);
                int resize = (iconSize - realIconSize) / 2;

                FrameLayout button1 = new FrameLayout(getContext());
                button1.setClipChildren(false);
                button1.setClipToPadding(false);
                FrameLayout button2 = new FrameLayout(getContext());
                FrameLayout button3 = new FrameLayout(getContext());
                FrameLayout button4 = new FrameLayout(getContext());
                FrameLayout button5 = new FrameLayout(getContext());
                FrameLayout button6 = new FrameLayout(getContext());
                FrameLayout button7 = new FrameLayout(getContext());
                FrameLayout button8 = new FrameLayout(getContext());

                FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams1.leftMargin = paddingH + resize;
                layoutParams1.topMargin = paddingV + resize;
                if (horizontal) {
                    layoutParams1.leftMargin = resize;
                }
                button1.setLayoutParams(layoutParams1);
                frameLayout.addView(button1);

                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams2.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams2.topMargin = paddingV + resize;
                if (horizontal) {
                    layoutParams2.leftMargin = paddingH + iconSize + resize;
                }
                button2.setLayoutParams(layoutParams2);
                frameLayout.addView(button2);

                FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams3.leftMargin = paddingH + resize;
                layoutParams3.topMargin = paddingV * 2 + iconSize + resize;
                if (horizontal) {
                    layoutParams3.leftMargin = paddingH * 2 + iconSize * 2 + resize;
                    layoutParams3.topMargin = paddingV + resize;
                }
                button3.setLayoutParams(layoutParams3);
                frameLayout.addView(button3);

                FrameLayout.LayoutParams layoutParams4 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams4.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams4.topMargin = paddingV * 2 + iconSize + resize;
                if (horizontal) {
                    layoutParams4.leftMargin = paddingH * 3 + iconSize * 3 + resize;
                    layoutParams4.topMargin = paddingV + resize;
                }
                button4.setLayoutParams(layoutParams4);
                frameLayout.addView(button4);

                FrameLayout.LayoutParams layoutParams5 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams5.leftMargin = paddingH + resize;
                layoutParams5.topMargin = paddingV * 3 + iconSize * 2 + resize;
                if (horizontal) {
                    layoutParams5.leftMargin = resize;
                    layoutParams5.topMargin = paddingV * 2 + iconSize + resize;
                }
                button5.setLayoutParams(layoutParams5);
                frameLayout.addView(button5);

                FrameLayout.LayoutParams layoutParams6 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams6.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams6.topMargin = paddingV * 3 + iconSize * 2 + resize;
                if (horizontal) {
                    layoutParams6.leftMargin = paddingH + iconSize + resize;
                    layoutParams6.topMargin = paddingV * 2 + iconSize + resize;
                }
                button6.setLayoutParams(layoutParams6);
                frameLayout.addView(button6);

                FrameLayout.LayoutParams layoutParams7 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams7.leftMargin = paddingH + resize;
                layoutParams7.topMargin = paddingV * 4 + iconSize * 3 + resize;
                if (horizontal) {
                    layoutParams7.leftMargin = paddingH * 2 + iconSize * 2 + resize;
                    layoutParams7.topMargin = paddingV * 2 + iconSize + resize;
                }
                button7.setLayoutParams(layoutParams7);
                frameLayout.addView(button7);

                FrameLayout.LayoutParams layoutParams8 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams8.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams8.topMargin = paddingV * 4 + iconSize * 3 + resize;
                if (horizontal) {
                    layoutParams8.leftMargin = paddingH * 3 + iconSize * 3 + resize;
                    layoutParams8.topMargin = paddingV * 2 + iconSize + resize;
                }
                button8.setLayoutParams(layoutParams8);
                frameLayout.addView(button8);

                FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ImageView image1 = new ImageView(getContext());
                ImageView image2 = new ImageView(getContext());
                ImageView image3 = new ImageView(getContext());
                ImageView image4 = new ImageView(getContext());
                ImageView image5 = new ImageView(getContext());
                ImageView image6 = new ImageView(getContext());
                ImageView image7 = new ImageView(getContext());
                ImageView image8 = new ImageView(getContext());
                image1.setImageResource(R.drawable.button_unpressed);
                image2.setImageResource(R.drawable.button_unpressed);
                image3.setImageResource(R.drawable.button_unpressed);
                image4.setImageResource(R.drawable.button_unpressed);
                image5.setImageResource(R.drawable.button_unpressed);
                image6.setImageResource(R.drawable.button_unpressed);
                image7.setImageResource(R.drawable.button_unpressed);
                image8.setImageResource(R.drawable.button_unpressed);
                image1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image2.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image3.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image4.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image5.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image6.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image7.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image8.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image1.setLayoutParams(imageLayoutParams);
                image2.setLayoutParams(imageLayoutParams);
                image3.setLayoutParams(imageLayoutParams);
                image4.setLayoutParams(imageLayoutParams);
                image5.setLayoutParams(imageLayoutParams);
                image6.setLayoutParams(imageLayoutParams);
                image7.setLayoutParams(imageLayoutParams);
                image8.setLayoutParams(imageLayoutParams);
                button1.addView(image1);
                button2.addView(image2);
                button3.addView(image3);
                button4.addView(image4);
                button5.addView(image5);
                button6.addView(image6);
                button7.addView(image7);
                button8.addView(image8);

                FrameLayout power = new FrameLayout(getContext());
                int size = dpToPx(getContext(), 60);
                FrameLayout.LayoutParams powerLayoutParams = new FrameLayout.LayoutParams(size, size);
                if (horizontal) {
                    powerLayoutParams.leftMargin = (int) (iconSize * 2 + paddingH * 1.5f - size / 2);
                    powerLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                } else {
                    powerLayoutParams.gravity = Gravity.CENTER;
                }
                power.setLayoutParams(powerLayoutParams);
                frameLayout.addView(power);

                ImageView powerBgImageView = new ImageView(getContext());
                powerBgImageView.setLayoutParams(new FrameLayout.LayoutParams(size, size));
                powerBgImageView.setImageResource(R.drawable.power_base_unpressed);
                powerBgImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                power.addView(powerBgImageView);

                ImageView powerImageView = new ImageView(getContext());
                FrameLayout.LayoutParams powerImageLayoutParams = new FrameLayout.LayoutParams(size / 2, size / 2);
                powerImageLayoutParams.gravity = Gravity.CENTER;
                powerImageView.setLayoutParams(powerImageLayoutParams);
                powerImageView.setImageResource(R.drawable.power);
                powerImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                power.addView(powerImageView);
            }
        });


        return view;
    }
}
