package com.example.musicplayer.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.musicplayer.Entity.User;
import com.example.musicplayer.Entity.UserWithSongLists;

import java.util.List;

//实体类对应的DAO操作
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAllUsers();//获得所有的用户

//    @Query("SELECT * FROM user WHERE id IN (:userIds)")
//    List<User> loadAllByIds(int[] userIds);//根据用户id来获得一些用户

    @Query("SELECT * FROM user WHERE user_name =  :user_name AND " +
            "user_password =:user_password ")
    User findByNameAndPassword(String user_name, String user_password);

    @Insert
    void insertAll(User... users);//批量插入用户
    @Insert
    void insertUser(User user);//插入一个用户


    @Delete
    void delete(User user);//删除用户

    @Transaction
    @Query("SELECT * FROM user")//获得用户的歌单
    public List<UserWithSongLists> getUserWithSongLists();


}