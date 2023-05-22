package com.example.narcolepsyproject.biosignals.heartrate;

public interface HeartRateCallback {
    void onHeartRateUpdate(int heartRate);
    void onDangerousHeartRate();
}
