package com.example.narcolepsyproject.db.setting;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.narcolepsyproject.db.contact.ContactData;

import java.util.List;

@Dao
public interface SettingDao {

    @Insert(onConflict = REPLACE)  // 새로운 데이터를 추가 할 때 중복된 기본키를 가진 데이터가 있으면 삭제하고 새로운 키롣 ㅐ체
    void insert(SettingData settingData);

    @Delete
    void delete(SettingData settingData);


    @Query("SELECT * FROM setting")
    List<SettingData> getAllSettingData();

    @Query("SELECT repeatCount FROM setting")
    Integer getRepeatCountData();

    @Query("SELECT activate FROM setting")
    Boolean getActivateData();


}
