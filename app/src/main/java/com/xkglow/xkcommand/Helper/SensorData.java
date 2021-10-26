package com.xkglow.xkcommand.Helper;

import java.io.Serializable;
import java.util.Arrays;

public class SensorData implements Serializable {
    public String id;
    public String name;
    public int function;
    public int brightness;
    public boolean[] channels;
    public int action;

    public SensorData(String id, String name) {
        this.id = id;
        this.name = name;
        this.function = 1;
        this.brightness = 100;
        this.channels = new boolean[8];
        Arrays.fill(channels, false);
        this.channels[0] = true;
        this.action = 0;
    }
}
