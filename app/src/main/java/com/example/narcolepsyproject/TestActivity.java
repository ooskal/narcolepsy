package com.example.narcolepsyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setMonthlyGraph();

    }
    private void setMonthlyGraph() {
        BarChart sleepChart = findViewById(R.id.testChart);

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            entries.add(new BarEntry(i, (float) Math.random() * 10)); // 임의의 값 넣기
        }

        BarDataSet dataSet = new BarDataSet(entries, "Sleep Month Data");
        dataSet.setColor(Color.rgb(255, 160, 72));

        ArrayList<String> labels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        BarData data = new BarData(dataSet);
        sleepChart.setData(data);

        XAxis xAxis = sleepChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonthDays()));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = sleepChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(10f);

        sleepChart.getAxisRight().setEnabled(false);
        Description description = new Description();
        description.setEnabled(false);
        sleepChart.setDescription(description);
        sleepChart.getLegend().setEnabled(false);
        sleepChart.invalidate();
    }

    // 임의값 생성
//    private float getRandomValue() {
//        return (float) (Math.random() * 10);
//    }

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