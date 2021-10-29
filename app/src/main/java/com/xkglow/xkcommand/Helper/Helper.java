package com.xkglow.xkcommand.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

public class Helper {
    public static final int PADDING = 1;
    public static final int ICON_SIZE = 3;
    public static final float ICON_RATIO = 455 / 360f;
    public static final float STATUS_RATIO = 936f / 842f;

    public static int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int)(dp * displayMetrics.density);
    }

    public static int getScreenWidth(Context context) {
        if (context == null) return 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        if (context == null) return 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static Bitmap getResizedBitmapWithFixedWidth(Bitmap image, int fixedWidth) {
        if (image == null) return null;
        int width = image.getWidth();
        int height = image.getHeight();

        if (width <= fixedWidth) {
            return image;
        }

        float ratio = (float) fixedWidth / (float) width;
        height = (int) (height * ratio);
        return Bitmap.createScaledBitmap(image, fixedWidth, height, true);
    }
}
