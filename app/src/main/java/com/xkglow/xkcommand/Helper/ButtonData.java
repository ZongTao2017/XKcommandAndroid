package com.xkglow.xkcommand.Helper;

import java.io.Serializable;
import java.util.Arrays;

public class ButtonData implements Serializable {
    public int id;
    public String text;
    public String icon;
    public String image;
    public boolean sync;
    public boolean momentary;
    public boolean[] channels;
    public int action;

    public ButtonData(int id) {
        this.id = id;
        this.action = 2;
        this.sync = false;
        this.momentary = false;
        this.channels = new boolean[8];
        Arrays.fill(this.channels, false);
        this.channels[0] = true;
    }
}
