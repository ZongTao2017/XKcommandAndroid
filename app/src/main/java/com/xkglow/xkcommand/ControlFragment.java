package com.xkglow.xkcommand;

import static com.xkglow.xkcommand.Helper.Helper.ICON_RATIO;
import static com.xkglow.xkcommand.Helper.Helper.ICON_SIZE;
import static com.xkglow.xkcommand.Helper.Helper.PADDING;
import static com.xkglow.xkcommand.Helper.Helper.STATUS_RATIO;
import static com.xkglow.xkcommand.Helper.Helper.dpToPx;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.View.ControlButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ControlFragment extends Fragment {
    FrameLayout frameLayout;
    ControlButton button1, button2, button3, button4, button5, button6, button7, button8;
    ImageView powerBgImageView, powerBgImageView2, powerImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        frameLayout = view.findViewById(R.id.frame_layout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int padding = dpToPx(getContext(), 20);

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

                button1 = new ControlButton(getContext());
                button1.setIconSize(iconSize);
                button1.setButtonData(AppGlobal.getButton(1));

                button2 = new ControlButton(getContext());
                button2.setIconSize(iconSize);
                button2.setButtonData(AppGlobal.getButton(2));

                button3 = new ControlButton(getContext());
                button3.setIconSize(iconSize);
                button3.setButtonData(AppGlobal.getButton(3));

                button4 = new ControlButton(getContext());
                button4.setIconSize(iconSize);
                button4.setButtonData(AppGlobal.getButton(4));

                button5 = new ControlButton(getContext());
                button5.setIconSize(iconSize);
                button5.setButtonData(AppGlobal.getButton(5));

                button6 = new ControlButton(getContext());
                button6.setIconSize(iconSize);
                button6.setButtonData(AppGlobal.getButton(6));

                button7 = new ControlButton(getContext());
                button7.setIconSize(iconSize);
                button7.setButtonData(AppGlobal.getButton(7));

                button8 = new ControlButton(getContext());
                button8.setIconSize(iconSize);
                button8.setButtonData(AppGlobal.getButton(8));

                FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams1.leftMargin = paddingH + resize;
                layoutParams1.topMargin = paddingV + resize;
                if (horizontal) {
                    layoutParams1.leftMargin = resize + padding;
                    layoutParams1.topMargin = paddingV * 2 + iconSize + resize;
                }
                button1.setLayoutParams(layoutParams1);
                frameLayout.addView(button1);

                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams2.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams2.topMargin = paddingV + resize;
                if (horizontal) {
                    layoutParams2.leftMargin = resize + padding;
                    layoutParams2.topMargin = paddingV + resize;
                }
                button2.setLayoutParams(layoutParams2);
                frameLayout.addView(button2);

                FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams3.leftMargin = paddingH + resize;
                layoutParams3.topMargin = paddingV * 2 + iconSize + resize;
                if (horizontal) {
                    layoutParams3.leftMargin = paddingH + iconSize + resize + padding;
                    layoutParams3.topMargin = paddingV * 2 + iconSize + resize;
                }
                button3.setLayoutParams(layoutParams3);
                frameLayout.addView(button3);

                FrameLayout.LayoutParams layoutParams4 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams4.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams4.topMargin = paddingV * 2 + iconSize + resize;
                if (horizontal) {
                    layoutParams4.leftMargin = paddingH + iconSize + resize + padding;
                    layoutParams4.topMargin = paddingV + resize;
                }
                button4.setLayoutParams(layoutParams4);
                frameLayout.addView(button4);

                FrameLayout.LayoutParams layoutParams5 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams5.leftMargin = paddingH + resize;
                layoutParams5.topMargin = paddingV * 3 + iconSize * 2 + resize;
                if (horizontal) {
                    layoutParams5.leftMargin = paddingH * 2 + iconSize * 2 + resize + padding;
                    layoutParams5.topMargin = paddingV * 2 + iconSize + resize;
                }
                button5.setLayoutParams(layoutParams5);
                frameLayout.addView(button5);

                FrameLayout.LayoutParams layoutParams6 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams6.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams6.topMargin = paddingV * 3 + iconSize * 2 + resize;
                if (horizontal) {
                    layoutParams6.leftMargin = paddingH * 2 + iconSize * 2 + resize + padding;
                    layoutParams6.topMargin = paddingV + resize;
                }
                button6.setLayoutParams(layoutParams6);
                frameLayout.addView(button6);

                FrameLayout.LayoutParams layoutParams7 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams7.leftMargin = paddingH + resize;
                layoutParams7.topMargin = paddingV * 4 + iconSize * 3 + resize;
                if (horizontal) {
                    layoutParams7.leftMargin = paddingH * 3 + iconSize * 3 + resize + padding;
                    layoutParams7.topMargin = paddingV * 2 + iconSize + resize;
                }
                button7.setLayoutParams(layoutParams7);
                frameLayout.addView(button7);

                FrameLayout.LayoutParams layoutParams8 = new FrameLayout.LayoutParams(realIconSize, realIconSize);
                layoutParams8.leftMargin = paddingH * 2 + iconSize + resize;
                layoutParams8.topMargin = paddingV * 4 + iconSize * 3 + resize;
                if (horizontal) {
                    layoutParams8.leftMargin = paddingH * 3 + iconSize * 3 + resize + padding;
                    layoutParams8.topMargin = paddingV + resize;
                }
                button8.setLayoutParams(layoutParams8);
                frameLayout.addView(button8);

                FrameLayout power = new FrameLayout(getContext());
                int size = dpToPx(getContext(), 60);
                FrameLayout.LayoutParams powerLayoutParams = new FrameLayout.LayoutParams(size, size);
                if (horizontal) {
                    powerLayoutParams.leftMargin = (int) (iconSize * 2 + paddingH * 1.5f - size / 2) + padding;
                    powerLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                } else {
                    powerLayoutParams.gravity = Gravity.CENTER;
                }
                power.setLayoutParams(powerLayoutParams);
                frameLayout.addView(power);

                powerBgImageView = new ImageView(getContext());
                powerBgImageView.setLayoutParams(new FrameLayout.LayoutParams(size, size));
                powerBgImageView.setImageResource(R.drawable.power_base_unpressed);
                powerBgImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                power.addView(powerBgImageView);

                FrameLayout.LayoutParams powerImageLayoutParams = new FrameLayout.LayoutParams(size / 2, size / 2);
                powerImageLayoutParams.gravity = Gravity.CENTER;

                powerBgImageView2 = new ImageView(getContext());
                powerBgImageView2.setLayoutParams(powerImageLayoutParams);
                powerBgImageView2.setImageResource(R.drawable.power_base_pressed);
                powerBgImageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
                power.addView(powerBgImageView2);
                powerBgImageView2.setVisibility(View.GONE);

                powerImageView = new ImageView(getContext());
                powerImageView.setLayoutParams(powerImageLayoutParams);
                powerImageView.setImageResource(R.drawable.power);
                powerImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                powerImageView.setColorFilter(0xffffffff);
                power.addView(powerImageView);

                if (AppGlobal.isPowerOn()) {
                    powerBgImageView.setVisibility(View.VISIBLE);
                    powerBgImageView2.setVisibility(View.GONE);
                    powerImageView.setAlpha(1f);
                } else {
                    powerBgImageView.setVisibility(View.GONE);
                    powerBgImageView2.setVisibility(View.VISIBLE);
                    powerImageView.setAlpha(0.2f);
                }

                power.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getActionMasked();
                        if (action == MotionEvent.ACTION_DOWN) {
                            if (AppGlobal.isPowerOn()) {
                                powerBgImageView.setVisibility(View.GONE);
                                powerBgImageView2.setVisibility(View.VISIBLE);
                                powerImageView.setAlpha(0.2f);
                                AppGlobal.setPowerOn(false);
                                button1.turnOff();
                                button2.turnOff();
                                button3.turnOff();
                                button4.turnOff();
                                button5.turnOff();
                                button6.turnOff();
                                button7.turnOff();
                                button8.turnOff();
                                return true;
                            }
                        }
                        return false;
                    }
                });


                ImageView image = view.findViewById(R.id.status_bar);
                FrameLayout.LayoutParams layoutParams9 = (FrameLayout.LayoutParams) image.getLayoutParams();
                layoutParams9.width = (int) ((iconSize * 2 + paddingH) * STATUS_RATIO);

                LinearLayout statusLayout = view.findViewById(R.id.status_layout);
                FrameLayout.LayoutParams layoutParams10 = (FrameLayout.LayoutParams) statusLayout.getLayoutParams();
                layoutParams10.width = (int) ((iconSize * 2 + paddingH) * STATUS_RATIO);

                if (horizontal) {
                    paddingV = height * PADDING / (PADDING * 3 + ICON_SIZE * 2);
                    iconSize = height * ICON_SIZE / (PADDING * 3 + ICON_SIZE * 2);
                    paddingH = (width - iconSize * 4) / 4;
                    layoutParams9.height = (int) ((iconSize * 2 + paddingV) * STATUS_RATIO);
                    layoutParams10.height = (int) ((iconSize * 2 + paddingV) * STATUS_RATIO);
                }

                image.setLayoutParams(layoutParams9);
                statusLayout.setLayoutParams(layoutParams10);
            }
        });
    }

    @Subscribe(sticky = true)
    public void onEvent(MessageEvent event) {
        if (event.type == MessageEvent.MessageEventType.TURN_ON_OFF) {
            if (AppGlobal.isPowerOn()) {
                powerBgImageView.setVisibility(View.VISIBLE);
                powerBgImageView2.setVisibility(View.GONE);
                powerImageView.setAlpha(1f);
            } else {
                powerBgImageView.setVisibility(View.GONE);
                powerBgImageView2.setVisibility(View.VISIBLE);
                powerImageView.setAlpha(0.2f);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (button1 != null) {
            button1.setButtonData(AppGlobal.getButton(1));
        }
        if (button2 != null) {
            button2.setButtonData(AppGlobal.getButton(2));
        }
        if (button3 != null) {
            button3.setButtonData(AppGlobal.getButton(3));
        }
        if (button4 != null) {
            button4.setButtonData(AppGlobal.getButton(4));
        }
        if (button5 != null) {
            button5.setButtonData(AppGlobal.getButton(5));
        }
        if (button6 != null) {
            button6.setButtonData(AppGlobal.getButton(6));
        }
        if (button7 != null) {
            button7.setButtonData(AppGlobal.getButton(7));
        }
        if (button8 != null) {
            button8.setButtonData(AppGlobal.getButton(8));
        }
    }
}
