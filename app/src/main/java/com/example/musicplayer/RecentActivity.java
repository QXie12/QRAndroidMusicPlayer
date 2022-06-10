package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
//最近播放列表界面的activity，其中包含三个fragment，分别表示最近播放的单曲、最近播放的专辑、最近播放的歌单
public class RecentActivity extends AppCompatActivity {
    private ActivityRecentBinding binding;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    private List<Fragment> mFragments;
    private static MusicService.MyBinder mm;
    public static int isOpen = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isOpen = 1;
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
        Intent intent = new Intent(RecentActivity.this,MusicService.class);
//        startService(intent);
        bindService(intent,sc, BIND_AUTO_CREATE);
        //初始化标签页
        initTab();

    }
    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //绑定后初始化
            System.out.println("初始化service。。。");
            mm = (MusicService.MyBinder)iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
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
    public static void playByPath(String path){
        mm.play(path);
    }
    public static void setCurrent(int current){
        Log.e("我姓你了",mm+" ");
        mm.setCurrent(current);
    }

    //添加每个切换页面的Fragment
    private void initFragment(){
        mFragments = new ArrayList<>();
        //todo 最近播放的单曲、专辑、歌单
        mFragments.add(RecentSongFragment.newInstance(1));
        mFragments.add(SingerFragment.newInstance(2));
        mFragments.add(AlbumFragment.newInstance(3));

    }

    @Override
    protected void onDestroy() {
        isOpen = 0;
        super.onDestroy();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recent_menu, menu);
        MenuItem clearItem = menu.findItem(R.id.clear);

        clearItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                System.out.println("点击了清空按钮");

                return false;
            }
        });
        return true;
    }


}