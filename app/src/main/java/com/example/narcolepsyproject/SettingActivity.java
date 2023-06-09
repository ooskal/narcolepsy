package com.example.narcolepsyproject;

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

import com.example.narcolepsyproject.biosignals.heartrate.HeartRateCallback;
import com.example.narcolepsyproject.biosignals.heartrate.HeartRateManager;
import com.example.narcolepsyproject.notification.NotificationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("알림 설정");

        bottomNavigationView = findViewById(R.id.bottomNav);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat("HH : mm", Locale.getDefault());
        notificationSwitch = findViewById(R.id.notification_switch);
        repeat = findViewById(R.id.repeat);


        //반복 수
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
                                NotificationHelper.setCount(intText);


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
                    HeartRateManager.onAlert();

                } else {
                    notificationSwitch.setText("꺼짐");
                    // 스위치가 비활성화된 상태
                    HeartRateManager.offAlert();
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTime);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTime);
            }
        });

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
    private void showTimePickerDialog(TextView textView) {
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
                    }
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }



}