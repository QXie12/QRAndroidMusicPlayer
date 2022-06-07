package com.example.musicplayer.Entity;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.musicplayer.DAO.SongDao;
import com.example.musicplayer.DAO.SongListDao;
import com.example.musicplayer.DAO.SongListWithSongCrossRefDao;
import com.example.musicplayer.DAO.UserDao;

//创建数据库的类 告诉系统这是Room数据库对象，entities用于指定该数据库有哪些表，
// version用于指定数据库版本号，后续数据库的升级通过版本号来判断
//使用单例模式来完成上数据库的创建
//创建的Dao对象在这里以抽象方法的范式返回
@Database(entities = {User.class,Song.class,SongList.class,SongListWithSongCrossRef.class}, version = 1 ,exportSchema = false)
public abstract class MyDataBase extends RoomDatabase
{
    private static final String DATABASE_NAME = "my_music_db";//todo 一会改成music_db

    private static MyDataBase databaseInstance;
    //加锁、单例
    public static synchronized MyDataBase getInstance(Context context)
    {
        if(databaseInstance == null)
        {//暂时允许在主线程中进行
            databaseInstance = Room
                    .databaseBuilder(context.getApplicationContext(), MyDataBase.class, DATABASE_NAME).allowMainThreadQueries()
                    .build();
        }
        return databaseInstance;
    }

    public abstract UserDao userDao();

    public abstract SongDao songDao();
    public abstract SongListDao songListDao();

    public abstract SongListWithSongCrossRefDao songListWithSongCrossRefDao();


}
