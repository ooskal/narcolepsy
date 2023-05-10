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
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout stressLayout;
    HorizontalBarChart horizontalBarChart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");

        bottomNavigationView = findViewById(R.id.bottomNav);
        stressLayout = findViewById(R.id.stressBox);

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
                        startActivity(new Intent(HomeActivity.this, SleepChartActivity.class));
                        return true;
                    case R.id.item_home:
                        editor.putInt("selectedTab", 1);
                        editor.apply();
                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        return true;
                    case R.id.item_myPage:
                        editor.putInt("selectedTab", 2);
                        editor.apply();
                        startActivity(new Intent(HomeActivity.this, MyPageActivity.class));
                        return true;
                    default:
                        return false;
                }
            }
        });

        horizontalBarChart = findViewById(R.id.weekChart);

        // "지난주" 데이터
        ArrayList<BarEntry> lastWeekEntries = new ArrayList<>();
        lastWeekEntries.add(new BarEntry(0, 20f));
// 데이터 추가 코드...

// "이번주" 데이터
        ArrayList<BarEntry> thisWeekEntries = new ArrayList<>();
        thisWeekEntries.add(new BarEntry(1, 10f));
// 데이터 추가 코드...

        BarDataSet lastWeekDataSet = new BarDataSet(lastWeekEntries, "지난주");
        BarDataSet thisWeekDataSet = new BarDataSet(thisWeekEntries, "이번주");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(thisWeekDataSet);
        dataSets.add(lastWeekDataSet);

        BarData barData = new BarData(dataSets);
        horizontalBarChart.setData(barData);

        XAxis xAxis = horizontalBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

// "지난주"과 "이번주" 라벨 설정
        xAxis.setLabelCount(2);
        String[] labels = {"지난주", "이번주"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        horizontalBarChart.invalidate();

        YAxis yAxis = horizontalBarChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

        horizontalBarChart.getAxisRight().setEnabled(false);

        YAxis yAxisRight = horizontalBarChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        yAxis.setDrawGridLines(false);
        lastWeekDataSet.setColors(Color.LTGRAY);
        horizontalBarChart.getLegend().setEnabled(false);
        horizontalBarChart.getDescription().setEnabled(false);
        horizontalBarChart.setDrawGridBackground(false);
        lastWeekDataSet.setDrawValues(false);
        thisWeekDataSet.setDrawValues(false);

        stressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, StressActivity.class));
            }
        });

    }
}