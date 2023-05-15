package com.example.narcolepsyproject.biosignals;

public interface HeartRateCallback {
    void onHeartRateUpdate(int heartRate);
    void onDangerousHeartRate();
}
