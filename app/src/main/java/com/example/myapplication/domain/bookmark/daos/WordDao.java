package com.example.myapplication.domain.bookmark.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.domain.bookmark.entities.WordEntity;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM WordEntity")
    List<WordEntity> getWords();

    @Insert
    void insertAll(WordEntity... wordEntities);

    @Insert
    void insert(WordEntity wordEntity);

    @Delete
    void delete(WordEntity wordEntity);

    @Query("SELECT word_id FROM WordEntity WHERE word_def=(:wordDef)")
    List<Integer> getIDsByWord(String wordDef);

    @Query("SELECT 1 FROM WordEntity WHERE word_comp=(:wordComp) AND language_to=(:langTo)")
    List<Integer> checkIfLangExists(String wordComp, String langTo);

    @Query("SELECT * FROM WordEntity WHERE word_comp=(:wordComp)")
    List<WordEntity> getWords(String wordComp);

    @Query("SELECT 1 FROM WordEntity WHERE word_comp=(:wordComp)")
    List<Integer> checkIfWordExists(String wordComp);

    @Query("DELETE FROM WordEntity WHERE word_comp=(:wordComp)")
    abstract void deleteByWordComp(String wordComp);

    @Query("SELECT * FROM WordEntity LIMIT (:limit) OFFSET (:offset)")
    List<WordEntity> selectLimitOffset(int limit, int offset);
}
