package com.example.narcolepsyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;

import android.content.res.AssetManager;

import android.content.res.ColorStateList;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.narcolepsyproject.biosignals.heartrate.HeartRateCallback;
import com.example.narcolepsyproject.biosignals.heartrate.HeartRateManager;
import com.example.narcolepsyproject.biosignals.heartrate.HeartRateMonitor;

import com.example.narcolepsyproject.db.RoomDB;

import com.example.narcolepsyproject.db.contact.ContactData;
import com.example.narcolepsyproject.db.setting.SettingDao;
import com.example.narcolepsyproject.notification.NotificationHelper;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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

public class HomeActivity extends AppCompatActivity implements HeartRateCallback {


    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    HorizontalBarChart horizontalBarChart;
    TextView heartbeatText;

    TextView weekReport;
    float lastWeekData;
    float thisWeekData;


    RoomDB database;
    HeartRateManager heartRateManager;








    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");

        database = RoomDB.getInstance(this);

        heartbeatText = findViewById(R.id.heartBeat);


//        ContactData.getAllContactData(this);
        List<ContactData> contactDataList = new ArrayList<>();
        contactDataList = ContactData.getAllContactData(this);

        SettingDao settingDao = database.settingDao();
        List<Integer> dataList = settingDao.getRepeatCountData();
        Boolean activate = settingDao.getActivateData();

        String logMessage = "Contact Data List: " + contactDataList.toString();
        System.out.println(logMessage + dataList.toString() + activate.toString());

        weekReport = findViewById(R.id.weekReport);



        //뷰 관련

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


        List<List<Integer>> sleepChartDataSet = setSleepRecord(30);
        List<Integer> thisWeekSleepData = sleepChartDataSet.get(1).subList(0,7);
        List<Integer> lastWeekSleepData = sleepChartDataSet.get(1).subList(23,30);
        float thisWeekTotal = 0;
        float lastWeekTotal = 0;
        for (int i = 0; i < 7; i++) {
            thisWeekTotal += thisWeekSleepData.get(i);
            lastWeekTotal += lastWeekSleepData.get(i);
        }
        thisWeekData = thisWeekTotal / 7;
        lastWeekData = lastWeekTotal / 7;

        if (thisWeekData > lastWeekData) {
            weekReport.setText("지난 주에 비해 활동시간 중 수면빈도가 증가했습니다.");
        } else if (thisWeekData < lastWeekData) {
            weekReport.setText("지난 주에 비해 활동시간 중 수면빈도가 증가했습니다.");
        } else {
            weekReport.setText("지난 주와 활동시간 중 수면빈도가 일치합니다.");
        }

        horizontalBarChart = findViewById(R.id.weekChart);

        // "지난주" 데이터
        ArrayList<BarEntry> lastWeekEntries = new ArrayList<>();
        lastWeekEntries.add(new BarEntry(0, lastWeekData));
// 데이터 추가 코드...

// "이번주" 데이터
        ArrayList<BarEntry> thisWeekEntries = new ArrayList<>();
        thisWeekEntries.add(new BarEntry(1, thisWeekData));
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


        HeartRateManager manager = new HeartRateManager(HomeActivity.this);
        manager.startHeartRateMonitoring();

        stressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, StressActivity.class));
            }
        });

        // HeartRateManager 싱글톤 객체 얻기
        heartRateManager = HeartRateManager.getInstance(this);
        // startHeartRateMonitoring() 함수가 처음 호출되었는지 확인 후 실행
        if (!heartRateManager.isIsHeartRateMonitoringStarted()) {
            heartRateManager.startHeartRateMonitoring(HomeActivity.this);
            heartRateManager.setIsHeartRateMonitoringStarted(true);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        heartbeatText = findViewById(R.id.heartBeat);

    }


    @Override
    public void onHeartRateUpdate(int heartRate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                heartbeatText.setText(Integer.toString(heartRate));
            }
        });
    }


    @Override
    public void onDangerousHeartRate() {
        NotificationHelper.showNotification(HomeActivity.this, "심박수 알림", "남은 횟수: ");
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


}