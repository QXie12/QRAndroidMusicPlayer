package com.example.musicplayer.Entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//歌曲实体
@Entity(tableName = "song")
public class Song {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "song_path", typeAffinity = ColumnInfo.TEXT)
    public String song_path;//歌曲的路径

    @ColumnInfo(name = "song_id", typeAffinity = ColumnInfo.INTEGER)
    public int song_id;//歌曲的id

    @ColumnInfo(name = "song_name", typeAffinity = ColumnInfo.TEXT)
    public String song_name;//歌曲的名字


    public Song(String song_path, int song_id, String song_name) {
        this.song_path = song_path;
        this.song_id = song_id;
        this.song_name = song_name;
    }

    public String getSong_path() {
        return song_path;
    }

    public void setSong_path(String song_path) {
        this.song_path = song_path;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }
}
