package com.example.musicplayer;

import static android.content.ContentValues.TAG;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.adapter.TabFragmentAdapter;
import com.example.musicplayer.common.MusicUtil;
import com.example.musicplayer.databinding.AppBarMainBinding;
import com.example.musicplayer.databinding.ContentMainBinding;
import com.example.musicplayer.ui.tab.mine.MineFragment;
import com.example.musicplayer.ui.tab.library.LibraryFragment;
import com.example.musicplayer.ui.tab.radio.RadioFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.musicplayer.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
//主Activity，包含三个底边的tab和一个侧边栏
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private List<Fragment> mFragments;
    private static MusicService.MyBinder mm;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private static CircleImageView music_image;
    private static TextView music_name;
    private static TextView music_singer;
    private static ImageView main_isPlay;
    private String[] mTitles = {"我的","乐库","电台"};
    private int[] mImages = {R.drawable.ic_menu_slideshow, R.drawable.ic_menu_gallery, R.drawable.ic_menu_camera};

    //读取歌曲
    MusicUtil musicUtil;

    public static Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            //下面接受service发送的消息，更新主页下面歌曲的信息
            Log.d(TAG,"给main发送消息");
            if(MusicUtil.getAlbumArtByPath(String.valueOf(msg.getData().get("path")))==null){
                music_image.setImageResource(R.drawable.gai);
            }else {
                music_image.setImageBitmap(MusicUtil.getAlbumArtByPath(String.valueOf(msg.getData().get("path"))));
            }
            System.out.println("bitmap有没有："+MusicUtil.getAlbumArtByPath(String.valueOf(msg.getData().get("path"))));

            music_name.setText(String.valueOf(msg.getData().get("musicName")));
            music_singer.setText(String.valueOf(msg.getData().get("singer")));
            main_isPlay.setImageResource(R.drawable.ic_music_on); //音乐开始播放
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //绑定service
        Intent intent = new Intent(MainActivity.this,MusicService.class);
//        startService(intent);
        bindService(intent,sc, BIND_AUTO_CREATE);

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
//        Toolbar toolbar = binding.appBarMain.toolbar;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        music_image = findViewById(R.id.music_image);
        music_name = findViewById(R.id.music_name);
        music_singer = findViewById(R.id.music_author);
        main_isPlay = findViewById(R.id.main_isPlay);
        main_isPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击暂停/播放
                if(MusicService.isPlay){
                    main_isPlay.setImageResource(R.drawable.music_suspend); //点击暂停
                    MusicService.isPlay = false;
                    mm.pause();
                }else {
                    main_isPlay.setImageResource(R.drawable.ic_music_on); //开始播放
                    if(!mm.isChanged()){
                        mm.play(); //第一首歌初始化需要
                    }else{
                        MusicService.isPlay = true;
                        mm.start();//后面的歌就是暂停之后再开始了
                    }


                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home: click_home();
                        break;
                    case R.id.nav_gallery:
                        break;
                    case R.id.nav_slideshow:
                        break;
                }
                drawer.closeDrawers();
                return false;
            }
        });

//        drawer.openDrawer(Gravity.LEFT);
//        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
//        drawerToggle.syncState();
//        drawer.addDrawerListener(drawerToggle);

        //标签页
        initView();

        if(MusicUtil.getMusicList().size()>0){

        }else{
            musicUtil= new MusicUtil(this);
        }
    }

    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //绑定后初始化
            System.out.println("初始化service。。。");
            mm = (MusicService.MyBinder)iBinder;
            //todo main里面下面歌曲那里初始化时可能要根据最近播放显示音乐在那里
            if(mm.exist()){
                if (MusicUtil.getAlbumArtByPath(mm.getPath())==null){
                    Log.d(TAG,"空的");
                }
                Log.d(TAG,"......."+mm.getPath());
                if(MusicUtil.getAlbumArtByPath(String.valueOf(mm.getPath()))==null){
                    music_image.setImageResource(R.drawable.gai);
                }else {
                    music_image.setImageBitmap(MusicUtil.getAlbumArtByPath(mm.getPath()));
                }

                music_name.setText(mm.getSongName());
                music_singer.setText(mm.getSinger());
                if(MusicService.isPlay){
                    main_isPlay.setImageResource(R.drawable.ic_music_on);//当前正在播放
                }else {
                    //设置成暂停的图片
                    //TODO 这里图片待改
                    main_isPlay.setImageResource(R.drawable.music_suspend);//暂停了
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //菜单栏
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initView(){
        //实例化组件
        mViewPager = binding.appBarMain.tabViewPager;
        mTabLayout = binding.appBarMain.tabLayout;


        //初始化分页面的 Fragment，并将其添加到列表中
        initFragment();

        //实例化 FragmentPagerAdapter 并将 Fragment 列表传入
        TabFragmentAdapter adapter = new TabFragmentAdapter(this, getSupportFragmentManager(), mFragments);
        //将实例化好的 Adapter 设置到 ViewPager 中
        mViewPager.setAdapter(adapter);
        //将 ViewPager 绑定到 TabLayout上
        mTabLayout.setupWithViewPager(mViewPager);

        //进行 Tab自定义布局的实例化和添加
        for(int i = 0; i < 3; i++){
            //实例化 Tab 布局
            View view = LayoutInflater.from(this).inflate(R.layout.style_tab, null);
            ImageView imageView = (ImageView)view.findViewById(R.id.tabImageView);
            TextView textView = (TextView)view.findViewById(R.id.tabTextView);
            textView.setText(mTitles[i]);
            imageView.setImageResource(mImages[i]);

            //将实例化好的 Tab 布局设置给当前的 Tab即可
            mTabLayout.getTabAt(i).setCustomView(view);
        }
    }

    //添加每个切换页面的Fragment
    private void initFragment(){
        mFragments = new ArrayList<>();
        mFragments.add(MineFragment.newInstance(1));
        mFragments.add(LibraryFragment.newInstance(2));
        mFragments.add(RadioFragment.newInstance(3));
    }

    public void click_home() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cardView:
                Log.d(TAG,"点了cardview");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MusicActivity.class);
                startActivity(intent);
                break;
        }
    }

}