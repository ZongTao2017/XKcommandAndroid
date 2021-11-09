package com.xkglow.xkcommand.Helper;

import java.io.Serializable;
import java.util.Arrays;

public class SensorData implements Serializable {
    public int id;
    public String name;
    public int function;
    public int brightness;
    public boolean[] channels;
    public int[] actions;
    public boolean sync;

    public SensorData(int id) {
        this.id = id;
        this.name = "Sensor Name " + id;
        this.function = 1;
        this.brightness = 100;
        this.channels = new boolean[8];
        Arrays.fill(this.channels, false);
        this.actions = new int[8];
        Arrays.fill(this.actions, 0);
        this.sync = true;
    }
}
