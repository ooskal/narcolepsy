package com.example.narcolepsyproject.db.sleep_session;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Entity(tableName = "sleep_session")
public class SleepSessionData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "startTime")
    private int startTime;

    @ColumnInfo(name = "startZoneOffset")
    private ZoneOffset startZoneOffSet; //zoneOffSet -> 시간타입

    @ColumnInfo(name = "endTime")
    private int endTime;

    @ColumnInfo(name = "endZoneOffset")
    private ZoneOffset endZoneOffSet;

    @ColumnInfo(name = "duration") //duration 시간길이
    private Duration duration;

//    @Ignore // 스키마에 포함 안함
//    private List<SleepStageRecord> stages = new ArrayList<>();

    public int getId()
    { return id; }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public ZoneOffset getStartZoneOffSet() {
        return startZoneOffSet;
    }

    public void setStartZoneOffSet(ZoneOffset startZoneOffSet) {
        this.startZoneOffSet = startZoneOffSet;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ZoneOffset getEndZoneOffSet() {
        return endZoneOffSet;
    }

    public void setEndZoneOffSet(ZoneOffset endZoneOffSet) {
        this.endZoneOffSet = endZoneOffSet;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
//
//    public List<SleepStageRecord> getStages() {
//        return stages;
//    }
//
//    public void setStages(List<SleepStageRecord> stages) {
//        this.stages = stages;
//    }



}
