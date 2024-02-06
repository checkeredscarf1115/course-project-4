package com.example.myapplication.domain.bookmark.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.domain.bookmark.daos.AntsDao;
import com.example.myapplication.domain.bookmark.daos.ContextsDao;
import com.example.myapplication.domain.bookmark.daos.SynsDao;
import com.example.myapplication.domain.bookmark.daos.WordDao;
import com.example.myapplication.domain.bookmark.entities.AntsEntity;
import com.example.myapplication.domain.bookmark.entities.ContextsEntity;
import com.example.myapplication.domain.bookmark.entities.SynsEntity;
import com.example.myapplication.domain.bookmark.entities.WordEntity;

@Database(entities={WordEntity.class, SynsEntity.class, AntsEntity.class, ContextsEntity.class}, version = 3)
public abstract class BookmarkDB extends RoomDatabase {
    public abstract WordDao wordDao();
    public abstract SynsDao synsDao();
    public abstract AntsDao antsDao();
    public abstract ContextsDao contextsDao();

    private static volatile BookmarkDB INSTANCE;

    public static BookmarkDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BookmarkDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookmarkDB.class, "bookmark_db")
                            //.fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
