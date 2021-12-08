package com.xkglow.xkcommand.Helper;

import android.bluetooth.BluetoothGatt;

public class BluetoothData {
    public BluetoothDataType type;
    public String address;
    public String service;
    public byte[] data;

    public BluetoothData(BluetoothDataType type, String address, String service, byte[] data) {
        this.type = type;
        this.address = address;
        this.service = service;
        this.data = data;
    }
}
