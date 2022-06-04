package com.example.musicplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.common.MusicUtil;

import java.util.List;
//单曲每一个recyclerlistitem的adapter
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.BaseViewHolder> {

    private List<MusicInfoModel> mDatas;
    private Context mContext;


    public MusicAdapter(Context context, List<MusicInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;
        //根据viewtype来创建条目
        view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new NormalHolder(view);

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        MusicInfoModel musicInfoModel = mDatas.get(position);
        NormalHolder realHolder = (NormalHolder) holder;
        realHolder.songName.setText(musicInfoModel.getMusicName());
        realHolder.singer.setText(musicInfoModel.getSinger());
        if(musicInfoModel.getBitmap() == null){//读不到专辑图片,有可能是因为bitmap不能序列化的原因
//            realHolder.cover.setImageResource(R.drawable.ic_gai);
//            if(MusicUtil.getAlbumArt(musicInfoModel.getImage()) != null){//如果通过id去拿有图
//                musicInfoModel.setBitmap(MusicUtil.getAlbumArt(musicInfoModel.getImage()));
//                Log.e("我通过imageid去拿有图", musicInfoModel.getMusicName()+ " " + musicInfoModel.getBitmap());
//                Glide.with(mContext).load(musicInfoModel.getBitmap()).into(realHolder.cover);
//            }else{
//                Glide.with(mContext).load(R.drawable.gai).into(realHolder.cover);
//            }
            if(MusicUtil.getAlbumArtByPath(musicInfoModel.getPath()) != null){//如果通过id去拿有图
                musicInfoModel.setBitmap(MusicUtil.getAlbumArtByPath(musicInfoModel.getPath()));
                Log.e("我通过path去拿有图", musicInfoModel.getMusicName()+ " " + musicInfoModel.getBitmap());
                Glide.with(mContext).load(musicInfoModel.getBitmap()).into(realHolder.cover);
            }else{
                Glide.with(mContext).load(R.drawable.gai).into(realHolder.cover);
            }


        }else{
//            realHolder.cover.setImageBitmap(musicInfoModel.getBitmap());
            Glide.with(mContext).load(musicInfoModel.getBitmap()).into(realHolder.cover);
        }

        Log.e("歌名",musicInfoModel.getMusicName());
        Log.e("歌手",musicInfoModel.getSinger());
        Log.e("图片",musicInfoModel.getImage()+"");


        // todo 张 点击单曲列表的歌曲跳转 可以通过musicInfoModel获得本首歌曲的相关信息
        realHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("本首歌的信息",musicInfoModel.getMusicName()+" "+ musicInfoModel.getSinger() + " " + musicInfoModel.getSortSongId());
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

    private class NormalHolder extends BaseViewHolder {
        private final TextView songName;
        private final TextView singer;
        private final ImageView cover;


        public NormalHolder(View itemView) {
            super(itemView);
            songName =  itemView.findViewById(R.id.list_item);
            singer = itemView.findViewById(R.id.item_content);
            cover =  itemView.findViewById(R.id.avatar);


        }
    }

}