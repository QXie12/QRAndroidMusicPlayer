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

import com.example.musicplayer.adapter.MusicFragmentAdapter;
import com.example.musicplayer.adapter.TabFragmentAdapter;
import com.example.musicplayer.databinding.ActivityLocalsongBinding;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.example.musicplayer.ui.localMusic.SingerFragment;
import com.example.musicplayer.ui.localMusic.SongFragment;
import com.example.musicplayer.ui.tab.library.LibraryFragment;
import com.example.musicplayer.ui.tab.mine.MineFragment;
import com.example.musicplayer.ui.tab.radio.RadioFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends AppCompatActivity {

    ActivityLocalsongBinding binding;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;

    private String[] mTitles = {"单曲","专辑","歌手","文件夹"};
    private int[] mImages = {R.drawable.ic_menu_slideshow,
            R.drawable.ic_menu_gallery,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLocalsongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.e("初始化前","本地音乐");
        initTab();
        Log.e("初始化后","本地音乐");

//        MusicFragmentAdapter musicFragmentAdapter = new MusicFragmentAdapter(this, getSupportFragmentManager(),mFragments);
        //实例化组件
//        mViewPager = binding.localViewpager;
//        mViewPager.setAdapter(musicFragmentAdapter);
//        mTabLayout = binding.localTabLayout;
//        mTabLayout.setupWithViewPager(mViewPager);



    }

    private void initTab(){

        //初始化分页面的 Fragment，并将其添加到列表中
        initFragment();
        List<String> title_list = new ArrayList<>();
        title_list.add("单曲");
        title_list.add("专辑");
        title_list.add("歌手");
        title_list.add("文件夹");



        //实例化 FragmentPagerAdapter 并将 Fragment 列表传入
        MusicFragmentAdapter adapter = new MusicFragmentAdapter(this, getSupportFragmentManager(), mFragments, title_list);
        mViewPager = binding.localViewpager;
        //将实例化好的 Adapter 设置到 ViewPager 中
        mViewPager.setAdapter(adapter);
        mTabLayout = binding.localTabLayout;
        //将 ViewPager 绑定到 TabLayout上
        mTabLayout.setupWithViewPager(mViewPager);

//        //进行 Tab自定义布局的实例化和添加
//        for(int i = 0; i < 4; i++){
//            View view = LayoutInflater.from(this).inflate(R.layout.style_tab, null);
//            ImageView imageView = (ImageView)view.findViewById(R.id.tabImageView);
//            TextView textView = (TextView)view.findViewById(R.id.tabTextView);
//            textView.setText(mTitles[i]);
//            imageView.setImageResource(mImages[i]);
//
//            //将实例化好的 Tab 布局设置给当前的 Tab即可
//            mTabLayout.getTabAt(i).setCustomView(view);
//
//
//        }
    }

    //添加每个切换页面的Fragment
    private void initFragment(){
        mFragments = new ArrayList<>();
        mFragments.add(SongFragment.newInstance(1));
        mFragments.add(SingerFragment.newInstance("aaa","bbb"));
        mFragments.add(SongFragment.newInstance(3));
        mFragments.add(SongFragment.newInstance(4));

    }
}