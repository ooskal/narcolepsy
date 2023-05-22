package com.example.narcolepsyproject.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.narcolepsyproject.HomeActivity;
import com.example.narcolepsyproject.R;

public class NotificationHelper {

    private static int buttonClickCount = 3;
    private static final String NOTIFICATION_CHANNEL_ID = "your_channel_id";
    public static final int NOTIFICATION_ID = 1234;

    public static void addButtonClickCount(){
        if(buttonClickCount != 0){
            buttonClickCount--;
        }else{
            buttonClickCount = 3;
        }
    }

    public static void showNotification(Context context, String title, String message) {
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );



        Intent buttonClickIntent = new Intent(context, ButtonClickReceiver.class);
        buttonClickIntent.setAction("com.example.BUTTON_CLICK_ACTION");
        buttonClickIntent.putExtra("CLICK_COUNT", buttonClickCount); // 버튼 클릭 횟수를 인텐트에 추가
        PendingIntent buttonClickPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                buttonClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String clickCountString = String.valueOf(buttonClickCount); // 버튼 클릭 횟수를 문자열로 변환

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message + clickCountString)
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
