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
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.SecondMusicActivity;
import com.example.musicplayer.SongListActivity;
import com.example.musicplayer.bean.Album;
import com.example.musicplayer.bean.SongList;
import com.example.musicplayer.common.MusicUtil;

import java.io.Serializable;
import java.util.List;

//每一个歌单所在holder的adapter
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.BaseViewHolder> {

    private List<SongList> mDatas;
    private Context mContext;


    public SongListAdapter(Context context, List<SongList> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public SongListAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;
        //根据viewtype来创建条目
        view = LayoutInflater.from(mContext).inflate(R.layout.song_list_item, parent, false);
        return new SongListAdapter.NormalHolder(view);

    }
    @Override
    public void onBindViewHolder(SongListAdapter.BaseViewHolder holder, final int position) {
        SongList songList = mDatas.get(position);
        SongListAdapter.NormalHolder realHolder = (SongListAdapter.NormalHolder) holder;
        //歌单名
        realHolder.songListName.setText(songList.getSongListName());
        //歌单详情
        realHolder.songListNumber.setText(songList.getMusicList().size()+" 首 ");
        //歌单封面
//        realHolder.cover.setImageResource(R.drawable.ic_album);
        if(songList.getCover() == null){//读不到专辑图片
            Glide.with(mContext).load(R.drawable.album).into(realHolder.cover);
        }else{
            Glide.with(mContext).load(songList.getCover()).into(realHolder.cover);
        }

        realHolder.itemView.setOnClickListener(new View.OnClickListener() {
            //todo 跳转到歌单页面
            @Override
            public void onClick(View v) {
                Log.e("本歌单的信息", songList.getSongListName()+" "+ songList.getMusicList().size());
//                创建intent
                Intent myIntent = new Intent(mContext, SongListActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("SongList",(Serializable) songList);
                Log.e("我创建的歌单传过去是这样的",songList.getSongListName()+songList.getMusicList().size());
//                bundle.putSerializable("songListName",songList.getSongListName());
//                //todo 暂时默认没有封面
//                bundle.putSerializable("cover",-1);
//                bundle.putSerializable("musicList", (Serializable) songList.getMusicList());
                myIntent.putExtras(bundle);
                //启动新的intent
                mContext.startActivity(myIntent);


//                //传递数据
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("songListName","我的最爱");
//                bundle.putSerializable("cover",R.drawable.avatar10);
//                bundle.putSerializable("musicList", (Serializable) MusicUtil.getAllFavoriteMusicList());
//                myIntent.putExtras(bundle);
//                //启动新的intent
//                startActivity(myIntent);

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

    private class NormalHolder extends SongListAdapter.BaseViewHolder {
        private final TextView songListName;
        private final TextView songListNumber;
        private final ImageView cover;


        public NormalHolder(View itemView) {
            super(itemView);
            songListName =  itemView.findViewById(R.id.song_list_item);
            songListNumber = itemView.findViewById(R.id.song_list_number);
            cover =  itemView.findViewById(R.id.song_list_avatar);
        }
    }

}