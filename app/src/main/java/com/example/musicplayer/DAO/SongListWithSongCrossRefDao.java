package com.example.musicplayer.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.musicplayer.Entity.SongListWithSongCrossRef;

import java.util.List;

@Dao
public interface SongListWithSongCrossRefDao {


    @Query("SELECT * FROM SongListWithSongCrossRef")
    List<SongListWithSongCrossRef> getAllSongListWithSongCrossRef();//获得所有的有歌曲的歌单

    @Query("SELECT song_path FROM SongListWithSongCrossRef WHERE song_list_id = :songListName")
    List<String> getSongsBySongList(String songListName);//通过歌单名字拿到所有的歌

    @Insert
    @Transaction
    void insertAll(SongListWithSongCrossRef... SongListWithSongCrossRef);

    @Insert
    @Transaction
    void insertSongListWithSongCrossRef(SongListWithSongCrossRef songListWithSongCrossRef);

    @Transaction
    @Delete
    void delete(SongListWithSongCrossRef songListWithSongCrossRef);//删除歌单中的歌曲
}
