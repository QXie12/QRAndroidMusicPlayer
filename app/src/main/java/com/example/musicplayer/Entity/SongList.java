package com.example.musicplayer.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//歌单实体 歌单的id、创建歌单的用户名、歌单的名称
@Entity(tableName = "songlist")
public class SongList {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "song_list_id", typeAffinity = ColumnInfo.TEXT)
    public String song_list_id;

    @ColumnInfo(name = "user_creator_name", typeAffinity = ColumnInfo.TEXT)
    public String user_creator_name;

    @ColumnInfo(name = "song_list_name", typeAffinity = ColumnInfo.TEXT)
    public String song_list_name;

    public SongList(String song_list_id, String user_creator_name, String song_list_name) {
        this.song_list_id = song_list_id;
        this.user_creator_name = user_creator_name;
        this.song_list_name = song_list_name;
    }

    @Ignore
    public SongList(String user_creator_name, String song_list_name) {
        this.user_creator_name = user_creator_name;
        this.song_list_name = song_list_name;
    }

    public String getSong_list_id() {
        return song_list_id;
    }

    public void setSong_list_id(String song_list_id) {
        this.song_list_id = song_list_id;
    }

    public String getUser_creator_name() {
        return user_creator_name;
    }

    public void setUser_creator_name(String user_creator_name) {
        this.user_creator_name = user_creator_name;
    }

    public String getSong_list_name() {
        return song_list_name;
    }

    public void setSong_list_name(String song_list_name) {
        this.song_list_name = song_list_name;
    }
}
