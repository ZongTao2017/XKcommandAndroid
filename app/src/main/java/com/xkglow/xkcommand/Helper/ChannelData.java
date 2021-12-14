package com.xkglow.xkcommand.Helper;

import java.io.Serializable;

public class ChannelData implements Serializable {
    public int id;
    public String name;
    public int maxAmp;
    public int amp;

    public ChannelData(int id) {
        this.id = id;
        maxAmp = 9;
        name = "Channel Name " + id;
    }
}
