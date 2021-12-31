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
import android.os.ParcelUuid;
import android.util.Log;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.BluetoothData;
import com.xkglow.xkcommand.Helper.BluetoothDataType;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.DeviceData;
import com.xkglow.xkcommand.Helper.DeviceState;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.Helper.SensorData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

public class BluetoothService extends Service {
    private BluetoothAdapter mBluetoothAdapter;
    private Timer mDeviceScanTimer;
    private DeviceScanTimerTask mDeviceScanTimerTask;
    private Handler mHandler;
    private Timer mConnectTimer;
    private ConnectTimerTask mConnectTimerTask;
    private ArrayList<String> mScanDeviceAddressList;
    private LinkedBlockingDeque<BluetoothData> mWaitingList;

    public static final String CONTROLLER_SERVICE = "02A8AF3E-C199-4735-BACE-FA8E9F74803E";
    public static final String DEVICE_INFO = "02A8AF3E-C001-4735-BACE-FA8E9F74803E";
    public static final String USER_SETTINGS = "02A8AF3E-C002-4735-BACE-FA8E9F74803E";
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

    public static final String TAG = "BleService";

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
        mWaitingList = new LinkedBlockingDeque<>();
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
                                AppGlobal.updateScanDevice(address, name, rssi);
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
            Log.d(TAG, "write char: " + service + ", " + Helper.convertBytesToBitsString(value));
        } else {
            Log.e(TAG, "write char fail: " + service + ", " + Helper.convertBytesToBitsString(value));
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
        boolean result = bluetoothGatt.readCharacteristic(gattChar);
        if (result) {
            Log.d(TAG, "read char: " + service);
        } else {
            Log.e(TAG, "read char fail: " + service);
        }
    }

    private void writeDescriptor(BluetoothGatt bluetoothGatt, String service) {
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
        boolean result = bluetoothGatt.writeDescriptor(descriptor);
        if (result) {
            Log.d(TAG, "set notification: " + service);
        } else {
            Log.e(TAG, "set notification fail: " + service);
        }
    }


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String address = gatt.getDevice().getAddress();
            DeviceData deviceData = AppGlobal.getDevice(address);
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
            DeviceData deviceData = AppGlobal.getDevice(address);
            if (deviceData != null) {
                deviceData.addRssi(rssi);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            final byte[] data = characteristic.getValue();
            String service = characteristic.getUuid().toString().toUpperCase();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (data != null && data.length > 0) {
                    Log.d(TAG, "Write char done: " + service + ", " + Helper.convertBytesToBitsString(data));
                } else {
                    Log.e(TAG, "Write char empty: " + service);
                }
            } else {
                Log.e(TAG, "Write char fail: " + service);
            }
            sendNext();
        }

        @Override
        public void onCharacteristicRead(final BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            String address = gatt.getDevice().getAddress();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                final byte[] data = characteristic.getValue();
                if (data != null && data.length > 0) {
                    DeviceData deviceData = AppGlobal.getDevice(address);
                    if (deviceData != null) {
                        if (characteristic.getUuid().toString().equalsIgnoreCase(DEVICE_INFO)) {
                            deviceData.deviceState = DeviceState.READY;
                            deviceData.deviceInfoBytes = data;
                            Log.d(TAG, "read device info: " + Helper.convertBytesToBitsString(data));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(USER_SETTINGS)) {
                            deviceData.userSettingsBytes = data;
                            deviceData.systemData.buttonColor = Helper.getRGB(data[4], data[5], data[6]);
                            deviceData.systemData.buttonWarningColor = Helper.getRGB(data[7], data[8], data[9]);
                            deviceData.systemData.turnBluetoothOffAfter = data[3];
                            if (deviceData.systemData.turnBluetoothOffAfter == 0) {
                                deviceData.systemData.bluetoothAutoOff = false;
                                deviceData.systemData.turnBluetoothOffAfter = 1;
                            }
                            deviceData.systemData.cutoffInput = data[0] * 0.2f;
                            if (deviceData.systemData.cutoffInput < 10.8f || deviceData.systemData.cutoffInput > 13.2f) {
                                deviceData.systemData.cutoffInput = 12f;
                            }
                            Log.d(TAG, "read user settings: " + Helper.convertBytesToBitsString(data));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(CHANNEL_STATUS)) {
                            deviceData.channelBytes = data;
                            Log.d(TAG, "read channel: " + Helper.convertBytesToBitsString(data));
                            for (int i = 0; i < deviceData.channels.length; i++) {
                                deviceData.channels[i].amp = (int) (data[i * 2] * 0.2f);
                                deviceData.channels[i].maxAmp = (int) (data[i * 2 + 1] * 0.2f);
                            }
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_1_STATUS)) {
                            deviceData.buttons[0].buttonBytes = data;
                            Log.d(TAG, "read button 1: " + Helper.convertBytesToBitsString(data));
                            setButton(deviceData.buttons[0], data);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_2_STATUS)) {
                            deviceData.buttons[1].buttonBytes = data;
                            Log.d(TAG, "read button 2: " + Helper.convertBytesToBitsString(data));
                            setButton(deviceData.buttons[1], data);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_3_STATUS)) {
                            deviceData.buttons[2].buttonBytes = data;
                            Log.d(TAG, "read button 3: " + Helper.convertBytesToBitsString(data));
                            setButton(deviceData.buttons[2], data);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_4_STATUS)) {
                            deviceData.buttons[3].buttonBytes = data;
                            Log.d(TAG, "read button 4: " + Helper.convertBytesToBitsString(data));
                            setButton(deviceData.buttons[3], data);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_5_STATUS)) {
                            deviceData.buttons[4].buttonBytes = data;
                            Log.d(TAG, "read button 5: " + Helper.convertBytesToBitsString(data));
                            setButton(deviceData.buttons[4], data);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_6_STATUS)) {
                            deviceData.buttons[5].buttonBytes = data;
                            Log.d(TAG, "read button 6: " + Helper.convertBytesToBitsString(data));
                            setButton(deviceData.buttons[5], data);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_7_STATUS)) {
                            deviceData.buttons[6].buttonBytes = data;
                            Log.d(TAG, "read button 7: " + Helper.convertBytesToBitsString(data));
                            setButton(deviceData.buttons[6], data);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_8_STATUS)) {
                            deviceData.buttons[7].buttonBytes = data;
                            Log.d(TAG, "read button 8: " + Helper.convertBytesToBitsString(data));
                            setButton(deviceData.buttons[7], data);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(SENSOR_1_STATUS)) {
                            deviceData.sensors[0].sensorBytes = data;
                            Log.d(TAG, "read sensor 1: " + Helper.convertBytesToBitsString(data));
                            setSensor(deviceData.sensors[0], data, deviceData.userSettingsBytes[2] & 0xff);
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(SENSOR_2_STATUS)) {
                            deviceData.sensors[1].sensorBytes = data;
                            Log.d(TAG, "read sensor 2: " + Helper.convertBytesToBitsString(data));
                            setSensor(deviceData.sensors[1], data, deviceData.userSettingsBytes[2] & 0xff);
                        }
                        if (characteristic.getUuid().toString().equalsIgnoreCase(SENSOR_3_STATUS)) {
                            deviceData.sensors[2].sensorBytes = data;
                            Log.d(TAG, "read sensor 3: " + Helper.convertBytesToBitsString(data));
                            setSensor(deviceData.sensors[2], data, deviceData.userSettingsBytes[2] & 0xff);
                        }
                    }
                }
            }
            sendNext();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            String address = gatt.getDevice().getAddress();
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                DeviceData deviceData = AppGlobal.getDevice(address);
                if (deviceData != null) {
                    if (characteristic.getUuid().toString().equalsIgnoreCase(DEVICE_INFO)) {
                        Log.d(TAG, "update device info: " + Helper.convertBytesToBitsString(data));
                        deviceData.deviceInfoBytes[4] = data[4];
                        deviceData.deviceInfoBytes[5] = data[5];
                        deviceData.deviceInfoBytes[6] = data[6];
                        deviceData.deviceInfoBytes[7] = data[7];
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(CHANNEL_STATUS)) {
                        Log.d(TAG, "update channel: " + Helper.convertBytesToBitsString(data));
                        for (int i = 0; i < deviceData.channels.length; i++) {
                            deviceData.channels[i].amp = data[i * 2];
                        }
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_1_STATUS)) {
                        deviceData.buttons[0].buttonBytes[0] = Helper.setBit(deviceData.buttons[0].buttonBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.buttons[0].buttonBytes[0] = Helper.setBit(deviceData.buttons[0].buttonBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update button 1: " + Helper.convertBytesToBitsString(data));
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_2_STATUS)) {
                        deviceData.buttons[1].buttonBytes[0] = Helper.setBit(deviceData.buttons[1].buttonBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.buttons[1].buttonBytes[0] = Helper.setBit(deviceData.buttons[1].buttonBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update button 2: " + Helper.convertBytesToBitsString(data));
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_3_STATUS)) {
                        deviceData.buttons[2].buttonBytes[0] = Helper.setBit(deviceData.buttons[2].buttonBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.buttons[2].buttonBytes[0] = Helper.setBit(deviceData.buttons[2].buttonBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update button 3: " + Helper.convertBytesToBitsString(data));
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_4_STATUS)) {
                        deviceData.buttons[3].buttonBytes[0] = Helper.setBit(deviceData.buttons[3].buttonBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.buttons[3].buttonBytes[0] = Helper.setBit(deviceData.buttons[3].buttonBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update button 4: " + Helper.convertBytesToBitsString(data));
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_5_STATUS)) {
                        deviceData.buttons[4].buttonBytes[0] = Helper.setBit(deviceData.buttons[4].buttonBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.buttons[4].buttonBytes[0] = Helper.setBit(deviceData.buttons[4].buttonBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update button 5: " + Helper.convertBytesToBitsString(data));
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_6_STATUS)) {
                        deviceData.buttons[5].buttonBytes[0] = Helper.setBit(deviceData.buttons[5].buttonBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.buttons[5].buttonBytes[0] = Helper.setBit(deviceData.buttons[5].buttonBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update button 6: " + Helper.convertBytesToBitsString(data));
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_7_STATUS)) {
                        deviceData.buttons[6].buttonBytes[0] = Helper.setBit(deviceData.buttons[6].buttonBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.buttons[6].buttonBytes[0] = Helper.setBit(deviceData.buttons[6].buttonBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update button 7: " + Helper.convertBytesToBitsString(data));
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(BUTTON_8_STATUS)) {
                        deviceData.buttons[7].buttonBytes[0] = Helper.setBit(deviceData.buttons[7].buttonBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.buttons[7].buttonBytes[0] = Helper.setBit(deviceData.buttons[7].buttonBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update button 8: " + Helper.convertBytesToBitsString(data));
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.UPDATE_DEVICE, address));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(SENSOR_1_STATUS)) {
                        deviceData.sensors[0].sensorBytes[0] = Helper.setBit(deviceData.sensors[0].sensorBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.sensors[0].sensorBytes[0] = Helper.setBit(deviceData.sensors[0].sensorBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update sensor 1: " + Helper.convertBytesToBitsString(data));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(SENSOR_2_STATUS)) {
                        deviceData.sensors[1].sensorBytes[0] = Helper.setBit(deviceData.sensors[1].sensorBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.sensors[1].sensorBytes[0] = Helper.setBit(deviceData.sensors[1].sensorBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update sensor 2: " + Helper.convertBytesToBitsString(data));
                    }
                    if (characteristic.getUuid().toString().equalsIgnoreCase(SENSOR_3_STATUS)) {
                        deviceData.sensors[2].sensorBytes[0] = Helper.setBit(deviceData.sensors[2].sensorBytes[0], 0, Helper.getBit(data[0], 0));
                        deviceData.sensors[2].sensorBytes[0] = Helper.setBit(deviceData.sensors[2].sensorBytes[0], 1, Helper.getBit(data[0], 1));
                        Log.d(TAG, "update sensor 3: " + Helper.convertBytesToBitsString(data));
                    }
                }
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "get notification.");
            } else {
                Log.e(TAG, "get notification error: " + status + ".");
            }
            sendNext();
        }
    };

    private void readDeviceInfo(String address) {
        DeviceData deviceData = AppGlobal.getDevice(address);
        if (deviceData != null) {
            deviceData.deviceState = DeviceState.INITIALIZING;
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, DEVICE_INFO, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, DEVICE_INFO, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, USER_SETTINGS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, CHANNEL_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, CHANNEL_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, BUTTON_1_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, BUTTON_1_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, BUTTON_2_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, BUTTON_2_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, BUTTON_3_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, BUTTON_3_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, BUTTON_4_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, BUTTON_4_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, BUTTON_5_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, BUTTON_5_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, BUTTON_6_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, BUTTON_6_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, BUTTON_7_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, BUTTON_7_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, BUTTON_8_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, BUTTON_8_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, SENSOR_1_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, SENSOR_1_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, SENSOR_2_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, SENSOR_2_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Read_Char, deviceData.address, SENSOR_3_STATUS, null));
            mWaitingList.add(new BluetoothData(BluetoothDataType.Write_Descriptor, deviceData.address, SENSOR_3_STATUS, null));

            sendNext();
        }
    }

    public void writeUserSettings(DeviceData deviceData) {
        BluetoothData bluetoothData = new BluetoothData(BluetoothDataType.Write_Char, deviceData.address, USER_SETTINGS, deviceData.userSettingsBytes);
        mWaitingList.add(bluetoothData);
        sendNext();
    }

    public void writeChannel(DeviceData deviceData) {
        BluetoothData bluetoothData = new BluetoothData(BluetoothDataType.Write_Char, deviceData.address, CHANNEL_STATUS, deviceData.channelBytes);
        mWaitingList.add(bluetoothData);
        sendNext();
    }

    public void writeButton(DeviceData deviceData, ButtonData buttonData) {
        String service = BUTTON_1_STATUS;
        switch (buttonData.id) {
            case 1:
                service = BUTTON_1_STATUS;
                break;
            case 2:
                service = BUTTON_2_STATUS;
                break;
            case 3:
                service = BUTTON_3_STATUS;
                break;
            case 4:
                service = BUTTON_4_STATUS;
                break;
            case 5:
                service = BUTTON_5_STATUS;
                break;
            case 6:
                service = BUTTON_6_STATUS;
                break;
            case 7:
                service = BUTTON_7_STATUS;
                break;
            case 8:
                service = BUTTON_8_STATUS;
                break;
        }
        BluetoothData bluetoothData = new BluetoothData(BluetoothDataType.Write_Char, deviceData.address, service, buttonData.buttonBytes);
        mWaitingList.add(bluetoothData);
        sendNext();
    }

    public void writeSensor(DeviceData deviceData, SensorData sensorData) {
        String service = SENSOR_1_STATUS;
        switch (sensorData.id) {
            case 1:
                service = SENSOR_1_STATUS;
                break;
            case 2:
                service = SENSOR_2_STATUS;
                break;
            case 3:
                service = SENSOR_3_STATUS;
                break;
        }
        if (sensorData.function == 0) {
            deviceData.userSettingsBytes[2] = (byte) (sensorData.brightness * 255 / 100);
            BluetoothData bluetoothData2 = new BluetoothData(BluetoothDataType.Write_Char, deviceData.address, USER_SETTINGS, deviceData.userSettingsBytes);
            mWaitingList.add(bluetoothData2);
        }
        BluetoothData bluetoothData = new BluetoothData(BluetoothDataType.Write_Char, deviceData.address, service, sensorData.sensorBytes);
        mWaitingList.add(bluetoothData);
        sendNext();
    }

    public void writeAllButtons(DeviceData deviceData) {
        String service = BUTTON_1_STATUS;
        for (int i = 0; i < 8; i++) {
            ButtonData buttonData = deviceData.buttons[i];
            switch (buttonData.id) {
                case 1:
                    service = BUTTON_1_STATUS;
                    break;
                case 2:
                    service = BUTTON_2_STATUS;
                    break;
                case 3:
                    service = BUTTON_3_STATUS;
                    break;
                case 4:
                    service = BUTTON_4_STATUS;
                    break;
                case 5:
                    service = BUTTON_5_STATUS;
                    break;
                case 6:
                    service = BUTTON_6_STATUS;
                    break;
                case 7:
                    service = BUTTON_7_STATUS;
                    break;
                case 8:
                    service = BUTTON_8_STATUS;
                    break;
            }
            BluetoothData bluetoothData = new BluetoothData(BluetoothDataType.Write_Char, deviceData.address, service, buttonData.buttonBytes);
            mWaitingList.add(bluetoothData);
        }
        sendNext();
    }

    private void sendNext() {
        BluetoothData bluetoothData = mWaitingList.poll();
        if (bluetoothData != null) {
            BluetoothGatt gatt = AppGlobal.getBluetoothGatt(bluetoothData.address);
            switch (bluetoothData.type) {
                case Write_Char:
                    writeCharacteristic(gatt, bluetoothData.service, bluetoothData.data);
                    break;
                case Read_Char:
                    readCharacteristic(gatt, bluetoothData.service);
                    break;
                case Write_Descriptor:
                    writeDescriptor(gatt, bluetoothData.service);
            }
        }
    }

    private void setButton(ButtonData buttonData, byte[] data) {
        buttonData.momentary = Helper.getBit(data[0], 4) == 1;
        if (data[1] == 0) {
            buttonData.channels[0] = false;
        } else {
            buttonData.channels[0] = true;
            buttonData.actions[0] = data[1] & 0xff;
        }
        if (data[2] == 0) {
            buttonData.channels[1] = false;
        } else {
            buttonData.channels[1] = true;
            buttonData.actions[1] = data[2] & 0xff;
        }
        if (data[3] == 0) {
            buttonData.channels[2] = false;
        } else {
            buttonData.channels[2] = true;
            buttonData.actions[2] = data[3] & 0xff;
        }
        if (data[4] == 0) {
            buttonData.channels[3] = false;
        } else {
            buttonData.channels[3] = true;
            buttonData.actions[3] = data[4] & 0xff;
        }
        if (data[5] == 0) {
            buttonData.channels[4] = false;
        } else {
            buttonData.channels[4] = true;
            buttonData.actions[4] = data[5] & 0xff;
        }
        if (data[6] == 0) {
            buttonData.channels[5] = false;
        } else {
            buttonData.channels[5] = true;
            buttonData.actions[5] = data[6] & 0xff;
        }
        if (data[7] == 0) {
            buttonData.channels[6] = false;
        } else {
            buttonData.channels[6] = true;
            buttonData.actions[6] = data[7] & 0xff;
        }
        if (data[8] == 0) {
            buttonData.channels[7] = false;
        } else {
            buttonData.channels[7] = true;
            buttonData.actions[7] = data[8] & 0xff;
        }
    }

    private void setSensor(SensorData sensorData, byte[] data, int brightness) {
        int b = (int) (100 * brightness / 255f) / 5 * 5;
        sensorData.brightness = Math.min(Math.max(20, b), 100);
        sensorData.function = 1 - Helper.getBit(data[0], 6);
        if (data[1] == 0) {
            sensorData.channels[0] = false;
        } else {
            sensorData.channels[0] = true;
            sensorData.actions[0] = data[1] & 0xff;
        }
        if (data[2] == 0) {
            sensorData.channels[1] = false;
        } else {
            sensorData.channels[1] = true;
            sensorData.actions[1] = data[2] & 0xff;
        }
        if (data[3] == 0) {
            sensorData.channels[2] = false;
        } else {
            sensorData.channels[2] = true;
            sensorData.actions[2] = data[3] & 0xff;
        }
        if (data[4] == 0) {
            sensorData.channels[3] = false;
        } else {
            sensorData.channels[3] = true;
            sensorData.actions[3] = data[4] & 0xff;
        }
        if (data[5] == 0) {
            sensorData.channels[4] = false;
        } else {
            sensorData.channels[4] = true;
            sensorData.actions[4] = data[5] & 0xff;
        }
        if (data[6] == 0) {
            sensorData.channels[5] = false;
        } else {
            sensorData.channels[5] = true;
            sensorData.actions[5] = data[6] & 0xff;
        }
        if (data[7] == 0) {
            sensorData.channels[6] = false;
        } else {
            sensorData.channels[6] = true;
            sensorData.actions[6] = data[7] & 0xff;
        }
        if (data[8] == 0) {
            sensorData.channels[7] = false;
        } else {
            sensorData.channels[7] = true;
            sensorData.actions[7] = data[8] & 0xff;
        }
    }
}
