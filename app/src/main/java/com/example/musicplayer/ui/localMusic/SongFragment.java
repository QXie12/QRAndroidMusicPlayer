package com.example.musicplayer.ui.localMusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.R;
import com.example.musicplayer.databinding.FragmentSongBinding;

public class SongFragment extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView mTextView;


    public static SongFragment newInstance(int index) {
        SongFragment fragment = new SongFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}