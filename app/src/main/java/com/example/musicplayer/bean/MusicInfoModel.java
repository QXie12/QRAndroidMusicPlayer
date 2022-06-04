package com.example.musicplayer.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

//每一首歌的信息
public class MusicInfoModel  implements Serializable {

    private String musicName;//用于显示的歌曲的名字
    private String singer;//歌手
    private String album;//专辑
    private int time;//歌曲时长
    private long size;//歌曲所占空间大小
    private transient  Bitmap bitmap;//专辑图片
    private String path;//歌曲地址
    private int imageId;//歌曲封面图片


    private String sortSongId;//用于排序的音乐id 在这里是歌曲拼音的首字母
    private String sortSongName;//用于排序的音乐全拼音 这个是用于后面的排序以及搜索

    private String sortSingerId;//用于排序的歌手id 在这里是歌手拼音的首字母
    private String sortSingerName;//用于排序的歌手全拼音 这个是用于后面的排序以及搜索

    private String sortAlbumId;//用于排序的专辑id 在这里是专辑拼音的首字母
    private String sortAlbumName;//用于排序的专辑全拼音 这个是用于后面的排序以及搜索


    public MusicInfoModel(String musicName) {
        this.musicName = musicName;
    }

    public MusicInfoModel(String musicName, String singer, String album, int time, int image) {
        this.musicName = musicName;
        this.singer = singer;
        this.album = album;
        this.time = time;
        this.imageId = image;
    }

    public MusicInfoModel(String musicName, String singer, String album, int time, int image, String path) {
        this.musicName = musicName;
        this.singer = singer;
        this.album = album;
        this.time = time;
        this.imageId = image;
        this.path = path;
    }


//    public MusicInfoModel(String musicName, String sortId, String sortName, String singer, String album, int time, int image) {
//        this.musicName = musicName;
//        this.sortId = sortId;
//        this.sortName = sortName;
//        this.singer = singer;
//        this.album = album;
//        this.time = time;
//        this.image = image;
//    }
//
//
//    public MusicInfoModel(String musicName, String sortId, String sortName) {
//        this.musicName = musicName;
//        this.sortId = sortId;
//        this.sortName = sortName;
//    }


    public String getSortSongId() {
        return sortSongId;
    }

    public void setSortSongId(String sortSongId) {
        this.sortSongId = sortSongId;
    }

    public String getSortSongName() {
        return sortSongName;
    }

    public void setSortSongName(String sortSongName) {
        this.sortSongName = sortSongName;
    }

    public String getSortSingerId() {
        return sortSingerId;
    }

    public void setSortSingerId(String sortSingerId) {
        this.sortSingerId = sortSingerId;
    }

    public String getSortSingerName() {
        return sortSingerName;
    }

    public void setSortSingerName(String sortSingerName) {
        this.sortSingerName = sortSingerName;
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
        return imageId;
    }

    public void setImage(int image) {
        this.imageId = image;
    }


    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
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
