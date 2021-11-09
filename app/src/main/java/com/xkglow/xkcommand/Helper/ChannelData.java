package com.xkglow.xkcommand.Helper;

import java.io.Serializable;

public class ChannelData implements Serializable {
    public int id;
    public String name;
    public int maxCurrent;

    public ChannelData(int id) {
        this.id = id;
        maxCurrent = 9;
        name = "Channel Name " + id;
    }
}
