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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
import java.util.List;

public class SleepChartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button weekButton;
    Button monthButton;
    boolean isWeekSelected = true;

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

        weekButton = findViewById(R.id.weekButton);
        monthButton = findViewById(R.id.monthButton);

        //기본은 주 선택된 것처럼 색상 변경
        weekButton.setTextColor(Color.WHITE);
        weekButton.setBackgroundColor(Color.GRAY);
        monthButton.setTextColor(Color.BLACK);
        monthButton.setBackgroundColor(Color.WHITE);
        setWeeklyGraph();
        // 주 버튼 클릭 시
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWeekSelected = true;
                updateButtonState();
                setWeeklyGraph();
            }
        });
        // 월 버튼 클릭 시
        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWeekSelected = false;
                updateButtonState();
                setMonthlyGraph();
            }
        });
    }
    private String getDayOfWeek(int dayOfWeek) {
        String[] daysOfWeek = {"일", "월", "화", "수", "목", "금", "토"};
        return daysOfWeek[dayOfWeek - 1];
    }
    private void updateButtonState() {
        if (isWeekSelected) {
            weekButton.setTextColor(Color.WHITE);
            weekButton.setBackgroundColor(Color.GRAY);
            monthButton.setTextColor(Color.BLACK);
            monthButton.setBackgroundColor(Color.WHITE);
        } else {
            weekButton.setTextColor(Color.BLACK);
            weekButton.setBackgroundColor(Color.WHITE);
            monthButton.setTextColor(Color.WHITE);
            monthButton.setBackgroundColor(Color.GRAY);
        }
    }

    // 주간 그래프 설정
    private void setWeeklyGraph() {
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
        dataSet.setColor(Color.rgb(255, 160, 72));

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
        sleepChart.getLegend().setEnabled(false);
        sleepChart.invalidate();
    }

    // 월간 그래프 설정
    private void setMonthlyGraph() {
        BarChart sleepChart = findViewById(R.id.sleepChart);

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            entries.add(new BarEntry(i, (float) Math.random() * 10)); // 임의의 값 넣기
        }

        BarDataSet dataSet = new BarDataSet(entries, "Sleep Month Data");
        dataSet.setColor(Color.rgb(255, 160, 72));
        BarData data = new BarData(dataSet);
        sleepChart.setData(data);

        XAxis xAxis = sleepChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonthDays()));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = sleepChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

        sleepChart.getAxisRight().setEnabled(false);
        Description description = new Description();
        description.setEnabled(false);
        sleepChart.setDescription(description);
        sleepChart.getLegend().setEnabled(false);
        sleepChart.invalidate();
    }

    // 날짜 반환하는 메소드
    private ArrayList<String> getMonthDays() {
        ArrayList<String> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 30; i++) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            days.add(String.valueOf(dayOfMonth));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }
}