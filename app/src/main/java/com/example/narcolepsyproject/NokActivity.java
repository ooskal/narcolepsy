package com.example.narcolepsyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class NokActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<HashMap<String, String>> Data = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> InputData1 = new HashMap<>();
    HashMap<String, String> InputData2 = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nok);

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
                        startActivity(new Intent(NokActivity.this, SleepChartActivity.class));
                        return true;
                    case R.id.item_home:
                        editor.putInt("selectedTab", 1);
                        editor.apply();
                        startActivity(new Intent(NokActivity.this, HomeActivity.class));
                        return true;
                    case R.id.item_myPage:
                        editor.putInt("selectedTab", 2);
                        editor.apply();
                        startActivity(new Intent(NokActivity.this, MyPageActivity.class));
                        return true;
                    default:
                        return false;

                }
            }
        });

        setTitle("보호자 관리");

//        String[] nok = {"엄마", "아빠"};

        ListView lv = (ListView) findViewById(R.id.phoneNumList);

        InputData1.put("nok", "엄마");
        InputData1.put("num", "010-0000-0000");
        Data.add(InputData1);

        InputData2.put("nok", "아빠");
        InputData2.put("num", "010-1111-1111");
        Data.add(InputData2);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, Data, android.R.layout.simple_list_item_2, new String[]{"nok", "num"}, new int[]{android.R.id.text1, android.R.id.text2});
        lv.setAdapter(simpleAdapter);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nok);
//        lv.setAdapter(adapter);
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), nok[i], Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}