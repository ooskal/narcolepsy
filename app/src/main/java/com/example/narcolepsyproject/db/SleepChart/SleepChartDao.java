package com.example.narcolepsyproject.db.SleepChart;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.narcolepsyproject.db.SleepChart.SleepChartData;

import java.util.List;

@Dao
public interface SleepChartDao {

    @Insert(onConflict = REPLACE)
    void insert(SleepChartData sleepChartData);

    @Update
    void update(SleepChartData sleepChartData);

    @Delete
    void delete(SleepChartData sleepChartData);

    @Query("SELECT * FROM sleep_chart")
    List<SleepChartData> getAllSleepChartData();
}
