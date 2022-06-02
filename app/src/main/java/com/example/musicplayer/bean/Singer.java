package com.example.musicplayer.bean;

import java.util.List;

//按歌手分类，歌手的名字、这个歌手的相关歌曲列表
public class Singer {
    private String singerName;
    private String sortSingerId;
    private String sortSingerName;

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
