package com.example.musicplayer.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

//切换我的歌单和收藏歌单的tab的adapter

public class SongListFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private final Context mContext;
    private List<String> title_list;

    public SongListFragmentAdapter(Context context, FragmentManager fm, List<Fragment> fragmentList, List<String> title_list) {
        super(fm);
        this.fragmentList = fragmentList;
        this.mContext = context;
        this.title_list = title_list;

    }



    public SongListFragmentAdapter(Context context, FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.mContext = context;
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