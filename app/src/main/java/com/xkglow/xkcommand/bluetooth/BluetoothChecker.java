package com.xkglow.xkcommand.bluetooth;

import android.bluetooth.BluetoothAdapter;

public class BluetoothChecker {

    public static boolean isBluetoothSupported() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
        if (mBluetoothAdapter == null)
            return false;
        else
            return true;
    }

    public static boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled())
            return true;
        else
            return false;
    }

    public static void turnOnBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
        mBluetoothAdapter.enable();
    }
}
