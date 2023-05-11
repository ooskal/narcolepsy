package com.example.narcolepsyproject.data;

public interface HeartRateCallback {
    void onHeartRateUpdate(int heartRate);
    void onDangerousHeartRate();
}
