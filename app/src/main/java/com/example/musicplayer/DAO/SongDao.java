package com.example.musicplayer.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.musicplayer.Entity.Song;

import java.util.List;

@Dao
public interface SongDao {

    @Query("SELECT * FROM song")
    List<Song> getAllSongs();//获得所有的歌曲

    @Insert
    void insertAll(Song... songs);//批量插入歌曲
    @Insert
    void insertSong(Song song);//插入一个歌曲

    @Delete
    void delete(Song song);//删除歌曲

}
