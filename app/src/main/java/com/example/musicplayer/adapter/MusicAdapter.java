package com.example.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.LocalSongActivity;
import com.example.musicplayer.MusicActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.common.MusicUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
//单曲、二级页面每一个recyclerlistitem的adapter
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.BaseViewHolder> {

    private List<MusicInfoModel> mDatas;
    private Context mContext;
    //指示位置的
    private int mPosition = -1;
    //弹窗
    private PopupWindow popupWindow;
    private View transparent;

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


        // todo 张 点击单曲列表的歌曲跳转 可以通过musicInfoModel获得本首歌曲的相关信息 增加添加到了最近播放列表
        realHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("本首歌的信息",musicInfoModel.getMusicName()+" "+ musicInfoModel.getSinger() + " " + musicInfoModel.getSortSongId()+musicInfoModel.getId());
//                MusicUtil.addRecentMusic(musicInfoModel);

                LocalSongActivity.setCurrent(musicInfoModel.getId());
                LocalSongActivity.playByPath(musicInfoModel.getPath());
                Intent intent = new Intent();
                intent.setClass(mContext, MusicActivity.class);
                mContext.startActivity(intent); //打开
            }
        });
        //小爱心
        realHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("本首歌的信息",musicInfoModel.getMusicName()+" "+ musicInfoModel.getSinger() + " " + musicInfoModel.getSortSongId()+musicInfoModel.getId());
                if(MusicUtil.addFavoriteMusic(musicInfoModel)){
                    Snackbar.make(v, "已添加到喜欢", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setBackgroundTint(mContext.getResources().getColor(R.color.green))
                            .show();
                }else{
                    Snackbar.make(v, "已喜欢", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setBackgroundTint(mContext.getResources().getColor(R.color.red))
                            .show();
                }

            }
        });

        //more
        realHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("歌曲的更多操作",musicInfoModel.getMusicName()+" "+ musicInfoModel.getSinger() + " " + musicInfoModel.getSortSongId()+musicInfoModel.getId());
                showPopupWindow(musicInfoModel);
            }
        });

        //长按事件
        realHolder.itemView.setLongClickable(true);
        realHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPosition = position;
                return false;
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
        //todo 此处仅为测试功能，实际收藏或者最爱按钮是在歌曲详情页面
        private final ImageView favorite;
        //todo more功能
        private final ImageView more;


        public NormalHolder(View itemView) {
            super(itemView);
            songName =  itemView.findViewById(R.id.list_item);
            singer = itemView.findViewById(R.id.item_content);
            cover =  itemView.findViewById(R.id.avatar);
            favorite = itemView.findViewById(R.id.aixin);
            more = itemView.findViewById(R.id.more);
        }


    }

    public int getmPosition() {
        return mPosition;
    }

    private void showPopupWindow(MusicInfoModel musicInfoModel){
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_layout, null);
        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);

//        //设置各个控件的点击响应
        CardView pop_play = contentView.findViewById(R.id.pop_play);
        CardView pop_collect = contentView.findViewById(R.id.pop_collect);
        CardView pop_download = contentView.findViewById(R.id.pop_download);


//        TextView tv1 = (TextView)contentView.findViewById(R.id.pop_computer);
//        TextView tv2 = (TextView)contentView.findViewById(R.id.pop_financial);
//        TextView tv3 = (TextView)contentView.findViewById(R.id.pop_manage);
//        tv1.setOnClickListener(this);
//        tv2.setOnClickListener(this);
//        tv3.setOnClickListener(this);

        pop_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("播放下一首",musicInfoModel.getMusicName()+" "+ musicInfoModel.getSinger() + " " + musicInfoModel.getSortSongId()+musicInfoModel.getId());
                LocalSongActivity.setCurrent(musicInfoModel.getId()); //更新current
                LocalSongActivity.playByPath(musicInfoModel.getPath()); //播放当前歌曲
                Snackbar.make(v, "开始播放", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setBackgroundTint(mContext.getResources().getColor(R.color.green))
                        .show();
            }
        });

        pop_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("收藏到歌单",musicInfoModel.getMusicName()+" "+ musicInfoModel.getSinger() + " " + musicInfoModel.getSortSongId()+musicInfoModel.getId());
            }
        });

        pop_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("添加到我的最爱",musicInfoModel.getMusicName()+" "+ musicInfoModel.getSinger() + " " + musicInfoModel.getSortSongId()+musicInfoModel.getId());
                if(MusicUtil.addFavoriteMusic(musicInfoModel)){
                    Snackbar.make(v, "添加成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setBackgroundTint(mContext.getResources().getColor(R.color.green))
                            .show();
                }else{
                    Snackbar.make(v, "歌曲已存在", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setBackgroundTint(mContext.getResources().getColor(R.color.red))
                            .show();
                }
            }
        });

        //显示PopupWindow
        View rootview = LayoutInflater.from(mContext).inflate(R.layout.activity_localsong, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);


    }
}