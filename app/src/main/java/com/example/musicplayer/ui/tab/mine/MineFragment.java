package com.example.musicplayer.ui.tab.mine;

import android.content.Intent;
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

import com.example.musicplayer.LocalSongActivity;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.databinding.FragmentMineBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView mTextView;
    private MineViewModel mineViewModel;
    private FragmentMineBinding binding;

    public static MineFragment newInstance(int index){
        MineFragment fragment = new MineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
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
        binding = FragmentMineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.mainText;

        mineViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        CircleImageView recommend_music = binding.recommendMusic;
        CircleImageView local_music = binding.recommendMusic;
        CircleImageView download_music = binding.recommendMusic;
        CircleImageView recent_music = binding.recommendMusic;
        CircleImageView favorite_music = binding.recommendMusic;

        recommend_music.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                MainActivity mainActivity = (MainActivity) getActivity();
                intent.setClass(mainActivity, LocalSongActivity.class);
                startActivity(intent);
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
