package com.xkglow.xkcommand.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.xkglow.xkcommand.Helper.ColorPalette;
import com.xkglow.xkcommand.Helper.Helper;

public class EditColorView extends BaseViewGroup {
    ColorGradient colorGradient;
    ColorPickerMarker colorPickerMarker;

    PointF markerCenter = new PointF();
    ColorPalette colorPalette = null;

    int color;
    float px, py;
    float gradientHeight;
    int angleHeight;
    int angleGap;
    int markerRad;
    int markerStroke;

    float touchX;
    float touchY;

    public EditColorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initDimensions();

        colorGradient = new ColorGradient(getContext());
        colorPickerMarker = new ColorPickerMarker(getContext());
        addView(colorGradient);
        addView(colorPickerMarker);
    }

    public void setColor(int color) {
        PointF center = getLocationFromColor(color);
        ColorPalette colorPalette = new ColorPalette();
        colorPalette.setColor(color);
        colorPalette.setLocation(center);
        setColorPalette(colorPalette);
    }

    public int getColor() {
        return getColorFromLocation(markerCenter);
    }

    private void initDimensions() {
        px = Helper.getScreenWidth(getContext());
        py = Helper.dpToPx(getContext(), 240);
        angleHeight = angleGap = Helper.dpToPx(getContext(), 12);
        markerRad = Helper.dpToPx(getContext(), 26);
        markerStroke = Helper.dpToPx(getContext(), 6);
        gradientHeight = py;
    }

    private void setColorPalette(ColorPalette palette) {
        this.colorPalette = palette;
        markerCenter = colorPalette.getLocation();
        colorGradient.invalidate();
        colorPickerMarker.invalidate();
    }

    private class ColorGradient extends View {

        public ColorGradient(Context context) {
            super(context);
            this.setDrawingCacheEnabled(true);
            init();
        }

        Paint gradientPaint;

        RectF gradRectF = new RectF();

        private void init() {
            gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            gradientPaint.setStyle(Paint.Style.FILL);
            gradientPaint.setDither(true);
            setLayerType(LAYER_TYPE_SOFTWARE, gradientPaint);
        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawColor(Color.parseColor("#4D535A"));

            int[] gradientColors = new int[] { Color.RED, Color.YELLOW,
                    Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA,
                    Color.RED };

            LinearGradient linearGradient = new LinearGradient(0, 0, px, 0,
                    gradientColors, null, Shader.TileMode.CLAMP);

            gradientColors = new int[] { Color.TRANSPARENT, Color.WHITE };

            LinearGradient gradient = new LinearGradient(0, 0, 0,
                    gradientHeight, gradientColors, null, Shader.TileMode.CLAMP);

            ComposeShader shader = new ComposeShader(gradient, linearGradient,
                    PorterDuff.Mode.DST_OVER);

            gradientPaint.setShader(shader);

            gradRectF.set(0, 0, px, gradientHeight);
            canvas.drawRect(gradRectF, gradientPaint);

            if (colorPalette != null) {
                if (colorPalette.getLocation().x == 999){
                    colorPalette.setLocation(getLocationFromColor(colorPalette.getColor()));
                }
                markerCenter = colorPalette.getLocation();
            }
        }
    }

    boolean isTapUp = true;
    boolean isMarkerDrag = false;

    private class ColorPickerMarker extends View {

        public ColorPickerMarker(Context context) {
            super(context);
            init();
        }

        Paint markerPaint;
        Paint markerOuterPaint;

        private void init() {

            markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            markerPaint.setStyle(Paint.Style.FILL);

            markerOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            markerOuterPaint.setStyle(Paint.Style.STROKE);
            markerOuterPaint.setColor(Color.WHITE);
            markerOuterPaint.setStrokeWidth(markerStroke);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (colorPalette != null) {
                color = getColorFromLocation(markerCenter);
                markerPaint.setColor(color);
                colorPalette.setColor(color);
                colorPalette.setLocation(markerCenter);
                canvas.drawCircle(markerCenter.x, markerCenter.y, markerRad,
                        markerPaint);
                canvas.drawCircle(markerCenter.x, markerCenter.y, markerRad,
                        markerOuterPaint);
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            requestDisallowInterceptTouchEvent(true);
            touchX = event.getX();
            touchY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isInGradient(touchX, touchY))
                        isMarkerDrag = true;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    isTapUp = false;
                    if (isInGradient(touchX, touchY)) {
                        isMarkerDrag = true;
                        markerCenter = new PointF(touchX, touchY);
                        colorPickerMarker.invalidate();
                    } else {
                        if (!isTapUp && isMarkerDrag) {
                            markerCenter = new PointF(touchX, touchY < 0 ? 1
                                    : gradientHeight - 1);
                            colorPickerMarker.invalidate();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    requestDisallowInterceptTouchEvent(false);
                    isTapUp = true;
                    isMarkerDrag = false;
                    return true;
                default:
                    break;
            }
            return super.onTouchEvent(event);
        }

        private boolean isInGradient(float x, float y) {
            if (x < 0 || x > px)
                return false;
            if (y < 0 || y > gradientHeight)
                return false;
            return true;
        }
    }

    private int getColorFromLocation(PointF location) {
        return color = Color.HSVToColor(new float[] { location.x * 360 / px,
                1.0f - location.y / gradientHeight, 1 });
    }

    private PointF getLocationFromColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float x = (float) hsv[0] / (360f / px);
        float y = (float) Math.floor(Math.abs(((float) -1f + hsv[1])
                * gradientHeight));
        Log.i("Location from hsv", "X : " + x + " | Y : " + y);
        return new PointF(x, y);
    }
}

