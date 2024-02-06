//package com.example.myapplication.domain.bookmark;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import com.example.myapplication.domain.bookmark.entities.WordEntity;
//
//import java.util.List;
//
//@Dao
//public interface BookmarkDao {
//    @Query("SELECT * FROM WordEntity")
//    List<WordEntity> getWords();
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
