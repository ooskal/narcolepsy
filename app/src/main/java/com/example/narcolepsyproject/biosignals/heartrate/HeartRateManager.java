package com.example.narcolepsyproject.biosignals.heartrate;

import android.util.Log;

import com.example.narcolepsyproject.biosignals.heartrate.HeartRateCallback;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HeartRateManager {
    private static final int MIN_HEART_RATE = 50;
    private static final int MAX_HEART_RATE = 70;
    private HeartRateCallback callback;
    private Timer timer;
    private Random random;
    private static boolean isChecked;

    //생성자
    public HeartRateManager(HeartRateCallback callback) {
        this.callback = callback;
        this.random = new Random();
    }

    public static void onAlert(){
        isChecked = true;
    }

    public static void offAlert(){
        isChecked = false;
    }

    //3초마다 심박 업데이트
    public void startHeartRateMonitoring() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int heartRate = generateRandomHeartRate();
                callback.onHeartRateUpdate(heartRate);
                Log.d("HeartRateManager", String.valueOf(heartRate));

                if (isChecked == true && heartRate <= 60) {
                    callback.onDangerousHeartRate();
                }
            }
        }, 0, 7000); // 7초마다 실행
    }

    //작동 중지
    public void stopHeartRateMonitoring() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    private int generateRandomHeartRate() {
        return random.nextInt(MAX_HEART_RATE - MIN_HEART_RATE + 1) + MIN_HEART_RATE;
    }
}
