package com.example.narcolepsyproject.db.contact;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ContactDao
{

    @Insert(onConflict = REPLACE)  // 새로운 데이터를 추가 할 때 중복된 기본키를 가진 데이터가 있으면 삭제하고 새로운 키롣 ㅐ체
    void insert(ContactData mainData);

    @Delete
    void delete(ContactData mainData);

    @Query("UPDATE contact SET name = :name, phoneNumber = :phoneNumber WHERE ID = :id")
    void update(int id, String name, String phoneNumber);

    @Query("SELECT * FROM contact")
    List<ContactData> getAll();
}