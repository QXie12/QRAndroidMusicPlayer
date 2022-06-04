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
import com.example.musicplayer.R;
import com.example.musicplayer.SecondMusicActivity;
import com.example.musicplayer.bean.Album;
import com.example.musicplayer.bean.MusicInfoModel;

import java.io.Serializable;
import java.util.List;

//单曲每一个recyclerlistitem的adapter
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.BaseViewHolder> {

//    private List<MusicInfoModel> mDatas;
    private List<Album> mDatas;
    private Context mContext;


    public AlbumAdapter(Context context, List<Album> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;
        //根据viewtype来创建条目
        view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new NormalHolder(view);

    }
    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        Album album = mDatas.get(position);
        NormalHolder realHolder = (NormalHolder) holder;
        //专辑名
        realHolder.albumName.setText(album.getAlbumName());
        //专辑详情
        realHolder.albumDetail.setText(album.getMusicList().size()+" 首 "+ album.getSingerName());
        //专辑封面
//        realHolder.cover.setImageResource(R.drawable.ic_album);
        if(album.getCover() == null){//读不到专辑图片
            Glide.with(mContext).load(R.drawable.album).into(realHolder.cover);

//            realHolder.cover.setImageResource(R.drawable.ic_album);
        }else{
            Glide.with(mContext).load(album.getCover()).into(realHolder.cover);

//            realHolder.cover.setImageBitmap(album.getCover());
        }

        realHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("本专辑的信息", album.getAlbumName()+" "+ album.getSingerName()+ " " + album.getMusicList().size());
//                创建intent
                Intent myIntent = new Intent(mContext,SecondMusicActivity.class);
                //传递数据
                Bundle bundle = new Bundle();
                bundle.putSerializable("name",album.getAlbumName());
                bundle.putSerializable("musicList",(Serializable)album.getMusicList());
                myIntent.putExtras(bundle);
                //启动新的intent
                mContext.startActivity(myIntent);

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
        private final TextView albumName;
        private final TextView albumDetail;
        private final ImageView cover;


        public NormalHolder(View itemView) {
            super(itemView);
            albumName =  itemView.findViewById(R.id.album_list_item);
            albumDetail = itemView.findViewById(R.id.album_content);
            cover =  itemView.findViewById(R.id.album_avatar);


        }
    }

}