package com.example.musicplayer.ui.recent;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.adapter.RecentAdapter;
import com.example.musicplayer.adapter.SingerAdapter;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.bean.Singer;
import com.example.musicplayer.common.MusicUtil;
import com.example.musicplayer.ui.localMusic.AlbumFragment;
import com.example.musicplayer.widget.MyItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecentSongFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    RecyclerView recyclerView;
    //todo 最近播放的音乐list，应该是要按时间排序的
    private List<MusicInfoModel> list;
    private CardView cardView;

    //页面recyclerview的适配器
    private RecentAdapter recentAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;
    public RecentSongFragment() {
    }

    public static RecentSongFragment newInstance(int index) {
        RecentSongFragment fragment = new RecentSongFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent_song, container, false);

        //获得recyclerList和侧边栏
        recyclerView = rootView.findViewById(R.id.recent_song_list);
        //管理视图渲染，线性的
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //播放栏
        cardView = rootView.findViewById(R.id.recent_play);
        //初始化Adapter
        initAdapter();
        return rootView;
    }

    private void initAdapter() {
        //todo 拿到最近播放的歌曲列表
        list = new ArrayList<>();
        list = MusicUtil.getAllRecentMusicList();

        if(list == null || list.size() <=0){
            cardView.setVisibility(View.INVISIBLE);
        }
        Collections.reverse(list);
//        Collections.sort(list, new Comparator<MusicInfoModel>() {
//            @Override
//            public int compare(MusicInfoModel o1, MusicInfoModel o2) {
//                return o1.getSortSongId().compareToIgnoreCase(o2.getSortSongId());
//            }
//        });


//        //设置分割线
//        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), new MyItemDecoration.TitleDecorationCallback() {
//            @Override
//            public String getGroupId(int position) {
//                return singerList.get(position).getSortSingerId();
//            }
//
//            @Override
//            public String getGroupName(int position) {
//                Singer singer = singerList.get(position);
//                return singer.getSortSingerId().toUpperCase();
//            }
//        }));


        recentAdapter = new RecentAdapter(getContext(), list);
        recyclerView.setAdapter(recentAdapter);
    }
}