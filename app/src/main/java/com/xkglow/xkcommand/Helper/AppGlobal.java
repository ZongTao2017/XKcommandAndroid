package com.xkglow.xkcommand.Helper;

public class AppGlobal {
    private static SensorData[] sensors;
    private static boolean hasDim;
    private static ButtonData[] buttons;
    public static int loaderId;

    public static void init() {
        sensors = new SensorData[3];
        sensors[0] = new SensorData("Sensor 1", "Sensor Name 1");
        sensors[1] = new SensorData("Sensor 2", "Sensor Name 2");
        sensors[2] = new SensorData("Sensor 3", "Sensor Name 3");
        hasDim = false;

        buttons = new ButtonData[8];
        buttons[0] = new ButtonData(1);
        buttons[1] = new ButtonData(2);
        buttons[2] = new ButtonData(3);
        buttons[3] = new ButtonData(4);
        buttons[4] = new ButtonData(5);
        buttons[5] = new ButtonData(6);
        buttons[6] = new ButtonData(7);
        buttons[7] = new ButtonData(8);

        loaderId = 1;
    }

    public static void setSensor(SensorData sensorData) {
        boolean dim = false;
        for (SensorData sensorData1 : sensors) {
            if (sensorData1.id.equals(sensorData.id)) {
                sensorData1.action = sensorData.action;
                sensorData1.channels = sensorData.channels;
                sensorData1.function = sensorData.function;
                sensorData1.brightness = sensorData.brightness;
                sensorData1.name = sensorData.name;
                if (sensorData.function == 0) {
                    dim = true;
                }
            }
        }
        hasDim = dim;
    }

    public static SensorData[] getSensors() {
        return sensors;
    }

    public static boolean hasDim() {
        return hasDim;
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
        for (ButtonData buttonData1 : buttons) {
            if (buttonData.id == buttonData1.id) {
                buttonData1.action = buttonData.action;
                buttonData1.channels = buttonData.channels;
                buttonData1.sync = buttonData.sync;
                buttonData1.momentary = buttonData.momentary;
            }
        }
    }
}
