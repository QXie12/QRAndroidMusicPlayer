package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.adapter.SongMusicAdapter;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.bean.SongList;
import com.example.musicplayer.common.MusicUtil;
import com.example.musicplayer.widget.LetterSideView;
import com.example.musicplayer.widget.MyItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//给空白歌单添加歌曲时，用于显示可以添加的歌曲的列表的activity
public class MusicListActivity extends AppCompatActivity {
    //页面相关内容
    private Toolbar toolbar;
    private LetterSideView letterSideView;
    private RecyclerView recyclerView ;
    //布局管理器
    private LinearLayoutManager layoutManager;
    //页面recyclerview的适配器
    private SongMusicAdapter songMusicAdapter;

    //主要用于展示数据的list
    private List<MusicInfoModel> list;

    //要添加歌曲的歌单的信息
    private static SongList songList;

    public static final String action = "notition";
    //歌单的歌曲的list
//    private static List<MusicInfoModel> songListMusic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        //一进来接收一个歌单对象，当点击相应的添加歌单的时候就把歌曲放到这个对象里面
        //获取数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //拿到需要更改的歌单
        songList = (SongList) bundle.getSerializable("SongList");
        if(songList.getMusicList() == null){
            System.out.println("应该不会进来");
        }
//        else {
//            songListMusic= songList.getMusicList();
//        }


//        顶部toolbar
        toolbar = findViewById(R.id.music_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(action);
                intent.putExtra("SongList", (Serializable) songList);
//                intent.putExtra("SongList", "nihao");
                sendBroadcast(intent);
                finish();
                //todo 返回到歌单页面的时候，把改过的songlist传回去
//                Intent myIntent = new Intent(MusicListActivity.this, SongListActivity.class);
//                //传递数据
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("SongList",(Serializable) songList);
//                myIntent.putExtras(bundle);
//                Log.e("我穿了新的歌单去了",songList.getSongListName()+" " + songList.getMusicList());
//                //启动新的intent
//                startActivity(myIntent);
            }
        });

//        侧边栏和音乐展示
        letterSideView = findViewById(R.id.musicIndexView);
        recyclerView = findViewById(R.id.music_list);
        //管理视图渲染，线性的
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //初始化Adapter
        initAdapter();
        //初始化侧边栏移动的监听器
        initListener();
    }

    private void initAdapter() {
        list = MusicUtil.getMusicList();
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

        songMusicAdapter = new SongMusicAdapter(this, MusicInfoModels);
        recyclerView.setAdapter(songMusicAdapter);
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

    public static boolean addMusicToThisSongList(MusicInfoModel musicInfoModel){
        return MusicUtil.addNormalMusic(songList,musicInfoModel);
//        songList.getMusicList().add(musicInfoModel);
//        //todo 还要操作一次数据库
//        MusicUtil.addSongToSongList(songList.getSongListName(),musicInfoModel);

    }

    public static SongList getSongList(){
        return songList;
    }


}