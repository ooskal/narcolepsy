package com.example.narcolepsyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        HorizontalBarChart horizontalBarChart = findViewById(R.id.testChart);

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
        dataSets.add(lastWeekDataSet);
        dataSets.add(thisWeekDataSet);

        BarData barData = new BarData(dataSets);
        horizontalBarChart.setData(barData);

        XAxis xAxis = horizontalBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

// "지난주"과 "이번주" 라벨 설정
        xAxis.setLabelCount(2);
        String[] labels = {"지난주", "이번주"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        horizontalBarChart.invalidate();

    }
}