package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
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
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private List<Fragment> mFragments;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private String[] mTitles = {"我的","乐库","电台"};
    private int[] mImages = {R.drawable.ic_menu_slideshow, R.drawable.ic_menu_gallery, R.drawable.ic_menu_camera};

    //读取歌曲
    MusicUtil musicUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

}