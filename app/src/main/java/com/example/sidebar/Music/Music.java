package com.example.sidebar.Music;

//音乐实体类
public class Music {
    private int id; //歌曲id
    private String music_name;
    private String singer; //歌手名
    private String music_time;//时长
    private String path; //歌曲路径
    private String lyric; //歌词
    //get/set方法
    public Music(int id,String music_name,String singer,String music_time,String path){
        this.id = id;
        this.music_name = music_name;
        this.singer = singer;
        this.music_time = music_time;
        this.path = path;
    }
    public int getId(){
        return id;
    }
    public String getMusic_name(){
        return music_name;
    }
    public String getSinger(){
        return singer;
    }
    public String getMusic_time(){
        return music_time;
    }
    public String getPath(){
        return path;
    }
}
