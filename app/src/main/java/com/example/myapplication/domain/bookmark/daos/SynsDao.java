package com.example.myapplication.domain.bookmark.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.domain.bookmark.entities.SynsEntity;

import java.util.List;

@Dao
public interface SynsDao {
    @Query("SELECT * FROM SynsEntity")
    List<SynsEntity> getSyns();

    @Insert
        void insertAll(SynsEntity... synsEntities);

    @Insert
    void insert(SynsEntity synsEntity);

    @Delete
    void delete(SynsEntity synsEntity);

    @Query("SELECT * FROM SynsEntity WHERE word_id=(:wordID)")
    List<SynsEntity> getByWordID(int wordID);
}
