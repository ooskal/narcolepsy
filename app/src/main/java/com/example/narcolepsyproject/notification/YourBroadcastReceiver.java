package com.example.narcolepsyproject.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.narcolepsyproject.notification.NotificationUtils;

public class YourBroadcastReceiver extends BroadcastReceiver {
    private static final String PREFS_NAME = "ButtonClickPrefs";
    private static final String KEY_CLICK_COUNT = "clickCount";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NotificationUtils.ACTION_BUTTON_CLICK)) {
            // 버튼이 클릭되었을 때 실행될 코드
            // 클릭된 횟수를 SharedPreferences에서 읽어옴
            SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int clickCount = preferences.getInt(KEY_CLICK_COUNT, 0);

            // 클릭된 횟수 증가 및 저장
            clickCount++;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(KEY_CLICK_COUNT, clickCount);
            editor.apply();

            // 저장된 클릭된 횟수 확인 (테스트용)
            Log.d("ButtonClick", "Click Count: " + clickCount);
        }
    }
}

