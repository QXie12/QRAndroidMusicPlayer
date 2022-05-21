package com.example.musicplayer.ui.tab.musicLibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.R;
import com.example.musicplayer.ui.tab.mine.MineFragment;

public class MusicLibraryFragment extends Fragment {

    private TextView mTextView;

    public static MineFragment newInstance(String title){
        Bundle arguments = new Bundle();
        arguments.putString("TITLE", title);
        MineFragment fragment = new MineFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mTextView = (TextView) root.findViewById(R.id.fragment_main);
        mTextView.setText(getArguments().getString("TITLE"));

        return root;
    }





}
