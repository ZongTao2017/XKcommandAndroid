package com.xkglow.newapp.Helper;

import android.content.Context;
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
}
