package com.example.musicplayer.ui.tab.mine;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class MineViewModel extends ViewModel {

//    private final MutableLiveData<String> mText;
//
//    public MineViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("ๆ็้กต้ข");
//    }
//
//    public LiveData<String> getText() {
//        return mText;
//    }
//}
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {

        @Override
        public String apply(Integer input) {
            return "";
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}