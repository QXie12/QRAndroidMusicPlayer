package com.example.musicplayer.ui.tab.radio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RadioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    public RadioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("电台页面");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

}