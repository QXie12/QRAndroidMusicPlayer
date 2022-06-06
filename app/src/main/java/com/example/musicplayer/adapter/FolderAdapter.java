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
import com.example.musicplayer.bean.Folder;
import com.example.musicplayer.bean.MusicInfoModel;

import java.io.Serializable;
import java.util.List;
//单曲每一个recyclerlistitem的adapter
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.BaseViewHolder> {

    private List<Folder> mDatas;
    private Context mContext;


    public FolderAdapter(Context context, List<Folder> data) {
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
        Folder folder = mDatas.get(position);
        NormalHolder realHolder = (NormalHolder) holder;
        //专辑名
        realHolder.folderName.setText(folder.getFolderName());
        //专辑详情
        realHolder.folderPath.setText(folder.getMusicList().size()+"首 "+ folder.getPath());
        //专辑封面
//        realHolder.cover.setImageResource(R.drawable.ic_album);
//        if(folder.getCover() == null){//读不到专辑图片
        Glide.with(mContext).load(R.drawable.album).into(realHolder.cover);


        realHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("本文件夹的信息", folder.getFolderName()+" "+ folder.getPath()+ " " + folder.getMusicList().size());
                //创建intent，打开二级页面
                Intent myIntent = new Intent(mContext,SecondMusicActivity.class);
                //传递数据
                Bundle bundle = new Bundle();
                bundle.putSerializable("name",folder.getFolderName());
                bundle.putSerializable("musicList",(Serializable)folder.getMusicList());
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
        private final TextView folderName;
        private final TextView folderPath;
        private final ImageView cover;


        public NormalHolder(View itemView) {
            super(itemView);
            folderName =  itemView.findViewById(R.id.folder_list_item);
            folderPath = itemView.findViewById(R.id.folder_content);
            cover =  itemView.findViewById(R.id.folder_avatar);


        }
    }

}