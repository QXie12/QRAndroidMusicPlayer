package com.example.musicplayer.bean;

import java.util.List;
//按专辑分分类，包括专辑名，唱专辑的歌手，这个专辑里面收录的歌曲
public class Album {


    private String albumName;
    private String sortAlbumId;
    private String sortAlbumName;

    private List<MusicInfoModel> musicList;

    public Album(String albumName, List<MusicInfoModel> musicList) {
        this.albumName = albumName;
        this.musicList = musicList;
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
}
