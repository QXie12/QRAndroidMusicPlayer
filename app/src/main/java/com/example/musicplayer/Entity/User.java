package com.example.musicplayer.Entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//用户表单 实现序列化，id自增
// Entity注解就像创建普通对象一样但是会被自动识别
// 每个属性相当于数据库表中的一个字段，可以标记主键 ，name是数据库中列名
//用户 用户名和密码

@Entity(tableName = "user")
public class User implements Serializable {
    //todo 一会所有的标识前面加上对应的类_下划线  user_id、user_name、user_password
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_name", typeAffinity = ColumnInfo.TEXT)
    public String user_name;

    @ColumnInfo(name = "user_password", typeAffinity = ColumnInfo.TEXT)
    public String user_password;

    //自己创建对象的时候调用的方法，Room只能识别和使用一个构造器，如果希望多个，使用Ignore
//    @Ignore
    public User(String user_name, String user_password) {
        this.user_name = user_name;
        this.user_password = user_password;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getUserPassword() {
        return user_password;
    }

    public void setUserPassword(String user_password) {
        this.user_password = user_password;
    }
}