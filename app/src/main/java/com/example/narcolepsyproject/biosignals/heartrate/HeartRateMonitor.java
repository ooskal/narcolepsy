package com.example.narcolepsyproject.biosignals.heartrate;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class HeartRateMonitor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private HeartRateCallback callback;
    private static boolean isChecked;

    public static void onAlert(){
        isChecked = true;
    }

    public static void offAlert(){
        isChecked = false;
    }

    public HeartRateMonitor(Context context, HeartRateCallback callback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        this.callback = callback;
    }

    public void startMonitoring() {
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopMonitoring() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float heartRate = event.values[0];
            callback.onHeartRateUpdate((int) heartRate);

            if (isChecked && heartRate <= 60) {
                callback.onDangerousHeartRate();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 정확도 변경 시 처리
    }
}
