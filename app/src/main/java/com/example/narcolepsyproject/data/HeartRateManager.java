package com.example.narcolepsyproject.data;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HeartRateManager {
    private static final int MIN_HEART_RATE = 50;
    private static final int MAX_HEART_RATE = 70;
    private HeartRateCallback callback;
    private Timer timer;
    private Random random;


    public HeartRateManager(HeartRateCallback callback) {
        this.callback = callback;
        this.random = new Random();
    }

    public void startHeartRateMonitoring() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int heartRate = generateRandomHeartRate();
                callback.onHeartRateUpdate(heartRate);

                if (heartRate < MIN_HEART_RATE) {
                    callback.onDangerousHeartRate();
                }
            }
        }, 0, 3000); // 3초마다 실행
    }

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
