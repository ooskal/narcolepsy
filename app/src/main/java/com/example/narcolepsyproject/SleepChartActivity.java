package com.example.narcolepsyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.narcolepsyproject.db.RoomDB;
import com.example.narcolepsyproject.db.SleepChart.SleepChartData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class SleepChartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button weekButton;
    Button monthButton;
    boolean isWeekSelected = true;

    //데이터베이스
    RoomDB database;
    List<SleepChartData> dataList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_chart);

        setTitle("수면기록");

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

        String upOrDown = "증가";
        int percent = 0;
        TextView timeText = findViewById(R.id.timeText2);
        List<List<Integer>> timeCheckDataSet = setSleepRecord(30);
        int yesterdayCheck = timeCheckDataSet.get(1).subList(6, 7).get(0);
        int twoDayAgoCheck = timeCheckDataSet.get(1).subList(5, 6).get(0);
        if (yesterdayCheck > twoDayAgoCheck) {
            upOrDown = "증가";
            percent = (yesterdayCheck - twoDayAgoCheck) * 100 / twoDayAgoCheck;
        } else {
            upOrDown = "감소";
            percent = (twoDayAgoCheck - yesterdayCheck) * 100 / yesterdayCheck;
        }
        String reportMessage = "이틀 전과 비교해서 전일 수면 시간이\n약 " + percent + "%만큼 " + upOrDown + "했습니다.";
        if (yesterdayCheck != twoDayAgoCheck) {
            timeText.setText(reportMessage);
        } else {
            timeText.setText("이틀 전과 어제의 수면 시간이 같습니다.");
        }
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

    //수면기록저장
    private void saveSleepRecord(int numEntries) {
        Random random = new Random();
        for (int i = 0; i < numEntries; i++) {
            int x = i;
            int y = random.nextInt(8) + 1;

            SleepChartData sleepData = new SleepChartData();
            sleepData.setDayOfWeek(x);
            sleepData.setHoursOfSleep(y);

            database.sleepDao().insert(sleepData);
        }

    }

    public List<List<Integer>> setSleepRecord(int numEntries) {
        AssetManager assetManager = getAssets();
        List<List<Integer>> result = new ArrayList<>();

        try {
            InputStream is = assetManager.open("json/sleepChart.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder buffer = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\n");
                line = reader.readLine();
            }
            String jsonData = buffer.toString();

            JSONArray jsonArray = new JSONArray(jsonData);

            List<Integer> xValue = new ArrayList<>();
            List<Integer> yValue = new ArrayList<>();
            // numEntries 값만큼 JSON 데이터 가져오기
            int length = Math.min(jsonArray.length(), numEntries);
            for (int i = 0; i < length; i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                int x = jo.getInt("x");
                int y = jo.getInt("y");
                xValue.add(x);
                yValue.add(y);

                List<Integer> message = xValue;
                Log.d( "setSleepRecord: ", message.toString());
            }
            result.add(xValue);
            result.add(yValue);

            // TODO: 가져온 데이터를 사용하여 추가 작업 수행

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void setWeeklyGraph() {

        BarChart sleepChart = findViewById(R.id.sleepChart);

        int numEntries = 7; // 사용할 데이터 개수

        ArrayList<BarEntry> entries = new ArrayList<>();

        List<List<Integer>> sleepChartDataSet = setSleepRecord(numEntries);


        for (int i = 0; i < numEntries; i++) {
            int x = i;
            int y = sleepChartDataSet.get(1).get(i); // 첫 번째 리스트에서 가져옵니다.
            entries.add(new BarEntry(x, y));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Sleep Data");
        dataSet.setColor(Color.rgb(110, 239, 187));

        ArrayList<String> labels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < numEntries; i++) {
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

        int numEntries = 30; // 사용할 데이터 개수

        ArrayList<BarEntry> entries = new ArrayList<>();
        // 앞의 0부터 6까지의 데이터를 맨 뒤로 이동시킴
        List<List<Integer>> sleepChartDataSet = setSleepRecord(numEntries);

        List<Integer> firstEntries = sleepChartDataSet.get(1).subList(7, numEntries);
        List<Integer> lastEntries = sleepChartDataSet.get(1).subList(0, 7);
        firstEntries.addAll(lastEntries);

        for (int i = 0; i < numEntries; i++) {
            int x = i;
            int y = firstEntries.get(i);
            entries.add(new BarEntry(x, y));
        }


        BarDataSet dataSet = new BarDataSet(entries, "Sleep Month Data");
        dataSet.setColor(Color.rgb(116, 188, 155));
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