package com.example.narcolepsyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.example.narcolepsyproject.db.contact.ContactAdapter;
//import com.example.narcolepsyproject.db.contact.ContactData;
import com.example.narcolepsyproject.db.RoomDB;
import com.example.narcolepsyproject.db.contact.ContactAdapter;
import com.example.narcolepsyproject.db.contact.ContactData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class NokActivity extends AppCompatActivity {
//    TextView textView; // retrofit

    RecyclerView recyclerView;
    Button btnAdd;
    EditText editNameText,editPhoneNumberText;

    //데이터베이스
    List<ContactData> dataList = new ArrayList<>();
    RoomDB database;
    ContactAdapter adapter;


    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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

        btnAdd = findViewById(R.id.btn_add); // 연락처 추가 버튼
        editNameText = findViewById(R.id.editNameText);
        editPhoneNumberText= findViewById(R.id.editPhoneNumberText);
        recyclerView = findViewById(R.id.recycler_view); //activity_nok - recycler_view

        database = RoomDB.getInstance(this);

        dataList = database.mainDao().getAll();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactAdapter(NokActivity.this, dataList);

        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editNameText.getText().toString().trim();
                String phoneNumber = editPhoneNumberText.getText().toString().trim();
                if (!name.equals("")) {
                    ContactData data = new ContactData();
                    data.setName(name);
                    data.setPhoneNumber(phoneNumber);
                    database.mainDao().insert(data);

                    Log.d("Data", "Inserted: " + data.getName() + ", " + data.getPhoneNumber());


                    editNameText.setText("");
                    editPhoneNumberText.setText("");

                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();

                }


                editNameText.setText("");
                editPhoneNumberText.setText("");
                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();


            }
        });
    }


}