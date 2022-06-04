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
import com.example.musicplayer.bean.Singer;

import java.io.Serializable;
import java.util.List;


//单曲每一个recyclerlistitem的adapter
public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.BaseViewHolder> {

//    private List<MusicInfoModel> mDatas;
    private List<Singer> mDatas;
    private Context mContext;

//    private OnItemClickListener myClickListener;
//    private OnItemLongClickListener myLongClickListener;


    public SingerAdapter(Context context, List<Singer> data) {
        this.mDatas = data;
        this.mContext = context;
    }



    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;
        //根据viewtype来创建条目
        view = LayoutInflater.from(mContext).inflate(R.layout.singer_item, parent, false);
        return new NormalHolder(view);

    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        Singer singer = mDatas.get(position);
        NormalHolder realHolder = (NormalHolder) holder;
        //歌手名字
        realHolder.singerName.setText(singer.getSingerName());
        //歌手的歌曲数量
        realHolder.singerDetail.setText(singer.getMusicList().size()+"首歌曲");
        //设置cover
        Glide.with(mContext).load(R.drawable.singer).into(realHolder.cover);
        realHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("本歌手的信息",singer.getSingerName()+" "+ singer.getMusicList().size() + " " +singer.getMusicList().get(0).getBitmap());
//                创建intent
                Intent myIntent = new Intent(mContext,SecondMusicActivity.class);
                //传递数据
                Bundle bundle = new Bundle();
                bundle.putSerializable("name",singer.getSingerName());
                bundle.putSerializable("musicList",(Serializable)singer.getMusicList());
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
        private final TextView singerName;
        private final TextView singerDetail;
        private final ImageView cover;


        public NormalHolder(View itemView) {
            super(itemView);
            singerName =  itemView.findViewById(R.id.singer_list_item);
            singerDetail = itemView.findViewById(R.id.singer_content);
            cover =  itemView.findViewById(R.id.singer_avatar);
        }
    }

//
//    public void setOnItemClickListener(OnItemClickListener myClickListener) {
//        this.myClickListener = myClickListener;
//    }
//
//    public void setOnLongClickListener(OnItemLongClickListener myLongClickListener) {
//        this.myLongClickListener = myLongClickListener;
//    }
//
//    //定义接口，创建回调函数 添加点击事件
//    public interface OnItemClickListener {//单击
//        public void onClick(View parent, int position);
//    }
//    public interface OnItemLongClickListener {//长按
//        public boolean onLongClick(View parent, int position);
//    }


}