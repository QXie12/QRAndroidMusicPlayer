package com.example.musicplayer.ui.tab.mine;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.musicplayer.LocalSongActivity;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.RecentActivity;
import com.example.musicplayer.SecondMusicActivity;
import com.example.musicplayer.SongListActivity;
import com.example.musicplayer.adapter.SongListFragmentAdapter;
import com.example.musicplayer.adapter.TabFragmentAdapter;
import com.example.musicplayer.bean.SongList;
import com.example.musicplayer.common.MusicUtil;
import com.example.musicplayer.databinding.FragmentMineBinding;
import com.example.musicplayer.ui.SongList.MySongListFragment;
import com.example.musicplayer.ui.tab.library.LibraryFragment;
import com.example.musicplayer.ui.tab.radio.RadioFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView mTextView;
    private MineViewModel mineViewModel;
    private FragmentMineBinding binding;

    //下面的tab
    private List<Fragment> mFragments;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    //动画
    private AnimationDrawable AD;
    ImageView bannerImage;

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

        mTextView = binding.mainText;

        mineViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mTextView.setText(s);
            }
        });

        //顶部banner图片
        bannerImage = binding.bannerImage;
//        Glide.with(this)
//                .load(R.drawable.avatar10)
//                .into(bannerImage);
        bannerImage.setImageResource(R.drawable.fengmian_animation_list);

        AD = (AnimationDrawable) bannerImage.getDrawable();
        AD.setOneShot(false);


//        上方几个Button
        ImageButton recommend_music = binding.recommendMusic;
        ImageButton local_music = binding.localMusic;
        ImageButton download_music = binding.downloadMusic;
        ImageButton recent_music = binding.recentMusic;
        ImageButton favorite_music = binding.favoriteMusic;

        //查看本地音乐的按钮
        local_music.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                MainActivity mainActivity = (MainActivity) getActivity();
                intent.setClass(mainActivity, LocalSongActivity.class);
                startActivity(intent);
            }
        });

        //点击最近播放按钮
        recent_music.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                MainActivity mainActivity = (MainActivity) getActivity();
                intent.setClass(mainActivity, RecentActivity.class);
                startActivity(intent);
            }
        });
        //todo 点击我的最爱按钮，需要更改传递的歌单封面、歌单名称、歌单list信息
        favorite_music.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Intent myIntent = new Intent(mainActivity, SongListActivity.class);
                //传递数据
                Bundle bundle = new Bundle();
                bundle.putSerializable("SongList",(Serializable) MusicUtil.getMyFavoriteSongList());

//                bundle.putSerializable("songListName","我的最爱");
//                bundle.putSerializable("cover",R.drawable.avatar10);
//                bundle.putSerializable("musicList", (Serializable) MusicUtil.getAllFavoriteMusicList());
                myIntent.putExtras(bundle);
                //启动新的intent
                startActivity(myIntent);
            }
        });

        //标签页
        initView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView(){
        //实例化组件
        mViewPager = binding.songListViewPager;
        mTabLayout = binding.songListTablayout;

        //初始化分页面的 Fragment，并将其添加到列表中
        initFragment();
        List<String> title_list = new ArrayList<>();
        title_list.add("自建歌单");
        title_list.add("收藏歌单");


        //实例化 FragmentPagerAdapter 并将 Fragment 列表传入
        SongListFragmentAdapter adapter = new SongListFragmentAdapter(getContext(), getChildFragmentManager(), mFragments,title_list);
        //将实例化好的 Adapter 设置到 ViewPager 中
        mViewPager.setAdapter(adapter);
        //将 ViewPager 绑定到 TabLayout上
        mTabLayout.setupWithViewPager(mViewPager);
        }


    //添加每个切换页面的Fragment
    private void initFragment(){
        mFragments = new ArrayList<>();
        //todo 收藏歌单标签页待完成
        mFragments.add(MySongListFragment.newInstance(1));
        mFragments.add(MySongListFragment.newInstance(2));
    }

    @Override
     public void onStart(){
        startAnimation();
        super.onStart();
    }

    private void startAnimation() {
        AD.start();
        Animation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0);
        trans.setDuration(1000);
        trans.setRepeatCount(Animation.INFINITE);
        bannerImage.startAnimation(trans);
    }
    
    
    
}
