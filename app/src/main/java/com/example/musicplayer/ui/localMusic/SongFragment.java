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
import com.example.musicplayer.databinding.FragmentSongBinding;
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
    private TextView mTextView;
    private FragmentSongBinding binding;


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
        recyclerView = rootView.findViewById(R.id.recy_list);
        letterSideView = rootView.findViewById(R.id.fastIndexView);
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

        list = new ArrayList<>();
        this.list = MusicUtil.getMusicList();
        cacheList = new ArrayList<>();
//        list.add(new MusicInfoModel("喜欢你","邓紫棋","喜欢你",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("光年之外","邓紫棋","电影《太空旅客》主题曲",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("爱你","王心凌","My！Cyndi!",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("不能说的秘密","周杰伦","不能说的秘密-电影原声带",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("东风破","周杰伦","Initial J",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("爷爷泡的茶","周杰伦","八度空间",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("回到过去","周杰伦","八度空间",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("Celebrity","IU","Celebrity",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("经济舱","Kafe.Hu","中国新说唱2020第10期",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("年少有为","李荣浩","耳朵",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("贝贝","李荣浩","耳朵",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("裹着心的光","林俊杰","裹着心的光",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("那些你很冒险的梦","林俊杰","学不会",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("Lover","Taylor Swift","Lover",5,R.drawable.averter1));
//        list.add(new MusicInfoModel("成全","林宥嘉","我的第七感",5,R.drawable.averter1));



        List<MusicInfoModel> MusicInfoModels = bindData(list);

        //设置分割线
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), new MyItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                //这个是用来比较是否是同一组数据的
                return list.get(position).getSortId();
            }

            @Override
            public String getGroupName(int position) {
                MusicInfoModel MusicInfoModel = list.get(position);
                //拼音都是小写的
                return MusicInfoModel.getSortId().toUpperCase();
            }
        }));

        musicAdapter = new MusicAdapter(getContext(), MusicInfoModels);
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
            if (MusicInfoModel.getSortId().toUpperCase().equals(letter)) {
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
                    return o1.getSortName().compareToIgnoreCase(o2.getSortName());
                }
            });
            this.list.clear();
            this.list.addAll(cacheList);
        }
        return list;
    }


    //判断一首歌是否字母开头
    public static boolean checkFirstIsEnglish(String string){
        char c = string.charAt(0);
        if((c>='a'&&c<='z')   ||   (c>='A'&&c<='Z')) {
            return   true;
        }else{
            return   false;
        }
    }
    //将传入的歌改成有排序id和排序名字的
    public static MusicInfoModel createMusic(MusicInfoModel music) throws PinyinException {
        if(checkFirstIsEnglish(music.getMusicName())){
            String name = music.getMusicName();
            music.setSortId(""+Character.toLowerCase(name.charAt(0)));
            music.setSortName(name);
        }else{
            String pingYin = PinyinHelper.convertToPinyinString(music.getMusicName(), " ", PinyinFormat.WITHOUT_TONE);
            Log.e("转换",pingYin);
            music.setSortId( pingYin.substring(0, 1));
            music.setSortName(pingYin);
        }
        return music;
    }
}
