package com.example.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.LocalSongActivity;
import com.example.musicplayer.MusicActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.RecentActivity;
import com.example.musicplayer.SecondMusicActivity;
import com.example.musicplayer.bean.Album;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.common.MusicUtil;

import java.io.Serializable;
import java.util.List;
//todo
//最近播放recyclerview的adapter
public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.BaseViewHolder> {

//    加载的列表是最近播放过的音乐
    private List<MusicInfoModel> mDatas;
    private Context mContext;


    public RecentAdapter(Context context, List<MusicInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public RecentAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;
        //根据viewtype来创建条目
        view = LayoutInflater.from(mContext).inflate(R.layout.recent_item, parent, false);
        return new RecentAdapter.NormalHolder(view);

    }
    @Override
    public void onBindViewHolder(RecentAdapter.BaseViewHolder holder, final int position) {
        MusicInfoModel musicInfoModel = mDatas.get(position);
        RecentAdapter.NormalHolder realHolder = (RecentAdapter.NormalHolder) holder;
        //歌曲名
        realHolder.songName.setText(musicInfoModel.getMusicName());
        //歌手、时长详情
        //todo 时长格式更改

        realHolder.singerName.setText(musicInfoModel.getSinger()+"  "+ MusicUtil.formatTime(musicInfoModel.getTime()));


        realHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 点击打开一个操作栏
                Log.e("更多操作",musicInfoModel.getMusicName());


            }
        });

        realHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 最近播放列表里面的歌点击也会添加到list里面 此处要打开一个新的activity，返回之后再刷新才看到新的list
//                MusicUtil.addRecentMusic(musicInfoModel);
                RecentActivity.setCurrent(musicInfoModel.getId());
                RecentActivity.playByPath(musicInfoModel.getPath());
                Intent intent = new Intent();
                intent.setClass(mContext, MusicActivity.class);
                mContext.startActivity(intent); //打开
            }
        });
    }


    static class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    private class NormalHolder extends RecentAdapter.BaseViewHolder {
        private final TextView songName;
        private final TextView singerName;
        private final ImageView imageView;


        public NormalHolder(View itemView) {
            super(itemView);
            songName =  itemView.findViewById(R.id.song_name);
            singerName = itemView.findViewById(R.id.singer_name);
            imageView = itemView.findViewById(R.id.more_button);


        }
    }


}