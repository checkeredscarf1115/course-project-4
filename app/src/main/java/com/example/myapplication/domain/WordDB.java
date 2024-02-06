//package com.example.myapplication.domain;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import androidx.annotation.NonNull;
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//
//@Database(entities={WordEntity.class}, version = 1)
//public abstract class WordDB extends RoomDatabase {
//    private static WordDB instance;
//    public abstract WordDao wordDao();
//
//    public static synchronized WordDB getInstance(Context context) {
//        if (instance == null) {
//            instance = Room.databaseBuilder(context.getApplicationContext(), WordDB.class, "word_db")
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
//                    .build();
//        }
//        return instance;
//    }
//
//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(instance).execute();
//        }
//    };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        PopulateDbAsyncTask(WordDB instance) {
//            WordDao dao = instance.wordDao();
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            return null;
//        }
//    }
//}
