package com.example.musicplayer.bean;

import android.graphics.Bitmap;

public class MusicInfoModel {

    private String musicName;//用于显示的歌曲的名字
    private String sortId;//用于排序的id 在这里是歌曲拼音的首字母
    private String sortName;//用于排序的全拼音 这个是用于后面的排序以及搜索

    private String singer;//歌手
    private String album;//专辑
    private int time;//歌曲时长

    private long size;//歌曲所占空间大小

    private Bitmap bitmap;//专辑图片

    private String path;//歌曲地址
    private int image;//歌曲封面图片






    public MusicInfoModel(String musicName) {
        this.musicName = musicName;
    }

    public MusicInfoModel(String musicName, String singer, String album, int time, int image) {
        this.musicName = musicName;
        this.singer = singer;
        this.album = album;
        this.time = time;
        this.image = image;
    }


    //创建排名的时候所需要的构造函数：音乐名、排序的第一个字母、排序名、歌手名、专辑名、持续时间、专辑、图片
    public MusicInfoModel(String musicName, String sortId, String sortName, String singer, String album, int time, int image) {
        this.musicName = musicName;
        this.sortId = sortId;
        this.sortName = sortName;
        this.singer = singer;
        this.album = album;
        this.time = time;
        this.image = image;
    }


    public MusicInfoModel(String musicName, String sortId, String sortName) {
        this.musicName = musicName;
        this.sortId = sortId;
        this.sortName = sortName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }




    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
