package com.example.musicplayer.bean;

import java.io.Serializable;
import java.util.List;

//按歌手分类，歌手的名字、这个歌手的相关歌曲列表
public class Singer implements Serializable {
    //歌手名字
    private String singerName;
    //排序专用
    private String sortSingerId;
    private String sortSingerName;
    //歌曲列表
    private List<MusicInfoModel> musicList;

    public Singer(String singerName, List<MusicInfoModel> musicList) {
        this.singerName = singerName;
        this.musicList = musicList;
    }

    public String getSortSingerName() {
        return sortSingerName;
    }

    public void setSortSingerName(String sortSingerName) {
        this.sortSingerName = sortSingerName;
    }

    public Singer(){

    }
    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public List<MusicInfoModel> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicInfoModel> musicList) {
        this.musicList = musicList;
    }

    public String getSortSingerId() {
        return sortSingerId;
    }

    public void setSortSingerId(String sortSingerId) {
        this.sortSingerId = sortSingerId;
    }
}
