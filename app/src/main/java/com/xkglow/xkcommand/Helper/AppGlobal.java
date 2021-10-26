package com.xkglow.xkcommand.Helper;

public class AppGlobal {
    private static SensorData[] sensors;
    private static boolean hasDim;

    public static void init() {
        sensors = new SensorData[3];
        sensors[0] = new SensorData("Sensor 1", "Sensor Name 1");
        sensors[1] = new SensorData("Sensor 2", "Sensor Name 2");
        sensors[2] = new SensorData("Sensor 3", "Sensor Name 3");
        hasDim = false;
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
}
