package com.xkglow.xkcommand.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.SoundEffectConstants;

import com.xkglow.xkcommand.R;

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

    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(100);
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.playSoundEffect(SoundEffectConstants.CLICK, 1.0f);
    }
}
