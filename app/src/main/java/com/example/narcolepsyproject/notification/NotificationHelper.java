package com.example.narcolepsyproject.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.narcolepsyproject.HomeActivity;
import com.example.narcolepsyproject.R;
import com.example.narcolepsyproject.biosignals.heartrate.HeartRateManager;
import com.example.narcolepsyproject.db.RoomDB;
import com.example.narcolepsyproject.db.contact.ContactData;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class NotificationHelper {


    private static final String NOTIFICATION_CHANNEL_ID = "your_channel_id";
    public static final int NOTIFICATION_ID = 1234;
    private static int notificationCount;
    private static int fixedCount;
    private static boolean isClicked = false;
    private static int delayMillis = 6000; // 6초



    public static void setCount(Integer count){
        fixedCount = count;
        notificationCount  = fixedCount;
    }






    //알림 실행 때마다 카운트 감소
    public static void changeNotificationCount(){

        if(notificationCount!=1){

            notificationCount--;
        }
        else {
            notificationCount--;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!isClicked) {
                                // 메세지 전송
                                MessageSender.sendMessage();

                                // 알림 끄기
                                HeartRateManager.offAlert();

                                // 카운트 리셋
                                notificationCount = fixedCount;
                            }
                        }
                    }, delayMillis);
                }
            }).start();


        }

    }



    //버튼 클릭 되었을 때
    public static void resetNotificationStatus(){
        notificationCount = fixedCount;
        isClicked = true;
    }




    //알림 생성
    public static void showNotification(Context context, String title, String message) {
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        //버튼 상태 리셋
        isClicked = false;

        //카운트 감소
        changeNotificationCount();

        Intent buttonClickIntent = new Intent(context, ButtonClickReceiver.class);
        buttonClickIntent.setAction("com.example.BUTTON_CLICK_ACTION");
        buttonClickIntent.putExtra("CLICK_COUNT", notificationCount); // 알림 횟수를 인텐트에 추가
        PendingIntent buttonClickPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                buttonClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String notificationString = notificationCount + "/" + fixedCount;


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message + notificationString)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_sms, "확인", buttonClickPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Notification Channel";
            String description = "Description of the channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    channelName,
                    importance
            );
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }




}
