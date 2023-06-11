package com.example.narcolepsyproject.notification;

public class SettingSingleton {

    private boolean isSwitchOn;
    private static SettingSingleton instance;
    private String startTime;
    private String endTime;

    public SettingSingleton() {
        this.isSwitchOn = false;
        this.startTime = "00 : 00";
        this.endTime = "00 : 00";
    }

    public static SettingSingleton getInstance() {
        if (instance == null) {
            instance = new SettingSingleton();
        }
        return instance;
    }

    public boolean isSwitchOn() {
        return isSwitchOn;
    }

    public void setSwitchOn(boolean switchOn) {
        isSwitchOn = switchOn;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
