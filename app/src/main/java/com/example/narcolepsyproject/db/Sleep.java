package com.example.narcolepsyproject.db;
import com.google.gson.annotations.SerializedName;

public class Sleep {

    private int sleep_id;
    private int day_of_week;
    private int hours_of_sleep;

    @SerializedName("day_of_week")
    private int dayOfWeek;
    @SerializedName("hours_of_sleep")
    private int hoursOfSleep;

    public int getSleepId() {
        return sleep_id;
    }

    public void setSleepId(int sleep_id) {
        this.sleep_id = sleep_id;
    }

    public int getDayOfWeek() {
        return day_of_week;
    }

    public void setDay_of_week(int dayOfWeek) {
        this.day_of_week = day_of_week;
    }

    public int getHoursOfSleep() {
        return hours_of_sleep;
    }

    public void setHoursOfSleep(int hoursOfSleep) {
        this.hours_of_sleep = hoursOfSleep;
    }
    // Add getters and setters for the fields
}
