//package com.example.myapplication.domain;
//
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import java.util.List;
//
//@Dao
//public interface DicDao {
//    @Query("SELECT * FROM dicEntity")
//    List<DicEntity> getDics();
//
//    @Query("SELECT id_saved_expressions FROM dicEntity WHERE name=(:name)")
//    List<Integer> getSavedExprs(String name);
//
//    @Query("SELECT DISTINCT name FROM dicEntity")
//    List<String> getDicNames();
//
//    @Insert
//    void insertAll(DicEntity... dicEntities);
//
//    @Delete
//    void delete(DicEntity dicEntity);
//}
