package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.musicplayer.adapter.MusicFragmentAdapter;
import com.example.musicplayer.databinding.ActivityLocalsongBinding;
import com.example.musicplayer.databinding.ActivityRecentBinding;
import com.example.musicplayer.ui.localMusic.AlbumFragment;
import com.example.musicplayer.ui.localMusic.FolderFragment;
import com.example.musicplayer.ui.localMusic.SingerFragment;
import com.example.musicplayer.ui.localMusic.SongFragment;
import com.example.musicplayer.ui.recent.RecentSongFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecentActivity extends AppCompatActivity {
    private ActivityRecentBinding binding;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    private List<Fragment> mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recent);
        binding = ActivityRecentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //工具栏返回
        toolbar = binding.recentToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //初始化标签页
        initTab();

    }
    private void initTab(){

        //初始化分页面的 Fragment，并将其添加到列表中
        initFragment();
        List<String> title_list = new ArrayList<>();
        title_list.add("单曲");
        title_list.add("歌单");
        title_list.add("专辑");

        //实例化 FragmentPagerAdapter 并将 Fragment 列表传入
        MusicFragmentAdapter adapter = new MusicFragmentAdapter(this, getSupportFragmentManager(), mFragments, title_list);
        mViewPager = binding.recentViewpager;
        //将实例化好的 Adapter 设置到 ViewPager 中
        mViewPager.setAdapter(adapter);
        mTabLayout = binding.recentTabLayout;
        //将 ViewPager 绑定到 TabLayout上
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //添加每个切换页面的Fragment
    private void initFragment(){
        mFragments = new ArrayList<>();
        mFragments.add(RecentSongFragment.newInstance(1));
        mFragments.add(SingerFragment.newInstance(2));
        mFragments.add(AlbumFragment.newInstance(3));

    }
}