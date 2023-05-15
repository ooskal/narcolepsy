package com.example.narcolepsyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class SleepChartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    BarChart sleepChart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_chart);

        setTitle("수면 기록");

        bottomNavigationView = findViewById(R.id.bottomNav);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        int selectedTab = sharedPreferences.getInt("selectedTab", 0);
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(selectedTab).getItemId());

        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_sleepChart:
                        editor.putInt("selectedTab", 0);
                        editor.apply();
                        startActivity(new Intent(SleepChartActivity.this, SleepChartActivity.class));
                        return true;
                    case R.id.item_home:
                        editor.putInt("selectedTab", 1);
                        editor.apply();
                        startActivity(new Intent(SleepChartActivity.this, HomeActivity.class));
                        return true;
                    case R.id.item_myPage:
                        editor.putInt("selectedTab", 2);
                        editor.apply();
                        startActivity(new Intent(SleepChartActivity.this, MyPageActivity.class));
                        return true;
                    default:
                        return false;

                }
            }
        });
        BarChart sleepChart = findViewById(R.id.sleepChart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 7));   // 일요일
        entries.add(new BarEntry(1, 6));   // 월요일
        entries.add(new BarEntry(2, 5));   // 화요일
        entries.add(new BarEntry(3, 8));   // 수요일
        entries.add(new BarEntry(4, 7));   // 목요일
        entries.add(new BarEntry(5, 6));   // 금요일
        entries.add(new BarEntry(6, 5));   // 토요일

        BarDataSet dataSet = new BarDataSet(entries, "Sleep Data");
        dataSet.setColor(Color.BLUE);

        ArrayList<String> labels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            labels.add(getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        BarData data = new BarData(dataSet);
        sleepChart.setData(data);

        XAxis xAxis = sleepChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = sleepChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

        sleepChart.getAxisRight().setEnabled(false);
        Description description = new Description();
        description.setEnabled(false);
        sleepChart.setDescription(description);
        sleepChart.invalidate();

    }
    private String getDayOfWeek(int dayOfWeek) {
        String[] daysOfWeek = {"일", "월", "화", "수", "목", "금", "토"};
        return daysOfWeek[dayOfWeek - 1];
    }
}