package com.example.musicplayer.adapter;

import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.example.musicplayer.R;
        import com.example.musicplayer.bean.MusicInfoModel;

        import java.util.List;
//单曲每一个recyclerlistitem的adapter
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.BaseViewHolder> {

    private List<MusicInfoModel> mDatas;
    private Context mContext;


    public FolderAdapter(Context context, List<MusicInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;
        //根据viewtype来创建条目
        view = LayoutInflater.from(mContext).inflate(R.layout.folder_item, parent, false);
        return new NormalHolder(view);

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        MusicInfoModel musicInfoModel = mDatas.get(position);
        NormalHolder realHolder = (NormalHolder) holder;
        realHolder.songName.setText(musicInfoModel.getMusicName());
        realHolder.singer.setText(musicInfoModel.getSinger());
        if(musicInfoModel.getBitmap() == null){//读不到专辑图片
            realHolder.cover.setImageResource(R.drawable.ic_gai);
        }else{
            realHolder.cover.setImageBitmap(musicInfoModel.getBitmap());
        }

        Log.e("歌名",musicInfoModel.getMusicName());
        Log.e("歌手",musicInfoModel.getSinger());
        Log.e("图片",musicInfoModel.getImage()+"");

        realHolder.songName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            songName =  itemView.findViewById(R.id.folder_list_item);
            singer = itemView.findViewById(R.id.folder_content);
            cover =  itemView.findViewById(R.id.folder_avatar);


        }
    }

}