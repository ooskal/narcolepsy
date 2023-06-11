package com.example.narcolepsyproject.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.narcolepsyproject.db.SleepChart.SleepChartDao;
import com.example.narcolepsyproject.db.SleepChart.SleepChartData;
import com.example.narcolepsyproject.db.contact.ContactDao;
import com.example.narcolepsyproject.db.contact.ContactData;
import com.example.narcolepsyproject.db.setting.SettingDao;
import com.example.narcolepsyproject.db.setting.SettingData;

@TypeConverters(Converters.class)
@Database(entities = {ContactData.class, SleepChartData.class, SettingData.class}, version = 2, exportSchema = false)
public abstract class RoomDB extends RoomDatabase
{
    private static RoomDB database;

    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context)
    {
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract ContactDao mainDao();
    public abstract SleepChartDao sleepDao();
    public abstract SettingDao settingDao();

}
