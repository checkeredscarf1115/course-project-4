//package com.example.myapplication.domain;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import java.util.List;
//
//@Dao
//public interface WordDao {
//    @Query("SELECT * FROM WordEntity")
//    List<WordEntity> getWords();
//
////    @Query("SELECT id_saved_expressions FROM dicEntity WHERE name=(:name)")
////    List<Integer> getSavedExprs(String name);
////
////    @Query("SELECT DISTINCT name FROM dicEntity")
////    List<String> getDicNames();
//
//    @Insert
//    void insertAll(WordEntity... wordEntities);
//
//    @Insert
//    void insert(WordEntity wordEntity);
//
//    @Delete
//    void delete(WordEntity wordEntity);
//}
