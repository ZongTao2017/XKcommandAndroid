package com.xkglow.xkcommand.Helper;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.util.Log;

import com.xkglow.xkcommand.R;
import com.xkglow.xkcommand.bluetooth.BluetoothService;

import org.greenrobot.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AppGlobal {
    private static Context context;
    public static BluetoothService bluetoothService;
    private static DeviceData currentDevice;
    private static DeviceData currentPairingDevice;
    private static LinkedHashMap<String, BluetoothGatt> bluetoothGattMap;
    private static LinkedHashMap<String, DeviceData> scanDeviceMap;
    private static HashMap<String, DeviceData> pairedDeviceMap;
    private static boolean isSavingPairedDevices = false;

    public static int loaderId;

    private static final String FILE_DEVICE = "FileDevice";
    public static final int REQUEST_LOCATION = 110;
    public static final int REQUEST_ENABLE_BT = 111;

    public static void init(Context contextGlobal) {
        context = contextGlobal;
        loaderId = 1;
        bluetoothGattMap = new LinkedHashMap<>();
        scanDeviceMap = new LinkedHashMap<>();
        pairedDeviceMap = new HashMap<>();
        loadPairedDevices();
    }

    public static int getIconResourceId(int index) {
        switch (index) {
            case 0:
                return R.drawable.button_icon1;
            case 1:
                return R.drawable.button_icon2;
            case 2:
                return R.drawable.button_icon3;
            case 3:
                return R.drawable.button_icon4;
            case 4:
                return R.drawable.button_icon5;
            case 5:
                return R.drawable.button_icon6;
            case 6:
                return R.drawable.button_icon7;
            case 7:
                return R.drawable.button_icon8;
        }
        return R.drawable.button_icon1;
    }

    public static LinkedHashMap<String, DeviceData> getScanDeviceMap() {
        return scanDeviceMap;
    }

    public static HashMap<String, DeviceData> getPairedDeviceMap() {
        return pairedDeviceMap;
    }

    public static HashMap<String, DeviceData> getUnpairedDeviceMap() {
        HashMap<String, DeviceData> unpairedDeviceMap = new HashMap<>();
        for (String address : scanDeviceMap.keySet()) {
            if (!pairedDeviceMap.containsKey(address)) {
                unpairedDeviceMap.put(address, scanDeviceMap.get(address));
            }
        }
        return unpairedDeviceMap;
    }

    public static DeviceData getCurrentDevice() {
        return currentDevice;
    }

    public static int getCurrentDeviceIndex() {
        ArrayList<DeviceData> pairedDevices = new ArrayList<>(getPairedDeviceMap().values());
        for (int i = 0; i < pairedDevices.size(); i++) {
            DeviceData deviceData1 = pairedDevices.get(i);
            if (currentDevice.address.equals(deviceData1.address)) {
                return i;
            }
        }
        return -1;
    }

    public static DeviceData getCurrentPairingDevice() {
        return currentPairingDevice;
    }

    public static DeviceData getFirstScanUnpairedDevice() {
        for (String address : scanDeviceMap.keySet()) {
            if (!pairedDeviceMap.containsKey(address)) {
                return scanDeviceMap.get(address);
            }
        }
        return null;
    }

    public static void setCurrentDevice(int index) {
        ArrayList<DeviceData> pairedDevices = new ArrayList<>(getPairedDeviceMap().values());
        if (index >= 0 && index <= pairedDevices.size() - 1) {
            DeviceData deviceData = pairedDevices.get(index);
            setCurrentDevice(deviceData);
        }
    }

    public static void setCurrentDevice(DeviceData deviceData) {
        if (currentDevice != null && !currentDevice.equals(deviceData)) {
            disconnect(currentDevice, false);
        }
        currentDevice = deviceData;
        if (deviceData != null) {
            ArrayList<DeviceData> pairedDevices = new ArrayList<>(getPairedDeviceMap().values());
            for (int i = 0; i < pairedDevices.size(); i++) {
                DeviceData deviceData1 = pairedDevices.get(i);
                if (deviceData.address.equals(deviceData1.address)) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.CHANGE_DEVICE, i));
                    return;
                }
            }
        }
    }

    public static DeviceData findDevice(DeviceData deviceData) {
        for (DeviceData deviceData1 : getPairedDeviceMap().values()) {
            if (deviceData.address.equals(deviceData1.address)) return deviceData1;
        }
        return null;
    }

    public static int findDeviceIndex(DeviceData deviceData) {
        ArrayList<DeviceData> pairedDevices = new ArrayList<>(getPairedDeviceMap().values());
        for (int i = 0; i < pairedDevices.size(); i++) {
            DeviceData deviceData1 = pairedDevices.get(i);
            if (deviceData.address.equals(deviceData1.address)) {
                return i;
            }
        }
        return -1;
    }

    public static LinkedHashMap<String, BluetoothGatt> getGattMap() {
        return bluetoothGattMap;
    }

    public static void clearGattMap() {
        bluetoothGattMap.clear();
    }

    public static void setBluetoothGatt(String address, BluetoothGatt bluetoothGatt) {
        if (bluetoothGatt == null) {
            bluetoothGattMap.remove(address);
        } else {
            bluetoothGattMap.put(address, bluetoothGatt);
        }
    }

    public static BluetoothGatt getBluetoothGatt(String address) {
        return bluetoothGattMap.get(address);
    }

    public static DeviceData getFirstScanPairedDevice() {
        for (String address : scanDeviceMap.keySet()) {
            if (pairedDeviceMap.containsKey(address)) {
                return scanDeviceMap.get(address);
            }
        }
        return null;
    }

    public static boolean hasDeviceGatt(String address) {
        return bluetoothGattMap.containsKey(address);
    }

    public static void connect(DeviceData deviceData) {
        if (bluetoothService != null && deviceData != null &&
                deviceData.deviceState != DeviceState.CONNECTING &&
                deviceData.deviceState != DeviceState.CONNECTED &&
                deviceData.deviceState != DeviceState.INITIALIZING &&
                deviceData.deviceState != DeviceState.READY) {
            deviceData.deviceState = DeviceState.CONNECTING;
            bluetoothService.connect(deviceData.address);
        }
    }

    public static void disconnect(DeviceData deviceData, boolean shouldRemove) {
        if (bluetoothService != null) {
            deviceData.deviceState = DeviceState.DISCONNECTING;
            bluetoothService.disconnect(deviceData, shouldRemove);
        }
    }

    public static void setCurrentPairingDevice(DeviceData deviceData) {
        currentPairingDevice = deviceData;
        if (currentPairingDevice != null) {
            AppGlobal.addPairDevice(currentPairingDevice);
        }
        setCurrentDevice(deviceData);
        connect(deviceData);
    }

    public static void addPairDevice(DeviceData deviceData) {
        if (deviceData != null && !pairedDeviceMap.containsKey(deviceData.address)) {
            pairedDeviceMap.put(deviceData.address, deviceData);
            savePairedDevices();
        }
    }

    public static void removePairDevice(String address) {
        if (address != null && pairedDeviceMap.containsKey(address)) {
            pairedDeviceMap.remove(address);
            savePairedDevices();
        }
    }
    public static void savePairedDevices() {
        try {
            if (isSavingPairedDevices) {
                Log.e("ERROR", "not saving paired devices");
                return;
            }
            if (pairedDeviceMap == null || context == null) {
                Log.e("ERROR", "save paired devices failed!");
                return;
            }
            isSavingPairedDevices = true;
            FileOutputStream fos = context.openFileOutput(FILE_DEVICE, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(pairedDeviceMap);
            os.close();
            fos.close();
            isSavingPairedDevices = false;
            Log.e("YAY", "save paired devices done!!!");
        } catch (Exception e) {
            Log.e("ERROR", "save paired devices error!");
            isSavingPairedDevices = false;
        }
    }

    public static boolean loadPairedDevices() {
        try {
            if (context == null) {
                Log.e("ERROR", "load paired devices failed!");
                return false;
            }
            FileInputStream fis = context.openFileInput(FILE_DEVICE);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object obj = is.readObject();
            if (obj == null) {
                pairedDeviceMap = new HashMap<>();
            } else {
                pairedDeviceMap = (HashMap<String, DeviceData>) obj;
            }
            is.close();
            fis.close();
            Log.e("YAY", "load paired devices done!!!");
            for (String address : pairedDeviceMap.keySet()) {
                DeviceData deviceData = pairedDeviceMap.get(address);
                deviceData.deviceState = DeviceState.OFFLINE;
            }
        } catch (Exception e) {
            Log.e("ERROR", "load paired devices error!");
            return false;
        }
        return pairedDeviceMap.size() == 0;
    }

    public static DeviceData updateScanDevice(String address, String name, int rssi) {
        DeviceData deviceData;
        if (scanDeviceMap.containsKey(address)) {
            deviceData = scanDeviceMap.get(address);
            deviceData.addRssi(rssi);
        } else if (pairedDeviceMap.containsKey(address)) {
            deviceData = pairedDeviceMap.get(address);
            deviceData.addRssi(rssi);
            scanDeviceMap.put(address, deviceData);
        } else {
            deviceData = new DeviceData(address, name);
            deviceData.addRssi(rssi);
            scanDeviceMap.put(address, deviceData);
        }
        if (deviceData.deviceState == DeviceState.OFFLINE || deviceData.deviceState == DeviceState.DISCONNECTING || deviceData.deviceState == DeviceState.DISCONNECTED) {
            deviceData.deviceState = DeviceState.ONLINE;
        }
        return deviceData;
    }

    public static void updateDeviceState(ArrayList<String> deviceAddressList) {
        Iterator<Map.Entry<String, DeviceData>> iter = scanDeviceMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, DeviceData> entry = iter.next();
            String address = entry.getKey();
            if (!deviceAddressList.contains(address)) {
                iter.remove();
            }
        }
        for (String address : pairedDeviceMap.keySet()) {
            if (!deviceAddressList.contains(address)) {
                DeviceData deviceData = pairedDeviceMap.get(address);
                if (!deviceData.equals(currentDevice) ||
                        currentDevice.deviceState == DeviceState.DISCONNECTED) {
                    deviceData.deviceState = DeviceState.OFFLINE;
                }
            }
        }
    }

    public static void turnOffBluetooth() {
        HashMap<String, DeviceData> deviceMap = new HashMap<>(pairedDeviceMap);
        deviceMap.putAll(scanDeviceMap);
        for (String address : deviceMap.keySet()) {
            DeviceData deviceData = deviceMap.get(address);
            deviceData.deviceState = DeviceState.OFFLINE;
        }
        bluetoothGattMap.clear();
        scanDeviceMap.clear();
    }

    public static DeviceData getDevice(String address) {
        if (pairedDeviceMap.containsKey(address)) {
            return pairedDeviceMap.get(address);
        }
        if (scanDeviceMap.containsKey(address)) {
            return scanDeviceMap.get(address);
        }
        return null;
    }

    public static void writeUserSettings() {
        if (currentDevice != null) {
            float volt = currentDevice.systemData.cutoffInput;
            byte voltByte = (byte) ((volt + 0.001f) / 0.2f);
            currentDevice.userSettingsBytes[0] = voltByte;
            currentDevice.userSettingsBytes[3] = (byte) currentDevice.systemData.turnBluetoothOffAfter;
            if (!currentDevice.systemData.bluetoothAutoOff) {
                currentDevice.userSettingsBytes[3] = 0;
            }
            currentDevice.userSettingsBytes[4] = (byte) Helper.getRed(currentDevice.systemData.buttonColor);
            currentDevice.userSettingsBytes[5] = (byte) Helper.getGreen(currentDevice.systemData.buttonColor);
            currentDevice.userSettingsBytes[6] = (byte) Helper.getBlue(currentDevice.systemData.buttonColor);
            currentDevice.userSettingsBytes[7] = (byte) Helper.getRed(currentDevice.systemData.buttonWarningColor);
            currentDevice.userSettingsBytes[8] = (byte) Helper.getGreen(currentDevice.systemData.buttonWarningColor);
            currentDevice.userSettingsBytes[9] = (byte) Helper.getBlue(currentDevice.systemData.buttonWarningColor);
            bluetoothService.writeUserSettings(currentDevice);
        }
    }

    public static void writeChannelAmpLimit(int index, float amp) {
        if (currentDevice != null) {
            byte ampByte = (byte) ((amp + 0.001f) / 0.2f);
            if (currentDevice.channelBytes[index * 2 + 1] != ampByte) {
                currentDevice.channelBytes[index * 2 + 1] = ampByte;
                bluetoothService.writeChannel(currentDevice);
            }
        }
    }
    
    public static void writeButtonData(int buttonId) {
        if (currentDevice != null) {
            currentDevice.buttons[buttonId - 1].buttonBytes[0] = Helper.setBit(currentDevice.buttons[buttonId - 1].buttonBytes[0], 3, 0);
            bluetoothService.writeButton(currentDevice, currentDevice.buttons[buttonId - 1]);
        }
    }

    public static void writeButtonOnOff(int buttonId, boolean on) {
        if (currentDevice != null) {
            currentDevice.buttons[buttonId - 1].buttonBytes[0] = Helper.setBit(currentDevice.buttons[buttonId - 1].buttonBytes[0], 3, 1);
            currentDevice.buttons[buttonId - 1].buttonBytes[0] = Helper.setBit(currentDevice.buttons[buttonId - 1].buttonBytes[0], 0, on ? 1 : 0);
            bluetoothService.writeButton(currentDevice, currentDevice.buttons[buttonId - 1]);
        }
    }

    public static void writeButtonAllOff() {
        if (currentDevice != null) {
            for (int i = 0; i < 8; i++) {
                currentDevice.buttons[i].buttonBytes[0] = Helper.setBit(currentDevice.buttons[i].buttonBytes[0], 3, 1);
                currentDevice.buttons[i].buttonBytes[0] = Helper.setBit(currentDevice.buttons[i].buttonBytes[0], 0, 0);
                bluetoothService.writeAllButtons(currentDevice);
            }
        }
    }

    public static void writeSensorData(int sensorId) {
        if (currentDevice != null) {
            bluetoothService.writeSensor(currentDevice, currentDevice.sensors[sensorId - 1]);
        }
    }
}
