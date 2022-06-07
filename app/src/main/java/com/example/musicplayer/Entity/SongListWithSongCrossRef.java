package com.example.musicplayer.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

//实体关系表
//歌曲与歌单是多对多的关系，歌单里可以有多个歌曲，歌曲可以在不同的歌单
@Entity(primaryKeys = {"song_list_id", "song_path"}, tableName = "SongListWithSongCrossRef")
public class SongListWithSongCrossRef {
    @NonNull
    public String song_list_id;
    @NonNull
    public String song_path;//歌曲的路径


    public SongListWithSongCrossRef(@NonNull String song_list_id, @NonNull String song_path) {
        this.song_list_id = song_list_id;
        this.song_path = song_path;
    }
}
