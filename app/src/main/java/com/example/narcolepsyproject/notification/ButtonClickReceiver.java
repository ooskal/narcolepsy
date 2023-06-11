package com.example.narcolepsyproject.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ButtonClickReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("com.example.BUTTON_CLICK_ACTION")) {

            //알림 수 리셋, 버튼 상태 변경
            NotificationHelper.resetNotificationStatus();

            // 알림 취소
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NotificationHelper.NOTIFICATION_ID);
            Log.d("cancelAlert2", "버튼 클릭 후 알림취소");

        }


    }
}
