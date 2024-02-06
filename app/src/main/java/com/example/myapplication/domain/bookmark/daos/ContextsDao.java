package com.example.myapplication.domain.bookmark.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.domain.bookmark.entities.ContextsEntity;

import java.util.List;

@Dao
    public interface ContextsDao {
    @Query("SELECT * FROM ContextsEntity")
    List<ContextsEntity> getContexts();

    @Insert
    void insertAll(ContextsEntity... contextsEntities);

    @Insert
    void insert(ContextsEntity contextsEntity);

    @Delete
    void delete(ContextsEntity contextsEntity);

    @Query("SELECT * FROM ContextsEntity WHERE word_id=(:wordID)")
    List<ContextsEntity> getByWordID(int wordID);
}
