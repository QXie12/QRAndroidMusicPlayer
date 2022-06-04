package com.example.musicplayer.bean;

import java.io.Serializable;
import java.util.List;

public class Folder implements Serializable {
    //文件夹名字
    private String folderName;
    //文件夹路径
    private String path;
    //排序专用
    private String sortFolderId;
    private String sortFolderName;
    //文件夹下的歌
    private List<MusicInfoModel> musicList;

    public Folder(String folderName, String path, List<MusicInfoModel> musicList) {
        this.folderName = folderName;
        this.path = path;
        this.musicList = musicList;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSortFolderId() {
        return sortFolderId;
    }

    public void setSortFolderId(String sortFolderId) {
        this.sortFolderId = sortFolderId;
    }

    public String getSortFolderName() {
        return sortFolderName;
    }

    public void setSortFolderName(String sortFolderName) {
        this.sortFolderName = sortFolderName;
    }

    public List<MusicInfoModel> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicInfoModel> musicList) {
        this.musicList = musicList;
    }
}
