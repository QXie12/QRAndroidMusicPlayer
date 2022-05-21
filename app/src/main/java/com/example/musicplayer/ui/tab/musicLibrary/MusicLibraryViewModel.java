package com.example.musicplayer.ui.tab.musicLibrary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MusicLibraryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MusicLibraryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("乐库页面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}