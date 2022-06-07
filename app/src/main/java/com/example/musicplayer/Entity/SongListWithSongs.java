package com.example.musicplayer.Entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class SongListWithSongs {
    @Embedded
    public SongList songList;
    @Relation(
            parentColumn = "song_list_id",
            entityColumn = "song_path",
            associateBy = @Junction(SongListWithSongCrossRef.class)
    )
    public List<Song> songs;

    public SongListWithSongs(SongList songList, List<Song> songs) {
        this.songList = songList;
        this.songs = songs;
    }

    public SongList getSongList() {
        return songList;
    }

    public void setSongList(SongList songList) {
        this.songList = songList;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
