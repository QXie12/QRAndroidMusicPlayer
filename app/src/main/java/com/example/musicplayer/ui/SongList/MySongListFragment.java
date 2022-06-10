package com.example.musicplayer.ui.SongList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.SongListActivity;
import com.example.musicplayer.adapter.RecentAdapter;
import com.example.musicplayer.adapter.SongListAdapter;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.bean.SongList;
import com.example.musicplayer.common.MusicUtil;
import com.example.musicplayer.ui.recent.RecentSongFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//自建歌单的fragment
public class MySongListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    RecyclerView recyclerView;
    //todo 歌单的list，数据库中读取到所有创建的歌单
    private List<SongList> list;

    //创建歌单和导入歌单的组件
    private ImageView createImage;
    private ImageView importImage;

    private CardView createCard;
    private CardView importCard;

    //页面recyclerview的适配器
    private SongListAdapter songListAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;
    public MySongListFragment() {
    }

    public static MySongListFragment newInstance(int index) {
        MySongListFragment fragment = new MySongListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_song_list, container, false);
        //创建歌单和导入歌单的一些控件
        createImage = rootView.findViewById(R.id.create_avatar);
        importImage = rootView.findViewById(R.id.import_avatar);

        //加载图片
        Glide.with(this).load(R.drawable.ic_create_new_folder).into(createImage);
        Glide.with(this).load(R.drawable.ic_share).into(importImage);

        createCard = rootView.findViewById(R.id.create_card);
        importCard = rootView.findViewById(R.id.import_card);

        // todo 创建歌单
        rootView.findViewById(R.id.create_layout).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Log.e("我要创建歌单","打开创建歌单的界面");
                EditText editText = new EditText(getActivity());
                editText.setHint("歌单名称（最大长度8个字符）");
                editText.setHintTextColor(R.color.gray2);
//                editText.setGravity(Gravity.CENTER_HORIZONTAL);
                new AlertDialog.Builder(getActivity()).setTitle("新建歌单")
                        .setIcon(R.drawable.ic_message)
                        .setView(editText)
                        .setPositiveButton("确定",  new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //todo 此处应该需要判断数据库中是否已经有这个歌单了，并且判断歌单名字是否合法
                                String title = editText.getText().toString();
                                System.out.println("点击确定按钮"+title);
                                //打开这个歌单的界面
                                MainActivity mainActivity = (MainActivity) getActivity();
                                Intent myIntent = new Intent(mainActivity, SongListActivity.class);
                                //传递数据
                                Bundle bundle = new Bundle();
                                //todo 新建一个歌单 在数据库中创建一个新的歌单
                                List<MusicInfoModel> musicInfoModelList = new ArrayList<>();
                                SongList songList = new SongList(title,musicInfoModelList);
                                bundle.putSerializable("SongList",(Serializable) songList);//把这个歌曲对象传过去
//                                bundle.putSerializable("songListName",songList.getSongListName());
//                                bundle.putSerializable("cover",R.drawable.album);//默认封面
//                                bundle.putSerializable("musicList", (Serializable) musicInfoModelList);


                                songListAdapter.notifyItemChanged(0);

                                myIntent.putExtras(bundle);
                                //调用util的方法
                                MusicUtil.addSongList(songList);
                                //启动新的intent
                                startActivity(myIntent);
                            }
                        })
                        .setNegativeButton("取消",  new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                System.out.println("点击取消按钮");
                            }
                        })
                        .show();
            }
        });
        //todo 导入歌单
        importImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("我要导入歌单","打开导入歌单的界面");
            }
        });

        //获得recyclerList和侧边栏
        recyclerView = rootView.findViewById(R.id.my_song_list);
        //避免打开置顶
        recyclerView.setFocusable(false);

        //管理视图渲染，线性的
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //初始化Adapter
        initAdapter();
        return rootView;
    }

    private void initAdapter() {
        //todo 拿到这个歌单列表 需要更改为从数据库拿到我创建的所有的歌单的列表
        list = new ArrayList<>();
        list = MusicUtil.getAllSongList();
        if(list.size() >0){
            Log.e("拿到歌单列表",list.size()+"个歌单");
        }
        songListAdapter = new SongListAdapter(getContext(), list);
        recyclerView.setAdapter(songListAdapter);
    }

}