package com.example.narcolepsyproject.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ButtonClickReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("com.example.BUTTON_CLICK_ACTION")) {

            //알림 수 리셋
            NotificationHelper.resetNotificationCount();
            // 알림 취소
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NotificationHelper.NOTIFICATION_ID);

        }


    }
}
