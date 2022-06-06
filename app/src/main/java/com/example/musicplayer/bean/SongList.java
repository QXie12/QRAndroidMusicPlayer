package com.example.musicplayer.bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

//歌单实体类
public class SongList implements Serializable {
    //歌单的名字
    private String songListName;
    //歌单里面的歌曲
    private List<MusicInfoModel> musicList;
    //todo 歌单的封面 目前是默认封面 暂时不实现
    private transient Bitmap cover = null;
    //扩展 编辑歌单的信息


    public SongList(String songListName, List<MusicInfoModel> musicList) {
        this.songListName = songListName;
        this.musicList = musicList;
    }

    public String getSongListName() {
        return songListName;
    }

    public void setSongListName(String songListName) {
        this.songListName = songListName;
    }

    public List<MusicInfoModel> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicInfoModel> musicList) {
        this.musicList = musicList;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
}
