package com.xkglow.xkcommand.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.R;

public class DevicePairView extends BaseViewGroup {
    CirclePattern mCirclePattern;
    AnimateCircle mAnimateCircle;
    AnimatePhone mAnimatePhone;

    float maxRadius;
    float minRadius;
    int ringColor = Color.rgb(0, 195, 255);
    PointF centerPoint;
    float centerOffsetY;
    float px;
    float py;
    double scale = 1.1;
    float animateCircleRadius = 0;
    int progress = 0;
    float dashRadius = 0;
    int percentFull = 0;

    public DevicePairView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCirclePattern = new CirclePattern(getContext());
        mAnimateCircle = new AnimateCircle(getContext());
        mAnimatePhone = new AnimatePhone(getContext());
        addView(mCirclePattern);
        addView(mAnimateCircle);
        addView(mAnimatePhone);
    }

    public void animateCircle() {
        animateCircleRadius = animateCircleRadius >= maxRadius ? 0 : ++animateCircleRadius;
        mAnimateCircle.invalidate();
    }

    public void setProgress(int progress, int percentFull) {
        this.progress = progress;
        this.percentFull = percentFull;
        mAnimatePhone.invalidate();
    }

    private class CirclePattern extends View {
        Paint circlePaint;
        Paint linePaint;
        Paint dashedLinePaint;

        public CirclePattern(Context context) {
            super(context);
            init();
        }

        private void init() {
            circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            circlePaint.setStyle(Paint.Style.FILL);

            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(2);
            linePaint.setColor(Color.WHITE);

            dashedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            dashedLinePaint.setStyle(Paint.Style.STROKE);
            dashedLinePaint.setColor(Color.WHITE);
            dashedLinePaint.setStrokeWidth(3);
            dashedLinePaint.setPathEffect(new DashPathEffect(new float[] { 6, 10 }, 6));

            centerPoint = new PointF();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            px = getMeasuredWidth();
            py = getMeasuredHeight();

            maxRadius = (float) (py * 0.7);
            minRadius = Helper.dpToPx(getContext(), 40);

            double padding = (maxRadius - minRadius) / (Math.pow(scale, 0) + Math.pow(scale, 1) + Math.pow(scale, 2) + Math.pow(scale, 3)
                    + Math.pow(scale, 4) + Math.pow(scale, 5) + Math.pow(scale, 6) + Math.pow(scale, 7) + Math.pow(scale, 8));
            centerOffsetY = Helper.dpToPx(getContext(), 65);
            centerPoint.set(px / 2, centerOffsetY);
            float radius = minRadius;
            int alpha = (int) (0.9 * 255);
            int color = Color.argb(alpha, Color.red(ringColor),
                    Color.green(ringColor), Color.blue(ringColor));
            circlePaint.setColor(color);
            canvas.drawCircle(centerPoint.x, centerPoint.y, radius,
                    circlePaint);

            for (int i = 1; i < 9; i++) {
                alpha = (int) ((0.9 * Math.pow((float) i + 1, -0.762)) * 255);
                color = Color.argb(alpha, Color.red(ringColor),
                        Color.green(ringColor), Color.blue(ringColor));
                circlePaint.setColor(color);
                padding = padding * scale;
                radius += (float) padding;
                if (i == 2) {
                    dashRadius = radius;
                }
                maxRadius = radius;
                canvas.drawCircle(centerPoint.x, centerPoint.y, radius,
                        circlePaint);
            }
            canvas.drawCircle(centerPoint.x, centerPoint.y, dashRadius,
                    dashedLinePaint);

        }
    }

    private class AnimateCircle extends View {
        Paint circleBorderPaint;

        public AnimateCircle(Context context) {
            super(context);

            circleBorderPaint = new Paint();
            circleBorderPaint.setStyle(Paint.Style.STROKE);
            circleBorderPaint.setStrokeWidth(2);
            circleBorderPaint.setColor(Color.WHITE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawColor(Color.TRANSPARENT);
            circleBorderPaint
                    .setAlpha(255 - (int) (255 * (animateCircleRadius * 100 / maxRadius) / 100));
            canvas.drawCircle(centerPoint.x, centerPoint.y, animateCircleRadius,
                    circleBorderPaint);
        }
    }

    private class AnimatePhone extends View {
        Paint icp;
        Paint ocp;
        RectF outerRectF = new RectF();
        Bitmap phoneBitmap;
        Bitmap arrowBitmap;
        Bitmap rightSignBitmap;
        Path path;
        Paint bubblePaint;

        public AnimatePhone(Context context) {
            super(context);

            icp = new Paint(Paint.ANTI_ALIAS_FLAG);
            icp.setStyle(Paint.Style.FILL);
            icp.setColor(Color.TRANSPARENT);
            icp.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

            ocp = new Paint(Paint.ANTI_ALIAS_FLAG);
            ocp.setColor(Color.BLACK);
            ocp.setAlpha(160);

            setLayerType(LAYER_TYPE_SOFTWARE, null);

            Bitmap bm = BitmapFactory.decodeResource(getResources(),
                    R.drawable.phone);
            phoneBitmap = Bitmap.createScaledBitmap(bm, Helper.dpToPx(getContext(), 50), Helper.dpToPx(getContext(), 100), false);

            Bitmap bm2 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.arrow);
            arrowBitmap = Bitmap.createScaledBitmap(bm2, Helper.dpToPx(getContext(), 30), Helper.dpToPx(getContext(), 30), false);

            Bitmap bm3 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.right_sign);
            rightSignBitmap = Bitmap.createScaledBitmap(bm3, Helper.dpToPx(getContext(), 90), Helper.dpToPx(getContext(), 90), false);

            path = new Path();

            bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bubblePaint.setStyle(Paint.Style.FILL);
            bubblePaint.setColor(ringColor);
            bubblePaint.setAlpha((int) (255 * 0.9));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int maxProgress = 100;
            float circleRad = maxRadius
                    - (progress * (maxRadius - dashRadius) / maxProgress);
            ocp.setAlpha((int) (progress * 1.60f));
            outerRectF.set(0, 0, px, py);
            canvas.drawRect(outerRectF, ocp);
            canvas.drawCircle(centerPoint.x, centerPoint.y, circleRad, icp);
            float left = centerPoint.x - phoneBitmap.getWidth() / 2;
            float top = circleRad + centerPoint.y - phoneBitmap.getHeight() - Helper.dpToPx(getContext(), 20);
            canvas.drawBitmap(phoneBitmap, left, top, null);
            if (progress < 100) {
                left = centerPoint.x - arrowBitmap.getWidth() / 2;
                top = top - arrowBitmap.getHeight() - Helper.dpToPx(getContext(), 10);
                canvas.drawBitmap(arrowBitmap, left, top, null);
            }

            if (progress == 100) {
                path.reset();
                float y = centerPoint.y + circleRad - circleRad * 2f * percentFull / 100f;
                double x1 = centerPoint.x - Math.sqrt(circleRad * circleRad - (y - centerPoint.y) * (y - centerPoint.y));
                double x2 = centerPoint.x + Math.sqrt(circleRad * circleRad - (y - centerPoint.y) * (y - centerPoint.y));
                double sweepAngle = 180;
                double startAngle = 0;
                if (y > centerPoint.y) {
                    sweepAngle = 2 * Math.acos((y - centerPoint.y) / circleRad) * 180 / Math.PI;
                    startAngle = 90 - Math.acos((y - centerPoint.y) / circleRad) * 180 / Math.PI;
                } else if (y < centerPoint.y){
                    sweepAngle = 360 - 2 * Math.acos((centerPoint.y - y) / circleRad) * 180 / Math.PI;
                    startAngle = 270 + Math.acos((centerPoint.y - y) / circleRad) * 180 / Math.PI;
                }
                path.moveTo((float) x1, y);
                path.lineTo((float) x2, y);
                path.addArc(centerPoint.x - circleRad, centerPoint.y - circleRad, centerPoint.x + circleRad,
                        centerPoint.y + circleRad, (float) startAngle, (float) sweepAngle);
                canvas.drawPath(path, bubblePaint);

                if (percentFull == 100) {
                    left = centerPoint.x - rightSignBitmap.getWidth() / 2;
                    top = (Helper.dpToPx(getContext(), 40) + centerOffsetY + circleRad) / 2 - rightSignBitmap.getHeight() / 2;
                    canvas.drawBitmap(rightSignBitmap, left, top, null);
                }
            }
        }
    }
}
