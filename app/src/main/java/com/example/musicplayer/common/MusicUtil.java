package com.example.musicplayer.common;
import com.example.musicplayer.R;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.example.musicplayer.bean.Album;
import com.example.musicplayer.bean.Folder;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.bean.Singer;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

//读取本地音乐
public class MusicUtil {
    //所有歌曲列表
    private static List<MusicInfoModel> allMusicList = new ArrayList<>();
    //所有分类歌手歌曲列表
    private static List<Singer> allSingerList = new ArrayList<>();
    //所有专辑分类的歌曲列表
    private static List<Album> allAlbumList = new ArrayList<>();
    //所有专辑分类的歌曲列表
    private static List<Folder> allFolderList = new ArrayList<>();


    //所有歌手列表
    private static HashMap<String,Integer> singerMap = new HashMap<>();
    //所有专辑列表
    private static HashMap<String,Integer> albumMap = new HashMap<>();
    //所有文件夹列表
    private static HashMap<String,Integer> folderMap = new HashMap<>();

    private Context context;


    private static ContentResolver contentResolver;
    //Uri，指向external的database
    private Uri contentUri = Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。
    private String[] projection = {
            Media._ID,           //        歌曲ID：MediaStore.Audio.Media._ID
            Media.TITLE,         //        歌曲的名称 ：MediaStore.Audio.Media.TITLE
            Media.ALBUM,         //        歌曲的专辑名：MediaStore.Audio.Media.ALBUM
            Media.ARTIST,        //        歌曲的歌手名： MediaStore.Audio.Media.ARTIST
            Media.DATA,          //        歌曲文件的全路径 ：MediaStore.Audio.Media.DATA
            Media.DISPLAY_NAME,  //        歌曲文件的名称：MediaStore.Audio.Media.DISPLAY_NAME
            Media.YEAR,          //        歌曲文件的发行日期：MediaStore.Audio.Media.YEAR
            Media.DURATION,      //        歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
            Media.SIZE,          //        歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
            Media.ALBUM_ID       //        专辑ID
    };

    //使用构造函数去获取本地所有的歌曲
    public MusicUtil(Context context){
        this.context = context;
        //创建ContentResolve对象
        contentResolver = context.getContentResolver();
        //创建游标 参数意义
        // uri 查询的数据库名称加上表的名称；
        // projection 指定查询数据库表中的哪几列
        // selection 指定查询条件
        // selectionArgs 参数selection里有？符号时，用这个实际值代替问号，如果没有的话，这个string数组可以为null
        //sortorder 指定查询结果的排列顺序
        Cursor cursor = contentResolver.query(contentUri,null,Media.IS_MUSIC,null,Media.ARTIST);
        //游标归零
        cursor.moveToFirst();
        //相应的列代表歌曲的相应信息
        int idCol = cursor.getColumnIndex(Media._ID);
        int titleCol = cursor.getColumnIndex(Media.TITLE);
        int albumCol = cursor.getColumnIndex(Media.ALBUM);
        int artistCol = cursor.getColumnIndex(Media.ARTIST);
        int dataCol = cursor.getColumnIndex(Media.DATA);
        int displayNameCol = cursor.getColumnIndex(Media.DISPLAY_NAME);
        int yearCol = cursor.getColumnIndex(Media.YEAR);
        int durationCol = cursor.getColumnIndex(Media.DURATION);
        int sizeCol = cursor.getColumnIndex(Media.SIZE);
        int albumIdCol = cursor.getColumnIndex(Media.ALBUM_ID);


        do{
            int id=cursor.getInt(idCol);//歌曲内部id
            String title=cursor.getString(titleCol);//歌曲名称
            String album=cursor.getString(albumCol);//歌曲专辑名
            String singer=cursor.getString(artistCol);//歌手名
            String data=cursor.getString(dataCol);//文件全路径
            String displayName=cursor.getString(displayNameCol);//文件全名称
            String year=cursor.getString(yearCol);//发行日期
            int duration=cursor.getInt(durationCol);//总播放时长
            long size=cursor.getLong(sizeCol);//文件大小
            int albumId = cursor.getInt(albumIdCol);
            Log.e("1、展示的名字",title+" "+ album+ " " +displayName +" " + albumId);
            Log.e("2、读取到的歌曲","歌曲id:"+id+"歌曲名称:"+title+"歌曲专辑:"+album+"歌曲路径:" +data+"歌手名:"+singer+"发行日期:"+year+"总播放时长:"+duration+"文件大小:"+size);


            MusicInfoModel musicInfoModel = new MusicInfoModel(title,singer,album,duration,albumId);
            //处理有时候歌曲名异常显示的情况，先分割出来歌曲名和歌手名
            if (title.contains("-")) {
                String[] str = title.split("-");
                musicInfoModel.setSinger(str[0].trim());
                musicInfoModel.setMusicName(str[1].trim());
                Log.e("3、这首歌的歌名是"," "+musicInfoModel.getMusicName());
                Log.e("4、这首歌的歌手是"," "+musicInfoModel.getSinger());
            }
            //设置专辑封面
            musicInfoModel.setBitmap(getAlbumArt(albumId));
            Log.e("5、这首歌的歌封面是"," "+musicInfoModel.getBitmap());

            //1、将歌曲添加到所有歌曲列表里
            allMusicList.add(musicInfoModel);

//            //2、将歌曲按歌手分分类添加到歌手列表里
//            if(singerMap.get(musicInfoModel.getSinger()) != null){//前面已经加过这个歌手的歌
//                Singer thisSinger = allSingerList.get(singerMap.get(musicInfoModel.getSinger()));
//                thisSinger.getMusicList().add(musicInfoModel);
//            }else{
//                List<MusicInfoModel> thisSingerMusicList = new ArrayList<>();
//                thisSingerMusicList.add(musicInfoModel);
//                String thisSingerName = musicInfoModel.getSinger();
//                allSingerList.add(new Singer(thisSingerName, thisSingerMusicList));
//                singerMap.put(thisSingerName,singerMap.size());
//            }
//            //将歌曲按专辑分类添加到专辑列表里
//            if(albumMap.get(musicInfoModel.getAlbum()) != null){//前面已经加过这个专辑的歌
//                Album thisAlbum = allAlbumList.get(albumMap.get(musicInfoModel.getAlbum()));
//                thisAlbum.getMusicList().add(musicInfoModel);
//            }else{
//                List<MusicInfoModel> thisAlbumMusicList = new ArrayList<>();
//                thisAlbumMusicList.add(musicInfoModel);
//                String thisAlbumName = musicInfoModel.getAlbum();
//                allAlbumList.add(new Album(thisAlbumName, musicInfoModel.getSinger(), musicInfoModel.getBitmap(),thisAlbumMusicList));
//                albumMap.put(thisAlbumName,albumMap.size());
//            }

            //将歌曲添加到文件夹列表
            String prePath = data.substring(0,data.lastIndexOf("/"));
            Log.e("文件夹地址", prePath);
            if(folderMap.get(prePath) != null){//前面已经加过这个专辑的歌
                Folder thisFolder = allFolderList.get(folderMap.get(prePath));
                thisFolder.getMusicList().add(musicInfoModel);
            }else{
                List<MusicInfoModel> thisFolderMusicList = new ArrayList<>();
                thisFolderMusicList.add(musicInfoModel);
                allFolderList.add(new Folder(prePath.substring(prePath.lastIndexOf("/")+1), prePath, thisFolderMusicList));
                folderMap.put(prePath,folderMap.size());
            }


        }while (cursor.moveToNext());
        cursor.close();

//        //打印按歌手分类之后的歌曲排序
//        for(Singer singer : allSingerList){
//            //打印歌手分类下的歌单
//            Log.e("歌手:",singer.getSingerName());
//            for(MusicInfoModel musicInfoModel : singer.getMusicList()){
//
//                Log.e("歌手分类下的歌单", " " + musicInfoModel.getMusicName());
//            }
//        }
//        //打印按专辑分类之后的歌曲排序
//        for(Album album : allAlbumList){
//            //打印专辑分类下的歌单
//            Log.e("专辑:",album.getAlbumName());
//            for(MusicInfoModel musicInfoModel : album.getMusicList()){
//                Log.e("专辑分类下的歌单", " " + musicInfoModel.getMusicName());
//            }
//        }
        //打印按文件夹分类之后的歌曲排序
        for(Folder folder : allFolderList){
            //打印专辑分类下的歌单
            Log.e("文件夹:",folder.getFolderName());
            for(MusicInfoModel musicInfoModel : folder.getMusicList()){
                Log.e("文件夹分类下的歌单", " " + musicInfoModel.getMusicName());
            }
        }

    }

    public static Bitmap getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = contentResolver.query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        }
        return bm;
    }

    public static List<MusicInfoModel> getMusicList() {
        return allMusicList;
    }

    public static List<Singer> getAllSingerList() {
        return allSingerList;
    }


    public static List<Album> getAllAlbumList() {
        return allAlbumList;
    }

    //判断一首歌是否字母开头
    public static boolean checkFirstIsEnglish(String string){
        char c = string.charAt(0);
        if((c>='a'&&c<='z')   ||   (c>='A'&&c<='Z')) {
            return   true;
        }else{
            return   false;
        }
    }


    public static List<Singer> getSingers(){
        //首次获取，此时静态列表还未创建完毕
        if(allSingerList.size() == 0){
            for(MusicInfoModel musicInfoModel : allMusicList){
                if(singerMap.get(musicInfoModel.getSinger()) != null){//前面已经加过这个歌手的歌
                    Singer thisSinger = allSingerList.get(singerMap.get(musicInfoModel.getSinger()));
                    thisSinger.getMusicList().add(musicInfoModel);
                    Log.e("此时歌曲有封面吗？",musicInfoModel.getBitmap()+" ");

                }else{
                    List<MusicInfoModel> thisSingerMusicList = new ArrayList<>();
                    thisSingerMusicList.add(musicInfoModel);
                    String thisSingerName = musicInfoModel.getSinger();
                    Log.e("此时歌曲有封面吗？",musicInfoModel.getBitmap()+" ");
                    allSingerList.add(new Singer(thisSingerName, thisSingerMusicList));
                    singerMap.put(thisSingerName,singerMap.size());
                }
            }
        }
            return allSingerList;
    }

    public static List<Album> getAlbums(){
        //首次获取，此时静态列表还未创建完毕
        if(allAlbumList.size() == 0){
            for(MusicInfoModel musicInfoModel : allMusicList){
                if(albumMap.get(musicInfoModel.getAlbum()) != null){//前面已经加过这个专辑的歌
                    Album thisAlbum = allAlbumList.get(albumMap.get(musicInfoModel.getAlbum()));
                    thisAlbum.getMusicList().add(musicInfoModel);
                }else{
                    List<MusicInfoModel> thisAlbumMusicList = new ArrayList<>();
                    thisAlbumMusicList.add(musicInfoModel);
                    String thisAlbumName = musicInfoModel.getAlbum();
                    allAlbumList.add(new Album(thisAlbumName, musicInfoModel.getSinger(), musicInfoModel.getBitmap(),thisAlbumMusicList));
                    albumMap.put(thisAlbumName,albumMap.size());
                }
            }
        }
        return allAlbumList;
    }

    public static List<Folder> getAllFolderList() {
        return allFolderList;
    }
}
