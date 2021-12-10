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
    public int[] actions;
    public boolean isPressed;
    public byte[] buttonBytes;

    public ButtonData(int id) {
        this.id = id;
        this.sync = true;
        this.momentary = false;
        this.channels = new boolean[8];
        Arrays.fill(this.channels, false);
        this.channels[id - 1] = true;
        this.actions = new int[8];
        Arrays.fill(this.actions, 1);
        this.type = 1;
        this.text = null;
        this.imagePath = null;
        this.iconResourceId = 0;
        this.isPressed = false;
        this.buttonBytes = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    }
}
