package com.xkglow.xkcommand.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.xkglow.xkcommand.R;

public class SwitchView extends ViewGroup {
    private boolean mIsSwitchOn;
    private float mCircleViewCenterX;
    private int mWidth;
    private int mHeight;
    private int mSwitchOnColor;
    private int mSwitchOffColor;
    private Paint mBgPaint;
    private Paint mBgStrokePaint;
    private Paint mCirclePaint;
    private GestureDetector mDetector;
    private boolean mIsTouchable;
    private boolean mIsScrollable;
    private boolean mRespondToSingleTap;
    private final Handler mHandler = new Handler();
    public String mLabel;

    private static final int PADDING = 4;

    private OnSwitchListener mListener;

    public SwitchView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        mIsSwitchOn = true;
        mIsTouchable = true;
        mIsScrollable = false;
        mRespondToSingleTap = true;

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL);

        mBgStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgStrokePaint.setStyle(Paint.Style.STROKE);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(ContextCompat.getColor(context, R.color.white));
        setLayerType(LAYER_TYPE_SOFTWARE, mCirclePaint);

        mDetector = new GestureDetector(getContext(), new GestureListener());
        mDetector.setIsLongpressEnabled(false);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CustomSwitch,
                0, 0);
        try {
            int type = a.getColor(R.styleable.CustomSwitch_switch_type, 0);
            switch (type) {
                case 0:
                    mSwitchOnColor = ContextCompat.getColor(context, R.color.switch_gray);
                    mSwitchOffColor = ContextCompat.getColor(context, android.R.color.transparent);
                    mBgStrokePaint.setColor(ContextCompat.getColor(context, R.color.white));
                    mBgStrokePaint.setStrokeWidth(PADDING);
                    break;
                case 1:
                    mSwitchOnColor = ContextCompat.getColor(context, R.color.switch_green);
                    mSwitchOffColor = ContextCompat.getColor(context, R.color.white);
                    mBgStrokePaint.setColor(ContextCompat.getColor(context, R.color.switch_light_gray));
                    mBgStrokePaint.setStrokeWidth(PADDING * 2);
                    break;
                default:
                    break;
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mWidth = r - l;
        mHeight = b - t;
        if (mIsSwitchOn) {
            mCircleViewCenterX = mWidth - mHeight / 2f;
        } else {
            mCircleViewCenterX = mHeight / 2f;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        float r = mHeight / 2f - PADDING;
        float radius = r - PADDING;
        if (mIsSwitchOn) {
            mBgPaint.setColor(mSwitchOnColor);
        } else {
            mBgPaint.setColor(mSwitchOffColor);
            canvas.drawRoundRect(PADDING, PADDING, mWidth - PADDING, mHeight - PADDING, r, r, mBgStrokePaint);
            mCirclePaint.setShadowLayer(5, 2, 2, 0x80000000);
        }
        canvas.drawRoundRect(PADDING, PADDING, mWidth - PADDING, mHeight - PADDING, r, r, mBgPaint);

        canvas.drawCircle(mCircleViewCenterX, mHeight / 2f, radius, mCirclePaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)) {
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            onTouchMove(event);
            return true;
        }
        return false;
    }

    public void setOnSwitchListener(OnSwitchListener listener) {
        mListener = listener;
    }

    public boolean isSwitchOn() {
        return mIsSwitchOn;
    }

    public void switchOn(boolean enableListener, boolean showMessage) {
        if (!mIsSwitchOn) {
            float currentX = mCircleViewCenterX;
            mIsSwitchOn = true;
            mIsTouchable = false;
            for (int i = 0; i < 100; i++) {
                final float centerX = currentX + (i + 1) * (mWidth - mHeight / 2f - currentX) / 100f;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCircleViewCenterX = centerX;
                        if (centerX == mWidth - mHeight / 2f) {
                            mIsTouchable = true;
                        }
                        invalidate();
                    }
                }, i);
            }
            if (enableListener && mListener != null) {
                mListener.onSwitchOn(showMessage);
            }
        }
    }

    public void switchOff(boolean enableListener, boolean showMessage) {
        if (mIsSwitchOn) {
            float currentX = mCircleViewCenterX;
            mIsSwitchOn = false;
            mIsTouchable = false;
            for (int i = 0; i < 100; i++) {
                final float centerX = currentX - (i + 1) * (currentX - mHeight / 2f) / 100f;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCircleViewCenterX = centerX;
                        if (centerX == mHeight / 2f) {
                            mIsTouchable = true;
                        }
                        invalidate();
                    }
                }, i);
            }
            if (enableListener && mListener != null) {
                mListener.onSwitchOff(showMessage);
            }
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            float radius = mHeight / 2f - PADDING;
            mRespondToSingleTap = true;
            if (mIsTouchable) {
                mIsScrollable = e.getX() >= mCircleViewCenterX - radius && e.getX() <= mCircleViewCenterX + radius;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mRespondToSingleTap) {
                if (e.getX() < mWidth / 2 && mIsSwitchOn) {
                    switchOff(true, true);
                }
                if (e.getX() > mWidth / 2 && !mIsSwitchOn) {
                    switchOn(true, true);
                }
            }
            return true;
        }
    }

    private void onTouchMove(MotionEvent e) {
        if (mIsScrollable) {
            RectF rect = new RectF(0, 0, mWidth, mHeight);
            if (rect.contains(e.getX(), e.getY())) {
                mRespondToSingleTap = false;
                if (mIsSwitchOn && e.getX() < mCircleViewCenterX) {
                    mIsScrollable = false;
                    switchOff(true, true);
                }
                if (!mIsSwitchOn && e.getX() > mCircleViewCenterX) {
                    mIsScrollable = false;
                    switchOn(true, true);
                }
            } else {
                mIsScrollable = false;
            }
        }
    }

    public interface OnSwitchListener {
        void onSwitchOn(boolean showMessage);
        void onSwitchOff(boolean showMessage);
    }
}
