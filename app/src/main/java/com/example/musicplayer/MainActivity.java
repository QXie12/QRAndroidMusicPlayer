package com.example.musicplayer;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
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
import com.example.musicplayer.bean.SongList;
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
//???Activity????????????????????????tab??????????????????
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
    private String[] mTitles = {"??????","??????","??????"};
    private int[] mImages = {R.drawable.ic_new_singer, R.drawable.ic_cidai, R.drawable.ic_recommend};

    //????????????
    MusicUtil musicUtil;

    //??????????????????
    public Boolean checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            Log.i("??????????????????"," ??? "+isGranted);
            if (!isGranted) {
                this.requestPermissions(
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                102);
            }
        }
        return isGranted;
    }



    //?????????????????????cardview
    public static Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            //????????????service???????????????????????????????????????????????????
            Log.d(TAG,"???main????????????");
            if(MusicUtil.getAlbumArtByPath(String.valueOf(msg.getData().get("path")))==null){
                music_image.setImageResource(R.drawable.gai);
            }else {
                music_image.setImageBitmap(MusicUtil.getAlbumArtByPath(String.valueOf(msg.getData().get("path"))));
            }
            System.out.println("bitmap????????????"+MusicUtil.getAlbumArtByPath(String.valueOf(msg.getData().get("path"))));

            music_name.setText(String.valueOf(msg.getData().get("musicName")));
            music_singer.setText(String.valueOf(msg.getData().get("singer")));
            main_isPlay.setImageResource(R.drawable.ic_stop); //??????????????????
            return false;
        }
    });

    //????????????
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //??????service
        Intent intent = new Intent(MainActivity.this,MusicService.class);
//        startService(intent);
        bindService(intent,sc, BIND_AUTO_CREATE);

        setSupportActionBar(binding.appBarMain.toolbar);
        IntentFilter filter = new IntentFilter(MusicListActivity.action);
        registerReceiver(broadcastReceiver, filter);

        //?????????
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //????????????
        music_image = findViewById(R.id.music_image);
        music_name = findViewById(R.id.music_name);
        music_singer = findViewById(R.id.music_author);
        main_isPlay = findViewById(R.id.main_isPlay);
        main_isPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????/??????
                if(MusicService.isPlay){
                    main_isPlay.setImageResource(R.drawable.ic_play); //????????????
                    MusicService.isPlay = false;
                    mm.pause();
                }else {
                    main_isPlay.setImageResource(R.drawable.ic_stop); //????????????
                    if(!mm.isChanged()){
                        mm.play(); //???????????????????????????
                    }else{
                        MusicService.isPlay = true;
                        mm.start();//??????????????????????????????????????????
                    }


                }
            }
        });

        //??????????????????????????? ??????????????????
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_gallery:click_home();
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


        //????????????
        checkPermission();
        //?????????
        initView();

        //???????????????
        if(MusicUtil.getMusicList().size()>0){
            //donothing
        }else{
            musicUtil= new MusicUtil(getApplicationContext());
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.d(TAG,"?????????????????????");
        }
    };

    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //??????????????????
            System.out.println("?????????service?????????");
            mm = (MusicService.MyBinder)iBinder;
            //todo main????????????????????????????????????????????????????????????????????????????????????
            if(mm.exist()){
                if (MusicUtil.getAlbumArtByPath(mm.getPath())==null){
                    Log.d(TAG,"??????");
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
                    main_isPlay.setImageResource(R.drawable.ic_stop);//??????????????????
                }else {
                    //????????????????????????
                    //TODO ??????????????????
                    main_isPlay.setImageResource(R.drawable.ic_play);//?????????
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //?????????
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    //????????????????????????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //???????????????????????????????????????
    private void initView(){
        //???????????????
        mViewPager = binding.appBarMain.tabViewPager;
        mTabLayout = binding.appBarMain.tabLayout;

        //????????????????????? Fragment??????????????????????????????
        initFragment();

        //????????? FragmentPagerAdapter ?????? Fragment ????????????
        TabFragmentAdapter adapter = new TabFragmentAdapter(this, getSupportFragmentManager(), mFragments);
        //?????????????????? Adapter ????????? ViewPager ???
        mViewPager.setAdapter(adapter);
        //??? ViewPager ????????? TabLayout???
        mTabLayout.setupWithViewPager(mViewPager);

        //?????? Tab????????????????????????????????????
        for(int i = 0; i < 3; i++){
            //????????? Tab ??????
            View view = LayoutInflater.from(this).inflate(R.layout.style_tab, null);
            ImageView imageView = (ImageView)view.findViewById(R.id.tabImageView);
            TextView textView = (TextView)view.findViewById(R.id.tabTextView);
            textView.setText(mTitles[i]);
            imageView.setImageResource(mImages[i]);

            //?????????????????? Tab ???????????????????????? Tab??????
            mTabLayout.getTabAt(i).setCustomView(view);
        }
    }

    //???????????????????????????Fragment
    private void initFragment(){
        mFragments = new ArrayList<>();
        mFragments.add(MineFragment.newInstance(1));
        mFragments.add(LibraryFragment.newInstance(2));
        mFragments.add(RadioFragment.newInstance(3));
    }

    //??????????????????
    public void click_home() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    //????????????????????????
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cardView:
                Log.d(TAG,"??????cardview");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MusicActivity.class);
                startActivity(intent);
                break;
        }
    }

}