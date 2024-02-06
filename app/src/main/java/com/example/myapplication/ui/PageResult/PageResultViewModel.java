package com.example.myapplication.ui.PageResult;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageResultViewModel extends ViewModel {
    private MutableLiveData<String> words_sequence;

    public PageResultViewModel() {
    }

    public MutableLiveData<String> getWords(String wordDef, int number) {
        words_sequence = new MutableLiveData<>();
        words_sequence.setValue(String.format("%s. %s\n", number, wordDef));
        return words_sequence;
    }


}
