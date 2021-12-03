package com.xkglow.xkcommand.Helper;

import java.io.Serializable;

public class SystemData implements Serializable {
    public String name;
    public int buttonColor, buttonWarningColor;
    public int turnBluetoothOffAfter;
    public boolean bluetoothAutoOff;

    public SystemData() {
        buttonColor = 0xff009be3;
        buttonWarningColor = 0xffff0000;
        turnBluetoothOffAfter = 120;
        bluetoothAutoOff = true;
    }
}
