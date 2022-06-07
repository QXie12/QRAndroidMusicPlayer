package com.example.musicplayer.Entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

//嵌套对象 用户以及用户的歌单列表 用户与歌单是1对多的关系，一个用户有多个歌单
public class UserWithSongLists {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "user_name",
            entityColumn = "user_creator_name"
    )
    public List<SongList> songLists;

    public UserWithSongLists(User user, List<SongList> songLists) {
        this.user = user;
        this.songLists = songLists;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SongList> getSongLists() {
        return songLists;
    }

    public void setSongLists(List<SongList> songLists) {
        this.songLists = songLists;
    }
}
