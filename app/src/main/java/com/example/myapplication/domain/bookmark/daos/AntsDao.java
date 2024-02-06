package com.example.myapplication.domain.bookmark.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.domain.bookmark.entities.AntsEntity;

import java.util.List;

@Dao
public interface AntsDao {
    @Query("SELECT * FROM AntsEntity")
    List<AntsEntity> getAnts();

    @Insert
    void insertAll(AntsEntity... antsEntities);

    @Insert
    void insert(AntsEntity antsEntity);

    @Delete
    void delete(AntsEntity antsEntity);

    @Query("SELECT * FROM AntsEntity WHERE word_id=(:wordID)")
    List<AntsEntity> getByWordID(int wordID);
}
