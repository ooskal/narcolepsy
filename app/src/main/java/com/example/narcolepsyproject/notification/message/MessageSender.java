package com.example.narcolepsyproject.notification.message;

import android.telephony.SmsManager;
import android.widget.Toast;

public class MessageSender {

    private static String phoneNo = "01074785637";
    private static String sms = "문자 내용";


    public void sendMessage() {
        // 메시지를 보내는 코드
        try {
            //전송
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
