package com.xkglow.xkcommand.Helper;

import java.io.Serializable;
import java.util.ArrayList;

public class DeviceData implements Serializable {
    public String deviceId;
    public String name;
    public String address;
    public ArrayList<Integer> rssiList;
    private boolean isAddingRssi = false;
    public int signalPercent;
    public DeviceState deviceState;
}
