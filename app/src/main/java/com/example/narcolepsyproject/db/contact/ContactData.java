package com.example.narcolepsyproject.db.contact;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


//Serializable -> 객체 직렬화
@Entity(tableName = "contact")
public class ContactData implements Serializable
{

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
}
