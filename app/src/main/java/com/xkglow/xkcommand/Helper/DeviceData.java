package com.xkglow.xkcommand.Helper;

import java.io.Serializable;
import java.util.ArrayList;

public class DeviceData implements Serializable {
    public String address;
    private boolean isAddingRssi = false;
    public ArrayList<Integer> rssiList;
    public int signalPercent;
    public DeviceState deviceState;
    public ButtonData[] buttons;
    public SensorData[] sensors;
    public ChannelData[] channels;
    public SystemData systemData;
    public boolean powerOn;

    public byte[] deviceSettingsBytes;
    public byte[] channelBytes;

    public DeviceData(String address, String name) {
        this.address = address;

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
        systemData.name = name;

        rssiList = new ArrayList<>();

        deviceSettingsBytes = new byte[20];
        channelBytes = new byte[16];
    }

    public void setSensor(SensorData sensorData) {
        for (SensorData sensorData1 : sensors) {
            if (sensorData1.id == sensorData.id) {
                sensorData1.actions = sensorData.actions;
                sensorData1.channels = sensorData.channels;
                sensorData1.function = sensorData.function;
                sensorData1.brightness = sensorData.brightness;
                sensorData1.name = sensorData.name;
            }
        }
        AppGlobal.saveInfo();
    }

    public SensorData[] getSensors() {
        return sensors;
    }

    public SensorData getSensor(int id) {
        for (SensorData sensorData : sensors) {
            if (sensorData.id == id) {
                return sensorData;
            }
        }
        return null;
    }

    public boolean hasDim() {
        for (SensorData sensorData : sensors) {
            if (sensorData.function == 0) {
                return true;
            }
        }
        return false;
    }

    public ButtonData getButton(int id) {
        for (ButtonData buttonData : buttons) {
            if (buttonData.id == id) {
                return buttonData;
            }
        }
        return null;
    }

    public void setButton(ButtonData buttonData) {
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
        AppGlobal.saveInfo();
    }

    public SystemData getSystemData() {
        return systemData;
    }

    public void setButtonColor(int color) {
        systemData.buttonColor = color;
        AppGlobal.saveInfo();
    }

    public void setWarningButtonColor(int color) {
        systemData.buttonWarningColor = color;
        AppGlobal.saveInfo();
    }

    public void setPowerOn(boolean isPowerOn) {
        powerOn = isPowerOn;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public ChannelData[] getChannels() {
        return channels;
    }

    public ChannelData getChannel(int id) {
        for (ChannelData channelData : channels) {
            if (channelData.id == id) {
                return channelData;
            }
        }
        return null;
    }

    public void setChannel(ChannelData channelData) {
        for (ChannelData channelData1 : channels) {
            if (channelData1.id == channelData.id) {
                channelData1.name = channelData.name;
                channelData1.maxCurrent = channelData.maxCurrent;
            }
        }
        AppGlobal.saveInfo();
    }

    public void setSystem(SystemData systemData1) {
        systemData = systemData1;
        AppGlobal.saveInfo();
    }

    public void addRssi(int rssi) {
        if (isAddingRssi) {
            return;
        }
        isAddingRssi = true;
        if (rssiList.size() == 5) {
            rssiList.remove((int) 0);
        }
        rssiList.add(rssi);
        int total = 0;
        for (float rssiVal : rssiList) {
            total += rssiVal;
        }
        int avg = total / rssiList.size();
        signalPercent = Math.min(100, 100 - ((Math.abs(avg) - 70) * 100 / 45));
        isAddingRssi = false;
    }
}
