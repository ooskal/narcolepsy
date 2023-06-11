package com.example.narcolepsyproject.biosignals.heartrate;



import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;


import com.example.narcolepsyproject.notification.NotificationHelper;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartRateManager {
    private static final int MIN_HEART_RATE = 50;
    private static final int MAX_HEART_RATE = 70;
//    private static HeartRateCallback callback;
    private static Timer timer;
    private static Random random;
    private static ScheduledExecutorService executorService;
    //알림 활성화 상태
    private static boolean isChecked;
    private HeartRateCallback callback;
    private static boolean isHeartRateMonitoringStarted;

    private static HeartRateManager instance;

    private HeartRateManager(HeartRateCallback callback) {
        this.callback = callback;
        this.random = new Random();
        this.isHeartRateMonitoringStarted = false;
        this.isChecked = false;
    }

    public static synchronized HeartRateManager getInstance(HeartRateCallback callback) {
        if (instance == null) {
            instance = new HeartRateManager(callback);
        }
        return instance;
    }

    public static void onAlert(){
        isChecked = true;
    }

    public static void offAlert(){
        isChecked = false;
    }




//    //심박 업데이트
//    public void startHeartRateMonitoring() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                stopHeartRateMonitoring();
//
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        // 심박 업데이트 작업 수행
//                        updateHeartRate();
//                    }
//                }, 0, 7000); // 7초마다 실행
//            }
//        }).start();
//    }

    //심박 업데이트
    public void startHeartRateMonitoring(HeartRateCallback callback) {

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        // 심박 업데이트 작업 수행
                        updateHeartRate(callback);
                    }
                }, 0, 7000); // 7초마다 실행

    }



    static void updateHeartRate(HeartRateCallback callback) {
        int heartRate = generateRandomHeartRate();
        callback.onHeartRateUpdate(heartRate);
        Log.d("HeartRateManager", String.valueOf(heartRate));

        if (isChecked && heartRate <= 60) {
            callback.onDangerousHeartRate();
        }
    }

    //작동 중지
    public static void stopHeartRateMonitoring() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    private static int generateRandomHeartRate() {
        return random.nextInt(MAX_HEART_RATE - MIN_HEART_RATE + 1) + MIN_HEART_RATE;
    }

    public static boolean isIsHeartRateMonitoringStarted() {
        return isHeartRateMonitoringStarted;
    }

    public static boolean isChecked() {
        return isChecked;
    }

    public static void setIsHeartRateMonitoringStarted(boolean isHeartRateMonitoringStarted) {
        HeartRateManager.isHeartRateMonitoringStarted = isHeartRateMonitoringStarted;
    }
}
