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

        deviceState = DeviceState.OFFLINE;
    }

    public void setSensor(SensorData sensorData) {
        for (SensorData sensorData1 : sensors) {
            if (sensorData1.id == sensorData.id) {
                sensorData1.actions = sensorData.actions;
                sensorData1.channels = sensorData.channels;
                sensorData1.function = sensorData.function;
                sensorData1.brightness = sensorData.brightness;
                sensorData1.name = sensorData.name;
                sensorData1.sync = sensorData.sync;
                if (sensorData1.sync) {
                    int action = 0;
                    for (int i = 0; i < sensorData1.channels.length; i++) {
                        if (sensorData1.channels[i]) {
                            if (action == 0) {
                                action = sensorData1.actions[i];
                            } else {
                                sensorData1.actions[i] = action;
                            }
                        }
                    }
                }
                sensorData1.sensorBytes[0] = Helper.setBit(sensorData1.sensorBytes[0], 7, 1);
                sensorData1.sensorBytes[0] = Helper.setBit(sensorData1.sensorBytes[0], 6, 1 - sensorData1.function);
                sensorData1.sensorBytes[0] = Helper.setBit(sensorData1.sensorBytes[0], 5, 1);
                sensorData1.sensorBytes[0] = Helper.setBit(sensorData1.sensorBytes[0], 4, 1);
                for (int i = 0; i < sensorData1.channels.length; i++) {
                    if (!sensorData1.channels[i]) {
                        sensorData1.sensorBytes[i + 1] = (byte) 0;
                    } else {
                        sensorData1.sensorBytes[i + 1] = (byte) sensorData1.actions[i];
                    }
                }
            }
        }
        AppGlobal.savPairedDevices();
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
        for (ButtonData buttonData1 : buttons) {
            if (buttonData.id == buttonData1.id) {
                buttonData1.type = buttonData.type;
                buttonData1.channels = buttonData.channels;
                buttonData1.sync = buttonData.sync;
                if (buttonData1.sync) {
                    int action = 0;
                    for (int i = 0; i < buttonData1.channels.length; i++) {
                        if (buttonData1.channels[i]) {
                            if (action == 0) {
                                action = buttonData1.actions[i];
                            } else {
                                buttonData1.actions[i] = action;
                            }
                        }
                    }
                }
                buttonData1.momentary = buttonData.momentary;
                buttonData1.iconResourceId = buttonData.iconResourceId;
                buttonData1.text = buttonData.text;
                buttonData1.buttonBytes[0] = Helper.setBit(buttonData1.buttonBytes[0], 7, 1);
                buttonData1.buttonBytes[0] = Helper.setBit(buttonData1.buttonBytes[0], 6, 0);
                buttonData1.buttonBytes[0] = Helper.setBit(buttonData1.buttonBytes[0], 5, 1);
                if (buttonData1.momentary) {
                    buttonData1.buttonBytes[0] = Helper.setBit(buttonData1.buttonBytes[0], 4, 1);
                } else {
                    buttonData1.buttonBytes[0] = Helper.setBit(buttonData1.buttonBytes[0], 4, 0);
                }
                for (int i = 0; i < buttonData1.channels.length; i++) {
                    if (!buttonData1.channels[i]) {
                        buttonData1.buttonBytes[i + 1] = (byte) 0;
                    } else {
                        buttonData1.buttonBytes[i + 1] = (byte) buttonData1.actions[i];
                    }
                }
            }
        }
        AppGlobal.savPairedDevices();
    }

    public SystemData getSystemData() {
        return systemData;
    }

    public void setButtonColor(int color) {
        systemData.buttonColor = color;
        AppGlobal.savPairedDevices();
    }

    public void setWarningButtonColor(int color) {
        systemData.buttonWarningColor = color;
        AppGlobal.savPairedDevices();
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
                channelData1.maxAmp = channelData.maxAmp;
            }
        }
        AppGlobal.savPairedDevices();
    }

    public void setSystem(SystemData systemData1) {
        systemData = systemData1;
        AppGlobal.savPairedDevices();
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

    public boolean isPowerOn() {
        for (ButtonData buttonData : buttons) {
            if (buttonData.isPressed && !buttonData.momentary) {
                return true;
            }
        }
        return false;
    }
}
