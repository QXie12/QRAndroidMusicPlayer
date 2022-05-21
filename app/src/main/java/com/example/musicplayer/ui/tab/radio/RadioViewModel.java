package com.example.musicplayer.ui.tab.radio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RadioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RadioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("电台页面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}