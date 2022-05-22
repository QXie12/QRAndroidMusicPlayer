package com.example.musicplayer.ui.tab.radio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicplayer.R;
import com.example.musicplayer.databinding.FragmentMineBinding;
import com.example.musicplayer.databinding.FragmentRadioBinding;
import com.example.musicplayer.ui.tab.mine.MineFragment;
import com.example.musicplayer.ui.tab.mine.MineViewModel;

public class RadioFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView mTextView;
    private RadioViewModel radioViewModel;
    private FragmentRadioBinding binding;

    public static RadioFragment newInstance(int index){
        RadioFragment fragment = new RadioFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioViewModel = new ViewModelProvider(this).get(RadioViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        radioViewModel.setIndex(index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRadioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.radioText;

        radioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

