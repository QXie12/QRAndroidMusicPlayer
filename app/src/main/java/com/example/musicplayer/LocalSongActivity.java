package com.example.musicplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import com.example.musicplayer.adapter.MusicFragmentAdapter;
import com.example.musicplayer.adapter.TabFragmentAdapter;
import com.example.musicplayer.common.MusicUtil;
import com.example.musicplayer.databinding.ActivityLocalsongBinding;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.example.musicplayer.ui.localMusic.AlbumFragment;
import com.example.musicplayer.ui.localMusic.SingerFragment;
import com.example.musicplayer.ui.localMusic.SongFragment;
import com.example.musicplayer.ui.tab.library.LibraryFragment;
import com.example.musicplayer.ui.tab.mine.MineFragment;
import com.example.musicplayer.ui.tab.radio.RadioFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LocalSongActivity extends AppCompatActivity {

    ActivityLocalsongBinding binding;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    private List<Fragment> mFragments;


    //读取歌曲
    MusicUtil musicUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLocalsongBinding.inflate(getLayoutInflater());
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setContentView(binding.getRoot());
        Log.e("初始化前","本地音乐");
        initTab();
        Log.e("初始化后","本地音乐");
        MusicUtil musicUtil= new MusicUtil(this);
    }

    private void initTab(){

        //初始化分页面的 Fragment，并将其添加到列表中
        initFragment();
        List<String> title_list = new ArrayList<>();
        title_list.add("单曲");
        title_list.add("歌手");
        title_list.add("专辑");
        title_list.add("文件夹");

        //实例化 FragmentPagerAdapter 并将 Fragment 列表传入
        MusicFragmentAdapter adapter = new MusicFragmentAdapter(this, getSupportFragmentManager(), mFragments, title_list);
        mViewPager = binding.localViewpager;
        //将实例化好的 Adapter 设置到 ViewPager 中
        mViewPager.setAdapter(adapter);
        mTabLayout = binding.localTabLayout;
        //将 ViewPager 绑定到 TabLayout上
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //添加每个切换页面的Fragment
    private void initFragment(){
        mFragments = new ArrayList<>();
        mFragments.add(SongFragment.newInstance(1));
//        mFragments.add(SongFragment.newInstance(2));

        mFragments.add(SingerFragment.newInstance(2));
        mFragments.add(AlbumFragment.newInstance(3));
        mFragments.add(SingerFragment.newInstance(4));

    }
}