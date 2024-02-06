//package com.example.myapplication.ui.gallery;
//
//import android.app.Application;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.myapplication.domain.bookmark.databases.BookmarkDB;
//
//import java.util.List;
//
//public class GalleryViewModel extends ViewModel {
//    private BookmarkDB bookmarkDB;
//
//    public GalleryViewModel(Application application) {
//        bookmarkDB = BookmarkDB.getInstance(application.getApplicationContext());
//    }
//
//    public LiveData<List<Integer>> getLiveWords() {
//        return bookmarkDB.wordDao().getLiveWords();
//    }
//}