package com.xkglow.xkcommand.Helper;

import android.content.Context;
import android.util.Log;

import com.xkglow.xkcommand.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class AppGlobal {
    private static Context context;
    private static HashMap<String, Object> hashMap;
    private static SensorData[] sensors;
    private static ButtonData[] buttons;
    private static ChannelData[] channels;
    private static SystemData systemData;
    private static boolean powerOn;

    public static int loaderId;

    public static String FILE_INFO = "fileinfo";

    public static void init(Context contextGlobal) {
        context = contextGlobal;
        hashMap = new HashMap<>();
        if (!loadInfo()) {
            initFirstTime();
        }
        powerOn = false;
        loaderId = 1;
    }

    private static void initFirstTime() {
        sensors = new SensorData[3];
        sensors[0] = new SensorData(1);
        sensors[1] = new SensorData(2);
        sensors[2] = new SensorData(3);

        buttons = new ButtonData[8];
        buttons[0] = new ButtonData(1);
        buttons[1] = new ButtonData(2);
        buttons[2] = new ButtonData(3);
        buttons[3] = new ButtonData(4);
        buttons[4] = new ButtonData(5);
        buttons[5] = new ButtonData(6);
        buttons[6] = new ButtonData(7);
        buttons[7] = new ButtonData(8);

        channels = new ChannelData[8];
        channels[0] = new ChannelData(1);
        channels[1] = new ChannelData(2);
        channels[2] = new ChannelData(3);
        channels[3] = new ChannelData(4);
        channels[4] = new ChannelData(5);
        channels[5] = new ChannelData(6);
        channels[6] = new ChannelData(7);
        channels[7] = new ChannelData(8);

        systemData = new SystemData();

        saveInfo();
    }

    private static void saveInfo() {
        hashMap.put("button", buttons);
        hashMap.put("sensor", sensors);
        hashMap.put("channel", channels);
        hashMap.put("system", systemData);
        try {
            FileOutputStream fos = context.openFileOutput(FILE_INFO, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(hashMap);
            os.close();
            fos.close();
            Log.e("YAY", "save info done!!!");
        } catch (Exception e) {
            Log.e("ERROR", "save info error!");
        }
    }

    private static boolean loadInfo() {
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
            hashMap = (HashMap<String, Object>) obj;
            is.close();
            fis.close();
            Log.e("YAY", "load info done!!!");
            buttons = (ButtonData[]) hashMap.get("button");
            for (ButtonData buttonData : buttons) {
                buttonData.isPressed = false;
            }
            sensors = (SensorData[]) hashMap.get("sensor");
            channels = (ChannelData[]) hashMap.get("channel");
            systemData = (SystemData) hashMap.get("system");
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

    public static void setSensor(SensorData sensorData) {
        for (SensorData sensorData1 : sensors) {
            if (sensorData1.id == sensorData.id) {
                sensorData1.actions = sensorData.actions;
                sensorData1.channels = sensorData.channels;
                sensorData1.function = sensorData.function;
                sensorData1.brightness = sensorData.brightness;
                sensorData1.name = sensorData.name;
            }
        }
        saveInfo();
    }

    public static SensorData[] getSensors() {
        return sensors;
    }

    public static SensorData getSensor(int id) {
        for (SensorData sensorData : sensors) {
            if (sensorData.id == id) {
                return sensorData;
            }
        }
        return null;
    }

    public static boolean hasDim() {
        for (SensorData sensorData : sensors) {
            if (sensorData.function == 0) {
                return true;
            }
        }
        return false;
    }

    public static ButtonData[] getButtons() {
        return buttons;
    }

    public static ButtonData getButton(int id) {
        for (ButtonData buttonData : buttons) {
            if (buttonData.id == id) {
                return buttonData;
            }
        }
        return null;
    }

    public static void setButton(ButtonData buttonData) {
        powerOn = false;
        for (ButtonData buttonData1 : buttons) {
            if (buttonData.id == buttonData1.id) {
                buttonData1.type = buttonData.type;
                buttonData1.channels = buttonData.channels;
                buttonData1.actions = buttonData.actions;
                buttonData1.sync = buttonData.sync;
                buttonData1.momentary = buttonData.momentary;
                buttonData1.iconResourceId = buttonData.iconResourceId;
                buttonData1.text = buttonData.text;
                buttonData1.imagePath = buttonData.imagePath;
            }
            if (buttonData1.isPressed) {
                powerOn = true;
            }
        }
        saveInfo();
    }

    public static SystemData getSystemData() {
        return systemData;
    }

    public static void setButtonColor(int color) {
        systemData.buttonColor = color;
        saveInfo();
    }

    public static void setWarningButtonColor(int color) {
        systemData.buttonWarningColor = color;
        saveInfo();
    }

    public static DeviceData getCurrentPairingDevice() {
        return null;
    }

    public static void setPowerOn(boolean isPowerOn) {
        powerOn = isPowerOn;
    }

    public static boolean isPowerOn() {
        return powerOn;
    }

    public static ChannelData[] getChannels() {
        return channels;
    }

    public static ChannelData getChannel(int id) {
        for (ChannelData channelData : channels) {
            if (channelData.id == id) {
                return channelData;
            }
        }
        return null;
    }

    public static void setChannel(ChannelData channelData) {
        for (ChannelData channelData1 : channels) {
            if (channelData1.id == channelData.id) {
                channelData1.name = channelData.name;
                channelData1.maxCurrent = channelData.maxCurrent;
            }
        }
        saveInfo();
    }

    public static void setSystem(SystemData systemData1) {
        systemData = systemData1;
        saveInfo();
    }
}
