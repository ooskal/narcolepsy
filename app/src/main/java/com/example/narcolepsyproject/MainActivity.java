package com.example.narcolepsyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.narcolepsyproject.db.RoomDB;
import com.example.narcolepsyproject.db.contact.ContactAdapter;
import com.example.narcolepsyproject.db.contact.ContactData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        
        setTitle("기면증 방지앱 (가제)");

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
                        startActivity(new Intent(MainActivity.this, SleepChartActivity.class));
                        return true;
                    case R.id.item_home:
                        editor.putInt("selectedTab", 1);
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        return true;
                    case R.id.item_myPage:
                        editor.putInt("selectedTab", 2);
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, MyPageActivity.class));
                        return true;
                    default:
                        return false;

                }
            }
        });



    }
}