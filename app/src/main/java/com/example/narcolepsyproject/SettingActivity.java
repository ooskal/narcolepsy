package com.example.narcolepsyproject;

import static com.example.narcolepsyproject.notification.SettingSingleton.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.narcolepsyproject.biosignals.heartrate.HeartRateManager;
import com.example.narcolepsyproject.biosignals.heartrate.HeartRateMonitor;


import com.example.narcolepsyproject.biosignals.heartrate.HeartRateManager;
import com.example.narcolepsyproject.db.RoomDB;
import com.example.narcolepsyproject.db.setting.SettingData;

import com.example.narcolepsyproject.notification.NotificationHelper;
import com.example.narcolepsyproject.notification.SettingSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView startTime, endTime, repeat;
    private Calendar calendar;
    private SimpleDateFormat timeFormat;
    private Switch notificationSwitch;
    private Integer repeatNum;
    RoomDB database;
    SettingData dataList;
    HeartRateManager heartRateManager;
    SettingSingleton settingSingleton = SettingSingleton.getInstance();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("알림설정");

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat("HH : mm", Locale.getDefault());
        notificationSwitch = findViewById(R.id.notification_switch);
        repeat = findViewById(R.id.repeat);

        // Room DB 인스턴스 생성
        database = RoomDB.getInstance(this);

        // 데이터 목록을 가져옴
//        List<Integer> repeatCount = database.settingDao().getRepeatCountData();
//        if (repeatCount != null) {
//            repeat.setText(String.valueOf(repeatCount.get(repeatCount.size() - 1)));
//        }

        repeat.setText(String.valueOf(settingSingleton.getRepeat()));
        // 반복 수
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                final EditText input = new EditText(SettingActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setText(repeat.getText());

                builder.setView(input)
                        .setTitle("반복 횟수를 입력하세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newText = input.getText().toString();
                                int intText = Integer.parseInt(newText);
                                repeat.setText(newText);

                                settingSingleton.setRepeat(intText);


                                // Room DB에 값을 저장
                                SettingData settingData = new SettingData();
                                settingData.setRepeatCount(intText);

                                // DB에 값 삽입
                                database.settingDao().insert(settingData);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




        //알림 활성화
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 상태 변경 시 호출되는 메서드
                if (isChecked) {
                    notificationSwitch.setText("켜짐");
                    // 스위치가 활성화된 상태
//                    HeartRateManager.onAlert();
                    NotificationHelper.setCount();
                    SettingSingleton isSwitchOnSingleton = getInstance();
                    isSwitchOnSingleton.setSwitchOn(true);



                    // Room DB에 값을 저장
                    SettingData settingData = new SettingData();
                    settingData.setActivate(true);

                    // DB에 값 삽입
                    database.settingDao().insert(settingData);

                } else {
                    notificationSwitch.setText("꺼짐");
                    // 스위치가 비활성화된 상태
//                    HeartRateManager.offAlert();

                    SettingSingleton isSwitchOnSingleton = getInstance();
                    isSwitchOnSingleton.setSwitchOn(false);

                    // Room DB에 값을 저장
                    SettingData settingData = new SettingData();
                    settingData.setActivate(false);

                    // DB에 값 삽입
                    database.settingDao().insert(settingData);
                }
            }
        });



        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTime, true);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTime, false);
            }
        });


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
                        startActivity(new Intent(SettingActivity.this, SleepChartActivity.class));
                        return true;
                    case R.id.item_home:
                        editor.putInt("selectedTab", 1);
                        editor.apply();
                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                        return true;
                    case R.id.item_myPage:
                        editor.putInt("selectedTab", 2);
                        editor.apply();
                        startActivity(new Intent(SettingActivity.this, MyPageActivity.class));
                        return true;
                    default:
                        return false;
                }
            }
        });


    }
    private void showTimePickerDialog(TextView textView, boolean isStartTime) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String selectedTime = timeFormat.format(calendar.getTime());
                        textView.setText(selectedTime);
                        if (isStartTime) {
                            SettingSingleton settingSingleton = SettingSingleton.getInstance();
                            settingSingleton.setStartTime(selectedTime);
                        } else {
                            SettingSingleton settingSingleton = SettingSingleton.getInstance();
                            settingSingleton.setEndTime(selectedTime);
                        }
                    }
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SettingSingleton settingSingleton = getInstance();
        if (settingSingleton.isSwitchOn()) {
            notificationSwitch.setChecked(true);
        } else {
            notificationSwitch.setChecked(false);
        }
        startTime.setText(settingSingleton.getStartTime());
        endTime.setText(settingSingleton.getEndTime());


        repeat.setText(String.valueOf(settingSingleton.getRepeat()));

//        database = RoomDB.getInstance(this);
//        List<Integer> repeatCount = database.settingDao().getRepeatCountData();
//        repeat.setText(String.valueOf(repeatCount.get(repeatCount.size() - 1)));


    }
}