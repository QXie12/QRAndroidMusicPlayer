package com.example.musicplayer.ui.localMusic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.bean.MusicInfoModel;
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

public class SongFragment extends Fragment {
    //获取左边控件以及侧边字母索引控件
    RecyclerView recyclerView;
    LetterSideView letterSideView;
    //主要用于展示数据的list
    private List<MusicInfoModel> list;
    //第一次加载之后缓存的数据
    private List<MusicInfoModel> cacheList;

    //页面recyclerview的适配器
    private MusicAdapter musicAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;

    private static final String ARG_SECTION_NUMBER = "section_number";


    public static SongFragment newInstance(int index) {
        SongFragment fragment = new SongFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_song, container, false);

        //获得recyclerList和侧边栏
        recyclerView = rootView.findViewById(R.id.song_list);
        letterSideView = rootView.findViewById(R.id.songIndexView);
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
//        拿到了音乐列表
        list = new ArrayList<>();
        this.list = MusicUtil.getMusicList();

        cacheList = new ArrayList<>();
        List<MusicInfoModel> musicInfoModels = bindData(list);

        //设置分割线
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), new MyItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                //这个是用来比较是否是同一组数据的
                return list.get(position).getSortSongId();
            }

            @Override
            public String getGroupName(int position) {
                MusicInfoModel musicInfoModel = list.get(position);
                //拼音都是小写的
                return musicInfoModel.getSortSongId().toUpperCase();
            }
        }));


        for(MusicInfoModel musicInfoModel: musicInfoModels){
            Log.e("测试排序完的歌曲",musicInfoModel.getMusicName()+" " +musicInfoModel.getSortSongId() + " " + musicInfoModel.getSortSongName());
        }
        musicAdapter = new MusicAdapter(getContext(), musicInfoModels);
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
//        if ("#".equals(letter)) {
//            letter = "*";
//        }
        for (int i = 0; i < list.size(); i++) {
            MusicInfoModel MusicInfoModel = list.get(i);
            if (MusicInfoModel.getSortSongId().toUpperCase().equals(letter)) {
                layoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    /**
     * 给View绑定数据
     *
     * @param allMusic 所有歌曲列表
     */
    public List<MusicInfoModel> bindData(List<MusicInfoModel> allMusic) {
        if (allMusic != null) {
            for (MusicInfoModel musicModel : allMusic) {
                try {
                    cacheList.add(createMusic(musicModel));
                }catch (PinyinException e){
                    e.printStackTrace();
                }
//                try {
//                    if(checkFirstIsEnglish(musicModel.getMusicName())){
//                        String name = musicModel.getMusicName();
//                        cacheList.add(new MusicInfoModel(musicModel.getMusicName(), ""+Character.toLowerCase(name.charAt(0)), name, musicModel.getSinger(), musicModel.getAlbum(), musicModel.getTime(), musicModel.getImage()));
//
//                    }else{
//                        String pingYin = PinyinHelper.convertToPinyinString(musicModel.getMusicName(), " ", PinyinFormat.WITHOUT_TONE);
//                        Log.e("转换",pingYin);
//                        cacheList.add(new MusicInfoModel(musicModel.getMusicName(), pingYin.substring(0, 1), pingYin, musicModel.getSinger(), musicModel.getAlbum(), musicModel.getTime(), musicModel.getImage()));
//                    }
//                } catch (PinyinException e) {
//                    e.printStackTrace();
//                }
            }
            //排序
            Collections.sort(cacheList, new Comparator<MusicInfoModel>() {
                @Override
                public int compare(MusicInfoModel o1, MusicInfoModel o2) {
                    return o1.getSortSongName().compareToIgnoreCase(o2.getSortSongName());
                }
            });
            this.list.clear();
            this.list.addAll(cacheList);
            for(MusicInfoModel musicInfoModel: list){
                Log.e("打印按歌曲排序完的歌曲",musicInfoModel.getMusicName()+" " +musicInfoModel.getSortSongId() + " " + musicInfoModel.getSortSongName() + " " + musicInfoModel.getSortSingerId() + " " + musicInfoModel.getSortSingerName());
            }
        }
        return list;
    }



    //将传入的歌改成有排序id和排序名字的
    public static MusicInfoModel createMusic(MusicInfoModel music) throws PinyinException {
        if(MusicUtil.checkFirstIsEnglish(music.getMusicName())){
            String name = music.getMusicName();
            music.setSortSongId(""+Character.toLowerCase(name.charAt(0)));
            music.setSortSongName(name);
        }else{
            String pingYin = PinyinHelper.convertToPinyinString(music.getMusicName(), " ", PinyinFormat.WITHOUT_TONE);
            Log.e("转换",pingYin);
            music.setSortSongId( pingYin.substring(0, 1));
            music.setSortSongName(pingYin);
        }
        return music;
    }
}
