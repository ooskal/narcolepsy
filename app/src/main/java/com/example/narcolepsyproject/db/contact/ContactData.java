package com.example.narcolepsyproject.db.contact;

import android.content.Context;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.narcolepsyproject.db.RoomDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//Serializable -> 객체 직렬화
@Entity(tableName = "contact")
public class ContactData implements Serializable
{
    private static RoomDB db;

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber;

    public int getId()
    { return id; }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    { return name;}

    public void setName(String name)
    { this.name = name; }

    public String getPhoneNumber()
    { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber)
    { this.phoneNumber = phoneNumber; }

    public static List<ContactData> getAllContactData(Context context) {
        db = RoomDB.getInstance(context);
        ContactDao contactDao = db.mainDao();

        List<ContactData> contactDataList = new ArrayList<>();

        // 데이터베이스에서 모든 데이터 가져옴
        Thread thread = new Thread(() -> {
            List<ContactData> contactList = contactDao.getAll();

            // 가져온 데이터
            for (ContactData contactData : contactList) {
                String phoneNumber = contactData.getPhoneNumber();
                System.out.println("Phone Number: " + phoneNumber);

                contactDataList.add(contactData);
            }
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return contactDataList;
    }

    @Override
    public String toString() {
        return "ContactData [phoneNumber=" + phoneNumber  + "]";
    }
}
