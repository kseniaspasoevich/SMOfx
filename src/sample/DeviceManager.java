package sample;

import lombok.Getter;

public final class DeviceManager {
    private static DeviceManager instance;

    @Getter
    private Device[] devices;
    @Getter
    private int deviceAmount;
    private Buffer buffer;

    private DeviceManager(int deviceAmount, Buffer buffer, double alpha, double beta){
        this.buffer = buffer;
        this.deviceAmount = deviceAmount;
        devices = new Device[deviceAmount];
        for (int i = 0; i < deviceAmount; i++){
            devices[i] = new Device(alpha, beta);
        }
    }

    public static DeviceManager getInstance(int deviceAmount, Buffer buffer, double alpha, double beta) {
        if (instance == null) {
            instance = new DeviceManager(deviceAmount,buffer, alpha, beta);
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public double getMinTime(){
        double time = devices[0].getTime();
        for (int i = 1; i < deviceAmount; i++) {
            if (devices[i].getTime() < time) {
                time = devices[i].getTime();
            }
        }
        return time;
    }

    public void takeRequest(){
        double time = devices[0].getTime();
        Device device = devices[0];
        for (int i = 1; i < deviceAmount; i++) {
            if (devices[i].getTime() < time) {
                time = devices[i].getTime();
                device = devices[i];
            }
        }
        device.takeRequest(buffer.getFirstRequest());
    }

    public double getMaxTime(){
        double time = devices[0].getTime();
        for (int i = 1; i < deviceAmount; i++) {
            if (devices[i].getTime() > time) {
                time = devices[i].getTime();
            }
        }
        return time;
    }

}
