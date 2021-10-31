package com.xkglow.xkcommand.Helper;

import android.graphics.PointF;

import java.io.Serializable;

public class ColorPalette implements Serializable {
    int color;
    PointF location;

    public ColorPalette() {

    }

    public ColorPalette(ColorPalette palettes) {
        this.color = palettes.color;
        this.location = palettes.location;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public PointF getLocation() {
        return location;
    }

    public void setLocation(PointF location) {
        this.location = location;
    }

}
