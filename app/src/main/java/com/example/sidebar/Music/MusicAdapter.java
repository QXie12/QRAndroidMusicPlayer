package com.example.sidebar.Music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

//获取本机音乐的适配器
public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<Music> music_List; //本机音乐列表
    private LayoutInflater inflater;

    public MusicAdapter(Context context, List<Music> mlist) {
        this.context = context;
        this.music_List = mlist;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount(){
        return music_List.size();
    }

    @Override
    public Object getItem(int i) {
        return music_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //不知道干啥的
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return view;
    }
}
