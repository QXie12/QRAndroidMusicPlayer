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

import com.example.musicplayer.bean.MusicInfoModel;

import java.util.ArrayList;
import java.util.List;
//读取本地音乐
public class MusicUtil {
    private static List<MusicInfoModel> musicList=new ArrayList<>();
    private Context context;


    ContentResolver contentResolver;
    //Uri，指向external的database
    private Uri contentUri = Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。
    private String[] projection = {
            Media._ID,//        歌曲ID：MediaStore.Audio.Media._ID
            Media.TITLE,//        歌曲的名称 ：MediaStore.Audio.Media.TITLE
            Media.ALBUM,//        歌曲的专辑名：MediaStore.Audio.Media.ALBUM
            Media.ARTIST,//        歌曲的歌手名： MediaStore.Audio.Media.ARTIST
            Media.DATA,//        歌曲文件的全路径 ：MediaStore.Audio.Media.DATA
            Media.DISPLAY_NAME,//        歌曲文件的名称：MediaStore.Audio.Media.DISPLAY_NAME
            Media.YEAR,//        歌曲文件的发行日期：MediaStore.Audio.Media.YEAR
            Media.DURATION,//        歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
            Media.SIZE,//        歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
            Media.ALBUM_ID//        专辑ID
    };

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
        Cursor cursor = contentResolver.query(contentUri,null,Media.IS_MUSIC,null,Media.DEFAULT_SORT_ORDER);
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
            long id=cursor.getLong(idCol);//歌曲内部id
            String title=cursor.getString(titleCol);//歌曲名称
            String album=cursor.getString(albumCol);//歌曲专辑名
            String singer=cursor.getString(artistCol);//歌手名
            String data=cursor.getString(dataCol);//文件全路径
            String displayName=cursor.getString(displayNameCol);//文件全名称
            String year=cursor.getString(yearCol);//发行日期
            int duration=cursor.getInt(durationCol);//总播放时长
            long size=cursor.getLong(sizeCol);//文件大小
            int albumId = cursor.getInt(albumIdCol);
            Log.e("展示的名字",title+" "+ album+ " " +displayName);
            Log.e("读取到的歌曲","歌曲id"+id+"歌曲名称"+title+"歌曲专辑"+album+"歌曲路径" +data+"歌手名"+singer+"发行日期"+year+"总播放时长"+duration+"文件大小"+size);


            MusicInfoModel musicInfoModel = new MusicInfoModel(title,singer,album,duration,R.drawable.ic_gai);
            if (title.contains("-")) {
                String[] str = title.split("-");
                musicInfoModel.setSinger(str[0].trim());
                musicInfoModel.setMusicName(str[1].trim());
            }
            musicInfoModel.setBitmap(getAlbumArt(albumId));
            musicList.add(musicInfoModel);
        }while (cursor.moveToNext());
        cursor.close();
    }

    private Bitmap getAlbumArt(int album_id) {
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
        return musicList;
    }

    public void setMusicList(List<MusicInfoModel> musicList) {
        this.musicList = musicList;
    }

}
