package com.example.musicplayer.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.musicplayer.LocalMusicActivity;
import com.example.musicplayer.R;

import java.util.List;

public class MusicFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private final Context mContext;
    private List<String> title_list;

    public MusicFragmentAdapter(Context context, FragmentManager fm, List<Fragment> fragmentList, List<String> title_list) {
        super(fm);
        this.fragmentList = fragmentList;
        this.mContext = context;
        this.title_list = title_list;

    }

    public MusicFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.title_list.get(position);
    }
}