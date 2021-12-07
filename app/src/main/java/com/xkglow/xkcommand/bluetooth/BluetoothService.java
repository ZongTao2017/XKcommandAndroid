package com.xkglow.xkcommand.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.DeviceData;
import com.xkglow.xkcommand.Helper.DeviceState;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.Helper.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BluetoothService extends Service {
    private BluetoothAdapter mBluetoothAdapter;
    private Timer mDeviceScanTimer;
    private DeviceScanTimerTask mDeviceScanTimerTask;
    private Handler mHandler;
    private Timer mConnectTimer;
    private ConnectTimerTask mConnectTimerTask;
    private ArrayList<String> mScanDeviceAddressList;

    public static final String CONTROLLER_SERVICE = "02A8AF3E-C199-4735-BACE-FA8E9F74803E";
    public static final String DEVICE_SETTING = "02A8AF3E-C002-4735-BACE-FA8E9F74803E";
    public static final String CHANNEL_STATUS = "02A8AF3E-C003-4735-BACE-FA8E9F74803E";
    public static final String BUTTON_1_STATUS = "02A8AF3E-C010-4735-BACE-FA8E9F74803E";
    public static final String BUTTON_2_STATUS = "02A8AF3E-C011-4735-BACE-FA8E9F74803E";
    public static final String BUTTON_3_STATUS = "02A8AF3E-C012-4735-BACE-FA8E9F74803E";
    public static final String BUTTON_4_STATUS = "02A8AF3E-C013-4735-BACE-FA8E9F74803E";
    public static final String BUTTON_5_STATUS = "02A8AF3E-C014-4735-BACE-FA8E9F74803E";
    public static final String BUTTON_6_STATUS = "02A8AF3E-C015-4735-BACE-FA8E9F74803E";
    public static final String BUTTON_7_STATUS = "02A8AF3E-C016-4735-BACE-FA8E9F74803E";
    public static final String BUTTON_8_STATUS = "02A8AF3E-C017-4735-BACE-FA8E9F74803E";
    public static final String SENSOR_1_STATUS = "02A8AF3E-C018-4735-BACE-FA8E9F74803E";
    public static final String SENSOR_2_STATUS = "02A8AF3E-C019-4735-BACE-FA8E9F74803E";
    public static final String SENSOR_3_STATUS = "02A8AF3E-C01A-4735-BACE-FA8E9F74803E";

    private static final String TAG = "BluetoothService";

    public class LocalBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    public void init() {
        mScanDeviceAddressList = new ArrayList<>();
        mHandler = new Handler();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            startScanTimers();
            startConnectTimers();
        }
    }

    private void close() {
        stopScanTimers();
        stopConnectTimers();
        LinkedHashMap<String, BluetoothGatt> gattMap = AppGlobal.getGattMap();
        for (String address : gattMap.keySet()) {
            BluetoothGatt bluetoothGatt = gattMap.get(address);
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect();
                bluetoothGatt.close();
            }
        }
        AppGlobal.clearGattMap();
    }

    public void startScanTimers() {
        stopScanTimers();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mDeviceScanTimer = new Timer();
            mDeviceScanTimerTask = new DeviceScanTimerTask();
            mDeviceScanTimer.schedule(mDeviceScanTimerTask, 0, 8 * 1000);
        }
    }

    public void stopScanTimers() {
        if (mDeviceScanTimer != null) {
            mDeviceScanTimer.cancel();
            mDeviceScanTimer = null;
        }
        if (mDeviceScanTimerTask != null) {
            mDeviceScanTimerTask.cancel();
            mDeviceScanTimerTask = null;
        }
    }

    public void startConnectTimers() {
        mConnectTimer = new Timer();
        mConnectTimerTask = new ConnectTimerTask();
        mConnectTimer.schedule(mConnectTimerTask, 1000, 1000);
    }

    public void stopConnectTimers() {
        if (mConnectTimerTask != null) {
            mConnectTimerTask.cancel();
            mConnectTimerTask = null;
        }
        if (mConnectTimer != null) {
            mConnectTimer.cancel();
            mConnectTimer = null;
        }
    }

    public BluetoothGatt connect(final String address) {
        if (mBluetoothAdapter == null || address == null)
            return null;
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null)
            return null;
        Log.d(TAG, "Device connecting: " + device.getName());
        BluetoothGatt bluetoothGatt = device.connectGatt(this, false, mGattCallback);
        AppGlobal.setBluetoothGatt(address, bluetoothGatt);
        return bluetoothGatt;
    }

    public void disconnect(DeviceData deviceData, boolean shouldRemove) {
        Log.d(TAG, "Device disconnecting: " + deviceData.systemData.name);
        if (shouldRemove) AppGlobal.removePairDevice(deviceData.address);
        if (mBluetoothAdapter == null || deviceData == null)
            return;
        BluetoothGatt bluetoothGatt = AppGlobal.getBluetoothGatt(deviceData.address);
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            ScanRecord sr = result.getScanRecord();
            if (sr != null) {
                BluetoothDevice device = result.getDevice();
                String address = device.getAddress();
                String name = device.getName();
                int rssi = result.getRssi();
                List<ParcelUuid> serviceUuids = sr.getServiceUuids();
                if (serviceUuids != null) {
                    for (ParcelUuid serviceUuid : serviceUuids) {
                        if (serviceUuid.toString().equalsIgnoreCase(CONTROLLER_SERVICE)) {
                            if (!mScanDeviceAddressList.contains(address)) {
                                Log.d(TAG, "scan device: " + device.getName());
                                mScanDeviceAddressList.add(address);
                                DeviceData deviceData = AppGlobal.updateScanDevice(address, name, rssi);
                                AppGlobal.setCurrentPairingDevice(deviceData);
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.ADD_DEVICE));
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    private class DeviceScanTimerTask extends TimerTask {
        @Override
        public void run() {
            AppGlobal.updateDeviceState(mScanDeviceAddressList);
            mScanDeviceAddressList.clear();
            scanBluetoothLeDevice();
        }
    }

    private void scanBluetoothLeDevice() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    final BluetoothLeScanner bluetoothLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                    if (bluetoothLEScanner != null) {
                        bluetoothLEScanner.startScan(mScanCallback);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (BluetoothChecker.isBluetoothSupported() && BluetoothChecker.isBluetoothEnabled())
                                    bluetoothLEScanner.stopScan(mScanCallback);
                            }
                        }, 5 * 1000);
                    }
                }
            });
        }
    }

    private class ConnectTimerTask extends TimerTask {
        @Override
        public void run() {
            DeviceData currentDevice = AppGlobal.getCurrentDevice();
            if (currentDevice == null) {
                DeviceData deviceData = AppGlobal.getFirstScanPairedDevice();
                if (deviceData != null) {
                    AppGlobal.setCurrentDevice(deviceData);
                    AppGlobal.connect(deviceData);
                }
            }
            else if (currentDevice.deviceState == DeviceState.ONLINE) {
                AppGlobal.connect(currentDevice);
            }
            for (String address: AppGlobal.getGattMap().keySet()) {
                BluetoothGatt gatt = AppGlobal.getBluetoothGatt(address);
                if (gatt != null) {
                    gatt.readRemoteRssi();
                }
            }
        }
    }

    public void writeCharacteristic(final BluetoothGatt bluetoothGatt, String service, final byte[] value) {
        if (bluetoothGatt == null)
            return;
        BluetoothGattService gattService = bluetoothGatt.getService(UUID.fromString(CONTROLLER_SERVICE));
        if (gattService == null)
            return;
        BluetoothGattCharacteristic gattChar = gattService.getCharacteristic(UUID.fromString(service));
        if (gattChar == null)
            return;
        gattChar.setValue(value);
        boolean result = bluetoothGatt.writeCharacteristic(gattChar);
        if (result) {
            Log.d(TAG, "write char: " + bluetoothGatt.getDevice().getName() + ", " + convertBytesToStr(value));
        } else {
            Log.e(TAG, "write char fail: " + bluetoothGatt.getDevice().getName() + ", " + convertBytesToStr(value));
        }
    }

    public void readCharacteristic(BluetoothGatt bluetoothGatt, String service) {
        if (bluetoothGatt == null)
            return;
        BluetoothGattService gattService = bluetoothGatt.getService(UUID.fromString(CONTROLLER_SERVICE));
        if (gattService == null)
            return;
        BluetoothGattCharacteristic gattChar = gattService.getCharacteristic(UUID.fromString(service));
        if (gattChar == null)
            return;
        bluetoothGatt.setCharacteristicNotification(gattChar, true);
        BluetoothGattDescriptor descriptor = gattChar.getDescriptors().get(0);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean result = bluetoothGatt.readCharacteristic(gattChar);
                if (result) {
//                    Log.d(TAG, "read char: " + bluetoothGatt.getDevice().getName());
                } else {
                    Log.e(TAG, "read char fail: " + bluetoothGatt.getDevice().getName());
                }
            }
        }, 200);

    }


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String address = gatt.getDevice().getAddress();
            DeviceData deviceData = AppGlobal.getConnectedDevice(address);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "Device connected: " + deviceData.systemData.name);
                deviceData.deviceState = DeviceState.CONNECTED;
                gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                AppGlobal.setBluetoothGatt(address, gatt);
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Device disconnected: " + deviceData.systemData.name);
                deviceData.deviceState = DeviceState.DISCONNECTED;
                gatt.close();
                AppGlobal.setBluetoothGatt(address, null);
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "service discovered");
                String address = gatt.getDevice().getAddress();
                readDeviceInfo(address);
            } else {
                Log.e(TAG, "onServicesDiscovered error: " + status);
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            String address = gatt.getDevice().getAddress();
            DeviceData deviceData = AppGlobal.getConnectedDevice(address);
            if (deviceData != null) {
                deviceData.addRssi(rssi);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            final byte[] data = characteristic.getValue();
            String service = characteristic.getService().getUuid().toString();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (data != null && data.length > 0) {
                    Log.d(TAG, "Write done: " + gatt.getDevice().getName() + ", " + convertBytesToStr(data));
                } else {
                    Log.e(TAG, "Write empty: " + gatt.getDevice().getName());
                }
            } else {
                Log.e(TAG, "Write fail: " + gatt.getDevice().getName());
            }
        }

        @Override
        public void onCharacteristicRead(final BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            String address = gatt.getDevice().getAddress();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                final byte[] data = characteristic.getValue();
                if (data != null && data.length > 0) {

                    DeviceData deviceData = AppGlobal.getConnectedDevice(address);
                    if (deviceData != null) {
                        if (characteristic.getUuid().toString().equalsIgnoreCase(DEVICE_SETTING)) {
                            deviceData.deviceSettingsBytes = data;
                            Log.d(TAG, "read device info: " + Helper.print(data));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(CHANNEL_STATUS)) {
                            deviceData.channelBytes = data;
                            Log.d(TAG, "read channel: " + Helper.print(data));
                            for (int i = 0; i < deviceData.channels.length; i++) {
                                deviceData.channels[i].maxCurrent = (int) (data[i * 2 + 1] * 0.2f);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            String address = gatt.getDevice().getAddress();
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                DeviceData deviceData = AppGlobal.getConnectedDevice(address);
                if (deviceData != null) {
                    if (characteristic.getUuid().toString().equalsIgnoreCase(DEVICE_SETTING)) {
                        Log.d(TAG, "update device info: " + Helper.print(data));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(CHANNEL_STATUS)) {
                        deviceData.channelBytes = data;
                        Log.d(TAG, "update channel: " + Helper.print(data));
                    }
                }
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "get notification.");
            }
        }
    };

    public String convertBytesToStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for(int i = 0; i < b.length; i++) {
            String str1 = Integer.toString(b[i] >> 4 & 0x0f, 16);
            String str2 = Integer.toString(b[i] & 0x0f, 16);
            result.append(str1 + str2 + " ");
        }
        return result.toString().substring(0, result.length() - 1);
    }

    private void readDeviceInfo(String address) {
        DeviceData deviceData = AppGlobal.getDevice(address);
        if (deviceData != null) {
            deviceData.deviceState = DeviceState.INITIALIZING;
            BluetoothGatt gatt = AppGlobal.getBluetoothGatt(address);
            readCharacteristic(gatt, CHANNEL_STATUS);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    readCharacteristic(gatt, DEVICE_SETTING);
                }
            }, 500);
        }
    }

    public void writeDeviceSettings(DeviceData deviceData) {
        BluetoothGatt gatt = AppGlobal.getBluetoothGatt(deviceData.address);
        writeCharacteristic(gatt, DEVICE_SETTING, deviceData.deviceSettingsBytes);
    }

    public void writeChannel(DeviceData deviceData) {
        BluetoothGatt gatt = AppGlobal.getBluetoothGatt(deviceData.address);
        writeCharacteristic(gatt, CHANNEL_STATUS, deviceData.channelBytes);
    }
}
