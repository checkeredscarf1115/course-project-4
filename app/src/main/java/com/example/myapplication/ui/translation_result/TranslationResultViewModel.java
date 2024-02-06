package com.example.myapplication.ui.translation_result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TranslationResultViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public TranslationResultViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is translation result fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
