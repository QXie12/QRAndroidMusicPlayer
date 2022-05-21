package com.example.musicplayer.ui.tab.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicplayer.R;
import com.example.musicplayer.databinding.FragmentGalleryBinding;
import com.example.musicplayer.databinding.FragmentMainBinding;
import com.example.musicplayer.ui.gallery.GalleryViewModel;

public class MineFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView mTextView;
    private MineViewModel mineViewModel;
    private FragmentMainBinding binding;

    public static MineFragment newInstance(String title){
        MineFragment fragment = new MineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mineViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        mineViewModel.setIndex(index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mTextView = (TextView) root.findViewById(R.id.fragment_main);
        mTextView.setText(getArguments().getString("TITLE"));

        return root;
    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    
    
    
    
}
