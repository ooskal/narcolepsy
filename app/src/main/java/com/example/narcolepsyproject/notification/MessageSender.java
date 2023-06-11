package com.example.narcolepsyproject.notification;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.narcolepsyproject.db.contact.ContactData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MessageSender {

    private static Context context;
    private static List<ContactData> contactList = ContactData.getAllContactData(context);
    private static List<String> smsList = new ArrayList<>();

    private static List<String> formattedDataList = new ArrayList<>();

    public MessageSender(Context context){
        this.context = context;
    };



    public static void sendMessage(){

        String sms = "[기면증 안내 문자] 현재 사용자가 알림에 반복적으로 응답이 없습니다. 신속히 사용자의 상태를 확인하고 조치 바랍니다.";
        String location = LocationHelper.getLocationText();


        for(ContactData contactData: contactList){

            String phoneNo = String.valueOf(contactData);

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, sms, null, null);

                smsManager.sendTextMessage(phoneNo, null, location, null, null);

                SettingSingleton settingSingleton = SettingSingleton.getInstance();
                settingSingleton.setSwitchOn(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        formatData(location);

    }

    public static void formatData(String location){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateStr = dateFormat.format(new Date());

        StringJoiner joiner = new StringJoiner(",");
        for (ContactData contactData : contactList) {
            String phoneNumber = contactData.getPhoneNumber(); // ContactData에서 phoneNumber 추출
            joiner.add(phoneNumber); // 번호를 StringJoiner에 추가
        }
        String contactStr = joiner.toString();

        String text = "날짜: " + dateStr + "\n수신자: " + contactStr + "\n위치: " + location;
        formattedDataList.add(text);
    }

    public static List<String> getMessageData(){
        return formattedDataList;
    }



}
