package com.example.musicplayer.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.musicplayer.Entity.SongList;
import com.example.musicplayer.Entity.SongListWithSongs;

import java.util.List;

@Dao
public interface SongListDao {

    @Query("SELECT * FROM songlist")
    List<SongList> getAllSongList();//获得所有的歌单

    @Query("SELECT * FROM songlist WHERE song_list_id = :song_list_id")
    SongList getSongListById(String song_list_id);//获得所有的歌单


    @Insert
    void insertAll(SongList... songLists);//批量插入歌单
    @Insert
    void insertSongList(SongList songList);//插入一个歌单

    @Delete
    void delete(SongList songList);//删除歌单


    @Transaction
    @Query("SELECT * FROM songlist")//返回一个歌单 里面有歌单和歌单的歌曲
    List<SongListWithSongs> getSongListWithSongs();

//    @Transaction
//    @Insert
//    void insertSongListWithSongs(SongListWithSongCrossRef songListWithSongCrossRef);




}
