package com.example.musicplayer.ui.tab.library;

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

import com.example.musicplayer.databinding.FragmentLibraryBinding;

public class LibraryFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView mTextView;
    private LibraryViewModel radioViewModel;
    private FragmentLibraryBinding binding;

    public static LibraryFragment newInstance(int index){
        LibraryFragment fragment = new LibraryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        radioViewModel.setIndex(index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.libraryText;

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

