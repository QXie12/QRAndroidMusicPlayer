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
import com.example.musicplayer.adapter.AlbumAdapter;
import com.example.musicplayer.adapter.SingerAdapter;
import com.example.musicplayer.bean.Album;
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


public class AlbumFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    //获取左边控件以及侧边字母索引控件
    RecyclerView recyclerView;
    LetterSideView letterSideView;
    //页面recyclerview的适配器
    private AlbumAdapter albumAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;

    private List<Album> albumsList;

    public AlbumFragment() {
        // Required empty public constructor
    }

    public static AlbumFragment newInstance(int index) {
        AlbumFragment fragment = new AlbumFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);

        //获得recyclerList和侧边栏
        recyclerView = rootView.findViewById(R.id.album_list);
        letterSideView = rootView.findViewById(R.id.albumIndexView);
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
        albumsList = new ArrayList<>();
        albumsList = MusicUtil.getAllAlbumList();
        for(Album album: albumsList){
            setSortName(album);
        }

        Collections.sort(albumsList, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                return o1.getSortAlbumId().compareToIgnoreCase(o2.getSortAlbumId());
            }
        });

        for(Album album: albumsList){
            Log.e("专辑",album.getAlbumName());
            for(MusicInfoModel musicInfoModel: album.getMusicList()){
                Log.e("歌曲",musicInfoModel.getMusicName());
            }
        }

        //设置分割线
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), new MyItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                return albumsList.get(position).getSortAlbumId();
            }

            @Override
            public String getGroupName(int position) {
                Album album = albumsList.get(position);
                return album.getSortAlbumId().toUpperCase();
            }
        }));


        albumAdapter = new AlbumAdapter(getContext(), albumsList);
        recyclerView.setAdapter(albumAdapter);
    }

    private void initListener() {
        letterSideView.setListener(new LetterSideView.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                moveToLetterPosition(letter);
            }
        });
    }

    private void moveToLetterPosition(String letter) {
        //这里主要是为了跳转到最顶端
        if ("#".equals(letter)) {
            letter = "*";
        }
        for (int i = 0; i < albumsList.size(); i++) {
            Album album = albumsList.get(i);
            if (album.getSortAlbumId().toUpperCase().equals(letter)) {
                layoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    private void setSortName(Album album){
        String firstChar = album.getAlbumName().charAt(0)+"";
        if(MusicUtil.checkFirstIsEnglish(firstChar) || firstChar.matches("[\\u4E00-\\u9FA5]")) {
            if (MusicUtil.checkFirstIsEnglish(album.getAlbumName())) {
                String albumName = album.getAlbumName();
                album.setSortAlbumId("" + Character.toLowerCase(albumName.charAt(0)));
                album.setSortAlbumName(albumName);
            } else {
                try {
                    String pingYin = PinyinHelper.convertToPinyinString(album.getAlbumName(), " ", PinyinFormat.WITHOUT_TONE);
                    album.setSortAlbumId(pingYin.substring(0, 1));
                    album.setSortAlbumName(pingYin);
                }catch (PinyinException e){
                    e.printStackTrace();
                }
            }
        }else{
            album.setSortAlbumId("#");
            album.setSortAlbumName(album.getAlbumName());
        }
    }
}