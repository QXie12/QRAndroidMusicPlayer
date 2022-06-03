package com.example.musicplayer.bean;

import android.graphics.Bitmap;

import java.util.List;
//按专辑分分类，包括专辑名，唱专辑的歌手，这个专辑里面收录的歌曲
public class Album {

    //专辑名字
    private String albumName;
    //歌手名字
    private String singerName;
    //专辑封面
    private Bitmap cover;
    //排序专用
    private String sortAlbumId;
    private String sortAlbumName;
    //歌曲列表
    private List<MusicInfoModel> musicList;

    public Album(String albumName,String singerName, Bitmap cover, List<MusicInfoModel> musicList) {
        this.albumName = albumName;
        this.singerName = singerName;
        this.musicList = musicList;
        this.cover = cover;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSortAlbumId() {
        return sortAlbumId;
    }

    public void setSortAlbumId(String sortAlbumId) {
        this.sortAlbumId = sortAlbumId;
    }

    public String getSortAlbumName() {
        return sortAlbumName;
    }

    public void setSortAlbumName(String sortAlbumName) {
        this.sortAlbumName = sortAlbumName;
    }

    public List<MusicInfoModel> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicInfoModel> musicList) {
        this.musicList = musicList;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
}
