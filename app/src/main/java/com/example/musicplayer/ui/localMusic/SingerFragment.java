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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingerFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    //获取左边控件以及侧边字母索引控件
    RecyclerView recyclerView;
    LetterSideView letterSideView;
    //主要用于展示数据的list
    private List<MusicInfoModel> list;
    //页面recyclerview的适配器
    private SingerAdapter singerAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;
    //第一次加载之后缓存的数据
    private List<MusicInfoModel> cacheList;


    public SingerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SingerFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        //        拿到了音乐列表
        list = new ArrayList<>();
        this.list = MusicUtil.getMusicList();

        cacheList = new ArrayList<>();
        List<MusicInfoModel> musicInfoModelList = bindData(list);

        //设置分割线
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), new MyItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                //这个是用来比较是否是同一组数据的
                return list.get(position).getSortSingerId();
            }

            @Override
            public String getGroupName(int position) {
                MusicInfoModel MusicInfoModel = list.get(position);
                //拼音都是小写的
                return MusicInfoModel.getSortSingerId().toUpperCase();
            }
        }));

        singerAdapter = new SingerAdapter(getContext(), musicInfoModelList);
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
    private void moveToLetterPosition(String letter) {
        //这里主要是为了跳转到最顶端
        if ("#".equals(letter)) {
            letter = "*";
        }
        for (int i = 0; i < list.size(); i++) {
            MusicInfoModel MusicInfoModel = list.get(i);
            if (MusicInfoModel.getSortSingerId().toUpperCase().equals(letter)) {
                layoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

//    /**
//     * 给View绑定数据
//     * @param allMusic 所有歌曲列表
//     */
//    public List<MusicInfoModel> bindData(List<MusicInfoModel> allMusic) {
//        //排序
//        Collections.sort(allMusic, new Comparator<MusicInfoModel>() {
//            @Override
//            public int compare(MusicInfoModel o1, MusicInfoModel o2) {
//                return o1.getSortSingerName().compareToIgnoreCase(o2.getSortSingerName());
//            }
//        });
//        for(MusicInfoModel musicInfoModel:allMusic){
//            Log.e("排序后的歌曲",musicInfoModel.getSinger()+" " + musicInfoModel.getSortSingerName() + " " + musicInfoModel.getSortSingerId());
//        }
//        return allMusic;
//    }



    /**
     * 给View绑定数据
     *
     * @param allMusic 所有歌曲列表
     */
    public List<MusicInfoModel> bindData(List<MusicInfoModel> allMusic) {
        if (allMusic != null) {
            for (MusicInfoModel musicModel : allMusic) {
//                try {
                    cacheList.add(createMusic(musicModel));
//                }catch (PinyinException e){
//                    e.printStackTrace();
//                }
            }
            //排序
            Collections.sort(cacheList, new Comparator<MusicInfoModel>() {
                @Override
                public int compare(MusicInfoModel o1, MusicInfoModel o2) {
                    return o1.getSortSingerName().compareToIgnoreCase(o2.getSortSingerName());
                }
            });
            this.list.clear();
            this.list.addAll(cacheList);

            for(MusicInfoModel musicInfoModel: list){
                Log.e("打印按歌手排序完的歌曲",musicInfoModel.getSinger()+" " +musicInfoModel.getSortSingerId() + " " + musicInfoModel.getSortSingerName());
            }

        }
        return list;
    }


    //将传入的歌改成有排序id和排序名字的
//    public MusicInfoModel createMusic(MusicInfoModel music) throws PinyinException {
//        if(MusicUtil.checkFirstIsEnglish(music.getSinger())){
//            String name = music.getSinger();
//            music.setSortSingerId(""+Character.toLowerCase(name.charAt(0)));
//            music.setSortSingerName(name);
//        }else{
//            String pingYin = PinyinHelper.convertToPinyinString(music.getMusicName(), " ", PinyinFormat.WITHOUT_TONE);
//            Log.e("转换",pingYin);
//            music.setSortSingerId( pingYin.substring(0, 1));
//            music.setSortSingerName(pingYin);
//        }
//        return music;
//    }


        public  MusicInfoModel  createMusic(MusicInfoModel music){
            Log.e("判断歌手的排序"," "+music.getSinger()+"第一个字符"+music.getSinger().charAt(0));
            String firstChar = music.getSinger().charAt(0)+"";
            if(MusicUtil.checkFirstIsEnglish(firstChar) || firstChar.matches("[\\u4E00-\\u9FA5]")) {
                if (MusicUtil.checkFirstIsEnglish(music.getSinger())) {
                    String singerName = music.getSinger();
                    music.setSortSingerId("" + Character.toLowerCase(singerName.charAt(0)));
                    music.setSortSingerName(singerName);
                } else {
                    try {
                        String pingYin = PinyinHelper.convertToPinyinString(music.getSinger(), " ", PinyinFormat.WITHOUT_TONE);
                        Log.e("转换", pingYin);
                        music.setSortSingerId(pingYin.substring(0, 1));
                        music.setSortSingerName(pingYin);
                    }catch (PinyinException e){
                        e.printStackTrace();
                    }
                }
            }else{
                music.setSortSingerId("#");
                music.setSortSingerName(music.getSinger());
                Log.e("以其他开头的歌手",music.getSortSingerId()+" "+music.getSortSingerName());
            }
            return music;
    }
}