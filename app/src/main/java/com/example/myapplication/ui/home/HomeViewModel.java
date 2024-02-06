package com.example.myapplication.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
//    private final MutableLiveData<String> spinnerFromItemDF;
//    private final MutableLiveData<String> spinnerToItemDF;

    public HomeViewModel(Context context) {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

//        spinnerFromItemDF = new MutableLiveData<>();
//        spinnerFromItemDF.setValue(Locale.getDefault().getDisplayName().substring(Locale.getDefault().getDisplayName().indexOf(" ")));
//
//        spinnerToItemDF = new MutableLiveData<>();
//        String[] langArray = context.getResources().getStringArray(R.array.lang_items);
//        spinnerToItemDF.setValue(langArray[0]);
        // both spinners should be last used language(s)
        // save last used
    }



    public LiveData<String> getText() {
        return mText;
    }
}