package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.databinding.ActivityLocalsongBinding;
import com.example.musicplayer.databinding.ActivitySecondMusicBinding;
import com.example.musicplayer.widget.LetterSideView;
import com.example.musicplayer.widget.MyItemDecoration;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//二级播放页面的activity
public class SecondMusicActivity extends AppCompatActivity {
    //页面相关内容
    private Toolbar toolbar;
    private LetterSideView letterSideView;
    private RecyclerView recyclerView ;
    //布局管理器
    private LinearLayoutManager layoutManager;
    //页面recyclerview的适配器
    private MusicAdapter musicAdapter;

    //主要用于展示数据的list
    private List<MusicInfoModel> list;

    //读取传过来的数据
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_music);

        //获取数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = (String) bundle.getSerializable("name");
        list = (List<MusicInfoModel>)bundle.getSerializable("musicList");
        Log.e("已经跳转过来了",title);
        for(MusicInfoModel musicInfoModel : list){
            Log.e("歌曲",musicInfoModel.getMusicName()+" " + musicInfoModel.getBitmap());
        }


//        顶部toolbar
        toolbar = findViewById(R.id.second_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.e("here","?");
//        侧边栏和音乐展示
        letterSideView = findViewById(R.id.secondIndexView);
        recyclerView = findViewById(R.id.second_list);
        //管理视图渲染，线性的
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //初始化Adapter
        initAdapter();
        //初始化侧边栏移动的监听器
        initListener();
    }

    private void initAdapter() {
        Log.e("你来啦","?");

        List<MusicInfoModel> MusicInfoModels = bindData(list);

        //设置分割线
        recyclerView.addItemDecoration(new MyItemDecoration(this, new MyItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                //这个是用来比较是否是同一组数据的
                return list.get(position).getSortSongId();
            }

            @Override
            public String getGroupName(int position) {
                MusicInfoModel MusicInfoModel = list.get(position);
                //拼音都是小写的
                return MusicInfoModel.getSortSongId().toUpperCase();
            }
        }));

        musicAdapter = new MusicAdapter(this, MusicInfoModels);
        recyclerView.setAdapter(musicAdapter);
    }

    private void initListener() {
        letterSideView.setListener(new LetterSideView.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                moveToLetterPosition(letter);
            }
        });
    }

    //滚动recyclerview
    private void moveToLetterPosition(String letter) {
        //这里主要是为了跳转到最顶端
        if ("#".equals(letter)) {
            letter = "*";
        }
        for (int i = 0; i < list.size(); i++) {
            MusicInfoModel MusicInfoModel = list.get(i);
            if (MusicInfoModel.getSortSongId().toUpperCase().equals(letter)) {
                layoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    public List<MusicInfoModel> bindData(List<MusicInfoModel> allMusic) {
        if (allMusic != null) {
            //排序
            Collections.sort(allMusic, new Comparator<MusicInfoModel>() {
                @Override
                public int compare(MusicInfoModel o1, MusicInfoModel o2) {
                    return o1.getSortSongName().compareToIgnoreCase(o2.getSortSongName());
                }
            });
        }
        return allMusic;
    }


}