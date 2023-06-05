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
import com.example.narcolepsyproject.notification.message.MessageSender;

public class NotificationHelper {


    private static final String NOTIFICATION_CHANNEL_ID = "your_channel_id";
    public static final int NOTIFICATION_ID = 1234;
    private static int notificationCount = 3;
    private static boolean isButtonClicked = false;
    public static MessageSender messageSender;



    //알림 실행 때마다 카운트 감소
    public static void addNotificationCount(){

        if(notificationCount!=0){

            notificationCount--;
        }
        else {
            //문자메시지 보내기..알림 사라지고 난 후에 바로 보내져야댐. 즉 타이머 사용해서 실행하기
            String phoneNo = "01074785637";
            String sms = "메시지테스트";
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, sms, null, null);
//                Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "전송 오류!", Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();//오류 원인이 찍힌다.
                e.printStackTrace();
            }


            //일단 리셋
            notificationCount=3;
        }

    }



    //버튼 클릭 되었을 때
    public static void resetNotificationCount(){
        notificationCount = 3;
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

        addNotificationCount();

        Intent buttonClickIntent = new Intent(context, ButtonClickReceiver.class);
        buttonClickIntent.setAction("com.example.BUTTON_CLICK_ACTION");
        buttonClickIntent.putExtra("CLICK_COUNT", notificationCount); // 알림 횟수를 인텐트에 추가
        PendingIntent buttonClickPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                buttonClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String notificationString = String.valueOf(notificationCount);

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
