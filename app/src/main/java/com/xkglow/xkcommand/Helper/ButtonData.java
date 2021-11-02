package com.xkglow.xkcommand.Helper;

import java.io.Serializable;
import java.util.Arrays;

public class ButtonData implements Serializable {
    public int id;
    public int type;
    public String text;
    public int iconResourceId;
    public String imagePath;
    public boolean sync;
    public boolean momentary;
    public boolean[] channels;

    public ButtonData(int id) {
        this.id = id;
        this.sync = false;
        this.momentary = false;
        this.channels = new boolean[8];
        Arrays.fill(this.channels, false);
        this.channels[0] = true;
        this.type = 0;
        this.text = "Button Name " + id;

    }

    public void setNA() {
        this.type = 0;
    }

    public void setText(String text) {
        this.type = 1;
        this.text = text;
    }

    public void setIconResourceId(int iconResourceId) {
        this.type = 2;
        this.iconResourceId = iconResourceId;
    }

    public void setImagePath(String imagePath) {
        this.type = 3;
        this.imagePath = imagePath;
    }
}
