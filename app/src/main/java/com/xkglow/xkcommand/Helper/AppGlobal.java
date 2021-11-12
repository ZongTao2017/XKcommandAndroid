package com.xkglow.xkcommand.Helper;

import android.content.Context;
import android.util.Log;

import com.xkglow.xkcommand.R;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AppGlobal {
    private static Context context;
    private static ArrayList<DeviceData> devices;
    private static DeviceData currentDevice;

    public static int loaderId;

    public static String FILE_INFO = "fileinfo";

    public static void init(Context contextGlobal) {
        context = contextGlobal;
        if (!loadInfo()) {
            initFirstTime();
        }
        loaderId = 1;
        currentDevice = devices.get(0);
    }

    private static void initFirstTime() {
        devices = new ArrayList<>();

        DeviceData deviceData1 = new DeviceData("1", "XKGLOW_1111");
        devices.add(deviceData1);
        DeviceData deviceData2 = new DeviceData("2", "XKGLOW_2222");
        devices.add(deviceData2);

        saveInfo();
    }

    public static void saveInfo() {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_INFO, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(devices);
            os.close();
            fos.close();
            Log.e("YAY", "save info done!!!");
        } catch (Exception e) {
            Log.e("ERROR", "save info error!");
        }
    }

    public static boolean loadInfo() {
        try {
            if (context == null) {
                Log.e("ERROR", "load info failed!");
                return false;
            }

            FileInputStream fis = context.openFileInput(FILE_INFO);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object obj = is.readObject();
            if (obj == null) {
                Log.e("ERROR", "load info error!");
                return false;
            }
            devices = (ArrayList<DeviceData>) obj;
            is.close();
            fis.close();
            Log.e("YAY", "load info done!!!");
            return true;
        } catch (Exception e) {
            Log.e("ERROR", "load info error!");
        }
        return false;
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

    public static DeviceData[] getDevices() {
        return null;
    }

    public static DeviceData getCurrentDevice() {
        return currentDevice;
    }

    public static DeviceData getCurrentPairingDevice() {
        return null;
    }

    public static void setCurrentDevice(int index) {
        if (index >= 0 && index <= devices.size() - 1) {
            currentDevice = devices.get(index);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.CHANGE_DEVICE));
        }
    }

    public static DeviceData getDevice(int index) {
        if (index >= 0 && index <= devices.size() - 1) {
            return devices.get(index);
        }
        return null;
    }
}
