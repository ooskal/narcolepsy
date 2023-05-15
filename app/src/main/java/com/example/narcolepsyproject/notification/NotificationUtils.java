package com.example.narcolepsyproject.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.narcolepsyproject.R;


public class NotificationUtils {

    private static final String CHANNEL_ID = "alert";
    private static final int NOTIFICATION_ID = 1;
    static String ACTION_BUTTON_CLICK = "alert_button_click";


    public static void showNotification(Context context, String title, String message, String buttonText) {
        // 알림 채널 생성 및 등록
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "알림 채널", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 액션 버튼 클릭 시 실행될 브로드캐스트 인텐트 생성
        Intent actionIntent = new Intent(context, YourBroadcastReceiver.class); // 대상 브로드캐스트 리시버로 대체해야 함
        actionIntent.setAction(ACTION_BUTTON_CLICK);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 알림 레이아웃을 커스텀하기 위한 RemoteViews 생성
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.drawable.btn_notification_layout);
        contentView.setTextViewText(R.id.notification_button, buttonText);

        // 알림 레이아웃에 버튼 클릭 이벤트 설정
        contentView.setOnClickPendingIntent(R.id.notification_button, actionPendingIntent);

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setCustomContentView(contentView)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // 알림 표시
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


}
