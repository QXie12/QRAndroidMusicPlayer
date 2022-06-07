package com.example.musicplayer.adapter;

import android.content.Context;
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
import com.example.musicplayer.MusicListActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.common.MusicUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

//添加歌单歌曲的item的adapter
public class SongMusicAdapter extends RecyclerView.Adapter<SongMusicAdapter.BaseViewHolder> {

    private List<MusicInfoModel> mDatas;
    private Context mContext;

    public SongMusicAdapter(Context context, List<MusicInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public SongMusicAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;
        //根据viewtype来创建条目
        view = LayoutInflater.from(mContext).inflate(R.layout.add_music_item, parent, false);

        return new SongMusicAdapter.NormalHolder(view);

    }

    @Override
    public void onBindViewHolder(SongMusicAdapter.BaseViewHolder holder, final int position) {
        MusicInfoModel musicInfoModel = mDatas.get(position);
        SongMusicAdapter.NormalHolder realHolder = (SongMusicAdapter.NormalHolder) holder;

        realHolder.songName.setText(musicInfoModel.getMusicName());
        realHolder.singer.setText(musicInfoModel.getSinger());
        if(musicInfoModel.getBitmap() == null){//读不到专辑图片,有可能是因为bitmap不能序列化的原因
            if(MusicUtil.getAlbumArtByPath(musicInfoModel.getPath()) != null){//如果通过id去拿有图
                musicInfoModel.setBitmap(MusicUtil.getAlbumArtByPath(musicInfoModel.getPath()));
                Log.e("我通过path去拿有图", musicInfoModel.getMusicName()+ " " + musicInfoModel.getBitmap());
                Glide.with(mContext).load(musicInfoModel.getBitmap()).into(realHolder.cover);
            }else{
                Glide.with(mContext).load(R.drawable.gai).into(realHolder.cover);
            }

        }else{
            Glide.with(mContext).load(musicInfoModel.getBitmap()).into(realHolder.cover);
        }

        //添加歌曲到歌单的按钮
        realHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("要把这首歌曲添加到歌单里面",musicInfoModel.getMusicName()+" "+ musicInfoModel.getSinger() + " " + musicInfoModel.getSortSongId()+musicInfoModel.getId());
                //todo 要把这一首歌曲添加到这个歌单里面
//                MusicListActivity.addMusicToThisSongList(musicInfoModel);
//                MusicListActivity.getSongList().getMusicList().add(musicInfoModel);
                Log.e("添加完毕",MusicListActivity.getSongList().getMusicList()+" "+MusicListActivity.getSongList().getMusicList().size()+MusicListActivity.getSongList().getSongListName());

//                MusicListActivity.addMusicToThisSongList(musicInfoModel)
                if(MusicListActivity.addMusicToThisSongList(musicInfoModel)){
                    Snackbar.make(v, "已添加到歌单" +MusicListActivity.getSongList().getSongListName(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setBackgroundTint(mContext.getResources().getColor(R.color.green))
                            .show();
                }else{
                    Snackbar.make(v, "歌单已包含此歌曲", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setBackgroundTint(mContext.getResources().getColor(R.color.red))
                            .show();
                }

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

    private class NormalHolder extends SongMusicAdapter.BaseViewHolder {
        private final TextView songName;
        private final TextView singer;
        private final ImageView cover;
        //todo 添加歌曲到歌单的按钮
        private final ImageView add;


        public NormalHolder(View itemView) {
            super(itemView);
            songName =  itemView.findViewById(R.id.music_list_item);
            singer = itemView.findViewById(R.id.music_item_content);
            cover =  itemView.findViewById(R.id.music_avatar);
            add = itemView.findViewById(R.id.add_song_button);
        }


    }
}