package com.example.musicplayer.ui.localMusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.adapter.SingerAdapter;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.bean.Singer;
import com.example.musicplayer.common.MusicUtil;
import com.example.musicplayer.widget.LetterSideView;
import com.example.musicplayer.widget.MyItemDecoration;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SingerFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    //获取左边控件以及侧边字母索引控件
    RecyclerView recyclerView;
    LetterSideView letterSideView;
    //页面recyclerview的适配器
    private SingerAdapter singerAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;

    private List<Singer> singerList;


    public SingerFragment() {
        // Required empty public constructor
    }

    public static SingerFragment newInstance(int index) {
        SingerFragment fragment = new SingerFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_singer, container, false);

        //获得recyclerList和侧边栏
        recyclerView = rootView.findViewById(R.id.singer_list);
        letterSideView = rootView.findViewById(R.id.singerIndexView);
        //管理视图渲染，线性的
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //初始化Adapter
        initAdapter();
        //初始化侧边栏移动的监听器
        initListener();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void initAdapter() {
        singerList = new ArrayList<>();
        singerList = MusicUtil.getSingers();
        for(Singer singer: singerList){
            setSortName(singer);
        }

        Collections.sort(singerList, new Comparator<Singer>() {
            @Override
            public int compare(Singer o1, Singer o2) {
                return o1.getSortSingerId().compareToIgnoreCase(o2.getSortSingerId());
            }
        });

        for(Singer singer: singerList){
            Log.e("歌手名",singer.getSingerName());
            for(MusicInfoModel musicInfoModel: singer.getMusicList()){
                Log.e("歌曲",musicInfoModel.getMusicName());
            }
        }

        //设置分割线
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), new MyItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                return singerList.get(position).getSortSingerId();
            }

            @Override
            public String getGroupName(int position) {
                Singer singer = singerList.get(position);
                return singer.getSortSingerId().toUpperCase();
            }
        }));


        singerAdapter = new SingerAdapter(getContext(), singerList);
        recyclerView.setAdapter(singerAdapter);
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
//    private void moveToLetterPosition(String letter) {
//        //这里主要是为了跳转到最顶端
//        if ("#".equals(letter)) {
//            letter = "*";
//        }
//        for (int i = 0; i < list.size(); i++) {
//            MusicInfoModel MusicInfoModel = list.get(i);
//            if (MusicInfoModel.getSortSingerId().toUpperCase().equals(letter)) {
//                layoutManager.scrollToPositionWithOffset(i, 0);
//                return;
//            }
//        }
//    }

    private void moveToLetterPosition(String letter) {
        //这里主要是为了跳转到最顶端
        if ("#".equals(letter)) {
            letter = "*";
        }
        for (int i = 0; i < singerList.size(); i++) {
            Singer singer = singerList.get(i);
            if (singer.getSortSingerId().toUpperCase().equals(letter)) {
                layoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

        private void setSortName(Singer singer){
            String firstChar = singer.getSingerName().charAt(0)+"";
            if(MusicUtil.checkFirstIsEnglish(firstChar) || firstChar.matches("[\\u4E00-\\u9FA5]")) {
                if (MusicUtil.checkFirstIsEnglish(singer.getSingerName())) {
                    String singerName = singer.getSingerName();
                    singer.setSortSingerId("" + Character.toLowerCase(singerName.charAt(0)));
                    singer.setSortSingerName(singerName);
                } else {
                    try {
                        String pingYin = PinyinHelper.convertToPinyinString(singer.getSingerName(), " ", PinyinFormat.WITHOUT_TONE);
                        singer.setSortSingerId(pingYin.substring(0, 1));
                        singer.setSortSingerName(pingYin);
                    }catch (PinyinException e){
                        e.printStackTrace();
                    }
                }
            }else{
                singer.setSortSingerId("#");
                singer.setSortSingerName(singer.getSingerName());
            }
        }
}
