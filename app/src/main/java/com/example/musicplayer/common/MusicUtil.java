package com.example.musicplayer.common;
import com.example.musicplayer.DAO.SongDao;
import com.example.musicplayer.DAO.SongListDao;
import com.example.musicplayer.DAO.SongListWithSongCrossRefDao;
import com.example.musicplayer.DAO.UserDao;
import com.example.musicplayer.Entity.MyDataBase;
import com.example.musicplayer.Entity.Song;
import com.example.musicplayer.Entity.SongListWithSongCrossRef;
import com.example.musicplayer.Entity.User;
import com.example.musicplayer.Entity.UserWithSongLists;
import com.example.musicplayer.R;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.example.musicplayer.bean.Album;
import com.example.musicplayer.bean.Folder;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.bean.Singer;
import com.example.musicplayer.bean.SongList;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

//读取本地音乐
public class MusicUtil {
    private static String username = "xiez";
    private static String password = "xiez";

    //所有歌曲列表
    private static List<MusicInfoModel> allMusicList = new ArrayList<>();
    //所有分类歌手歌曲列表
    private static List<Singer> allSingerList = new ArrayList<>();
    //所有专辑分类的歌曲列表
    private static List<Album> allAlbumList = new ArrayList<>();
    //所有专辑分类的歌曲列表
    private static List<Folder> allFolderList = new ArrayList<>();
    //所有歌单的列表
    private static List<SongList> allSongList = new ArrayList<>();

    //维护最近播放列表
    private static List<MusicInfoModel> allRecentMusicList = new ArrayList<>();
    //最近播放歌曲的hashmap
    private static HashMap<Integer,String> recentSongMap = new HashMap<>();

    //维护我的最爱歌单
    private static List<MusicInfoModel> allFavoriteMusicList = new ArrayList<>();
    private static SongList myFavoriteSongList = new SongList("我的最爱",allFavoriteMusicList);
    //我的最爱播放歌曲的hashmap
    private static HashMap<Integer,String> favoriteSongMap = new HashMap<>();

    //所有歌手列表
    private static HashMap<String,Integer> singerMap = new HashMap<>();
    //所有专辑列表
    private static HashMap<String,Integer> albumMap = new HashMap<>();
    //所有文件夹列表
    private static HashMap<String,Integer> folderMap = new HashMap<>();

    //上下文以及数据库
    private Context context;
    static MyDataBase myDataBase ;
    static UserDao userDao;
    static SongDao songDao;
    static SongListDao songListDao;
    static SongListWithSongCrossRefDao songListWithSongCrossRefDao;


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
        //创建数据库对象
        myDataBase = MyDataBase.getInstance(context.getApplicationContext());
        songDao = myDataBase.songDao();
        songListDao = myDataBase.songListDao();
        userDao = myDataBase.userDao();
        songListWithSongCrossRefDao = myDataBase.songListWithSongCrossRefDao();

        /*---------------------从本地拿取所有的音乐----------------- */
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
            String path=cursor.getString(dataCol);//文件全路径
            String displayName=cursor.getString(displayNameCol);//文件全名称
            String year=cursor.getString(yearCol);//发行日期
            int duration=cursor.getInt(durationCol);//总播放时长
            long size=cursor.getLong(sizeCol);//文件大小
            int albumId = cursor.getInt(albumIdCol);
            Log.e("1、展示的名字",title+" "+ album+ " " +displayName +" " + albumId);
            Log.e("2、读取到的歌曲","歌曲id:"+id+"歌曲名称:"+title+"歌曲专辑:"+album+"歌曲路径:" +path+"歌手名:"+singer+"发行日期:"+year+"总播放时长:"+duration+"文件大小:"+size);


            MusicInfoModel musicInfoModel = new MusicInfoModel(title,singer,album,duration,albumId,path,id);
            //处理有时候歌曲名异常显示的情况，先分割出来歌曲名和歌手名
            if (title.contains("-")) {
                String[] str = title.split("-");
                musicInfoModel.setSinger(str[0].trim());
                musicInfoModel.setMusicName(str[1].trim());
                Log.e("3、这首歌的歌名是"," "+musicInfoModel.getMusicName());
                Log.e("4、这首歌的歌手是"," "+musicInfoModel.getSinger());
            }
            //设置专辑封面
//            musicInfoModel.setBitmap(getAlbumArt(albumId));

            musicInfoModel.setBitmap(getAlbumArtByPath(path));
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

            //4、将歌曲添加到文件夹列表
            String prePath = path.substring(0,path.lastIndexOf("/"));
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

            //设置歌曲排序id
            MusicUtil.setSortSong(musicInfoModel);


        }while (cursor.moveToNext());
        cursor.close();

        //初始化用户
        initUser(username,password);
        //初始化本地音乐
        initUserAllSongList(username);


    }

    /************数据库连接相关**********/
    //初始化用户
    private static void initUser(String name, String password){
        //正常流程要插入用户，并创建一个我的最爱歌单、最近播放歌单
        //todo Song增加一个属性 long lastPlay 上一次播放的时间 初始值为-1
//        1. 插入用户
        if(userDao.findByNameAndPassword(username,password) != null){//已经创建默认的用户
//            不做任何操作
            System.out.println("我已经有这个用户了");
        }else{
            userDao.insertUser(new User(name,password));//插入一个默认的用户
        }
        System.out.println(1);
//        2. 将本地歌曲导入到数据库中
        if(songDao.getAllSongs().size() == 0){
            for(MusicInfoModel musicInfoModel : allMusicList){
                songDao.insertSong(new Song(musicInfoModel.getPath(), musicInfoModel.getId(),musicInfoModel.getMusicName()));
            }
        }
        System.out.println(2);
        //3、创建两个默认歌单
        if(songListDao.getSongListById("我的最爱") == null){
            songListDao.insertSongList(new com.example.musicplayer.Entity.SongList("我的最爱",name,"我的最爱"));
        }
        if(songListDao.getSongListById("最近播放") == null){
            songListDao.insertSongList(new com.example.musicplayer.Entity.SongList("最近播放",name,"最近播放"));
        }
        System.out.println(3);
    }

    //todo 初始化时获取用户的歌单的信息   对应于后面的这个方法  getAllSongList
    private void initUserAllSongList(String username){
        List<UserWithSongLists> userWithSongLists = userDao.getUserWithSongLists();//获取所有用户歌单信息
        System.out.println(4);
//        通过获取的信息来实例化本地的allSongList
        for (UserWithSongLists usersong: userWithSongLists) {//  用户---歌单
            if(usersong.getUser().getUserName().equals(username)){//如果是这个用户绑定的歌单
                for(com.example.musicplayer.Entity.SongList songList : usersong.getSongLists()){//  这个用户下的每一个歌单
                    //对于这个用户的所有的歌单
//                    1、 拿到歌单名字
                    String songlistname  = songList.getSong_list_name();
                    //2、 去找这个歌单的所有的歌
                    //存储歌曲列表
                    List<MusicInfoModel> musicInfoModels = new ArrayList<>();
                    //db
                    List<String> songPaths =  songListWithSongCrossRefDao.getSongsBySongList(songlistname);
                    System.out.println("打印找到的所有的歌路径");
                    for(int i = 0 ; i < songPaths.size() ; i++){
                        System.out.println(songPaths.get(i));
                        musicInfoModels.add(findMusicInfoByPath(songPaths.get(i)));
                    }
                    //装载到本地所有歌单对象中
                    SongList localSongList =new SongList(songlistname,musicInfoModels);
                    if(songlistname.equals("我的最爱")){//我的最爱歌单，加载到本地的我的最爱歌单里面
                        myFavoriteSongList = localSongList;
                    }else if(songlistname.equals("最近播放")){//todo 最近播放歌单，加载到本地的最近播放歌单里面
//                        allRecentMusicList = musicInfoModels;
                    }else{//其他歌单
                        allSongList.add(localSongList);//加载到本地显示的进来
                    }
                }
            }
        }
    }
    //新建歌单
    private static void insertSongList(SongList songList, String username){
        //插入歌单
        //插入一个歌单 用户与歌单绑定
        System.out.println(5);
        songListDao.insertSongList(new com.example.musicplayer.Entity.SongList(songList.getSongListName(),username,songList.getSongListName()));
        Log.e(songList.getSongListName(),songListDao.getAllSongList().size()+"");
        //插入歌单歌曲的联系 歌单与歌曲绑定
        for (MusicInfoModel musicInfoModel: songList.getMusicList()){
            songListWithSongCrossRefDao.insertSongListWithSongCrossRef(new SongListWithSongCrossRef(songList.getSongListName(),musicInfoModel.getPath()));
            Log.e("新建歌单",musicInfoModel.getMusicName()+songList.getSongListName());
            System.out.println(6);
        }
    }
    //向歌单中添加歌曲
    private static void addSongToSongList(String SongListName, MusicInfoModel musicInfoModel){
        songListWithSongCrossRefDao.insertSongListWithSongCrossRef(new SongListWithSongCrossRef(SongListName,musicInfoModel.getPath()));
        System.out.println("向歌单中添加信息"+7);
    }

    //向歌单中删除歌曲
    private static void deleteSongFromSongList(String SongListName, MusicInfoModel musicInfoModel){
        songListWithSongCrossRefDao.delete(new SongListWithSongCrossRef(SongListName,musicInfoModel.getPath()));
        System.out.println("向歌单中删除信息"+8);
    }


    /*************************数据库连接相关************/





    //安卓10以下使用
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

    //通过路径获取封面 安卓通用
    public static Bitmap getAlbumArtByPath(String path){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        Bitmap songImage;
        try {
            byte [] streamPicture = mediaMetadataRetriever.getEmbeddedPicture();
            songImage = BitmapFactory
                    .decodeByteArray(streamPicture, 0, streamPicture.length);
        } catch (Exception e) {
            songImage = null;
        }
        return songImage;
    }


    //返回本地所有音乐的list
    public static List<MusicInfoModel> getMusicList() {
        return allMusicList;
    }

    //返回所有歌手的list
    public static List<Singer> getAllSingerList() {
        return allSingerList;
    }
    //返回所有专辑的list
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


    //返回singer二级页面的内容，即二级页面的singers
    public static List<Singer> getSingers(){
        //首次获取，此时静态列表还未创建完毕
        if(allSingerList.size() == 0){
            for(MusicInfoModel musicInfoModel : allMusicList){
                System.out.println(musicInfoModel.getMusicName()+" "+musicInfoModel.getId());
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


    //返回album二级页面的内容，即二级页面的albums
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

    //读取到本地所有分类文件夹
    public static List<Folder> getAllFolderList() {
        return allFolderList;
    }

    //todo 从数据库中读取到所有这个用户创建的歌单
    public static List<SongList> getAllSongList() {
        return allSongList;
    }


    //todo 用户创建歌单，1、向数据库中增加歌单数据 2、将这个歌单加入到MusicUtil的静态歌单列表
    public static void addSongList(SongList songList){
        allSongList.add(songList);//先添加到本地
        insertSongList(songList,username);//添加到数据库

    }
    //从本地的歌单列表中，通过歌单名字来找歌单
    public static SongList findSongListBySongListName(String name){
        if(name.equals("我的最爱")){
            return myFavoriteSongList;
        }else{
            //todo 此处应该是要去数据库中拿到这个歌单
            for(SongList songList: allSongList){
                if(songList.getSongListName().equals(name)){
                    return songList;
                }
            }
        }
        return null;
    }

    //从本地找到歌单，并返回这个歌单里面的歌曲
    public static List<MusicInfoModel> findMusicListBySongListName(String songListName){
        if(songListName.equals("我的最爱")){
            return myFavoriteSongList.getMusicList();
        }else{
            for(SongList songList: allSongList){
                if(songList.getSongListName().equals(songListName)){
                    return songList.getMusicList();
                }
            }
        }
        return null;
    }
    //通过歌手名字找得到歌手的相关信息
    public static Singer findSingerBySingerName(String singerName){
        for(Singer singer : allSingerList){
            if(singer.getSingerName().equals(singerName)){
                return singer;
            }
        }
        return null;
    }


    //通过专辑名字找得到专辑的相关信息
    public static Album findAlbumByAlbumName(String albumName){
        for(Album album : allAlbumList){
            if(album.getAlbumName().equals(albumName)){
                return album;
            }
        }
        return null;
    }


    //todo 数据库的实现？获取最近播放的歌曲列表
    public static List<MusicInfoModel> getAllRecentMusicList() {
        return allRecentMusicList;
    }

    //todo 向最近播放歌曲列表中添加歌曲
    public static void addRecentMusic(MusicInfoModel musicInfoModel){
        //如果列表中已经有了这首歌，就把它添加到列表末端
        int id = musicInfoModel.getId();
        System.out.println("这首歌的id是"+musicInfoModel.getId());
        if(recentSongMap.get(id) != null){//todo 这一部分有问题
            int index = -1;
            for (int i = allRecentMusicList.size()-1 ; i >= 0 ; i--){
                if(allRecentMusicList.get(i).getMusicName().equals(musicInfoModel.getMusicName())){
                    System.out.println("要移除掉这首歌"+allRecentMusicList.get(i).getMusicName());
                    index = i;
                    break;
                }
            }
            if(index >-1){
                allRecentMusicList.remove(index);
                System.out.println("移除完了");
            }
        }else{//列表中没有，直接添加到末端
            recentSongMap.put(musicInfoModel.getId(),musicInfoModel.getMusicName());//放到hashmap中
        }
        allRecentMusicList.add(musicInfoModel);
        //打印一下当前的最近播放列表
        for (int i = allRecentMusicList.size()-1 ; i >= 0 ; i--){
            Log.e("最近播放列表"+i ,allRecentMusicList.get(i).getMusicName());
        }
    }
    //清除最近播放列表
    public static void clearRecent(){
        allRecentMusicList.clear();
    }

    //获取我的最爱的歌曲列表
    public static List<MusicInfoModel> getAllFavoriteMusicList() {
        return allFavoriteMusicList;
    }

    //todo 需要操作数据库 移除歌单中的歌曲
    public static boolean deleteFavoriteMusic(MusicInfoModel musicInfoModel){
        for(MusicInfoModel musicInfoModel1 : myFavoriteSongList.getMusicList()){
            if(musicInfoModel1.getMusicName().equals(musicInfoModel.getMusicName())){
                Log.e("我在MusicUtil里面要移除这首歌的喜欢了",musicInfoModel.getMusicName());
                myFavoriteSongList.getMusicList().remove(musicInfoModel1);
                //todo 操作数据库
                deleteSongFromSongList("我的最爱",musicInfoModel);
                return false;
            }
        }
        return true;
    }
    //todo 向我的最爱列表中添加歌曲
    public static boolean addFavoriteMusic(MusicInfoModel musicInfoModel){
//        1、本地添加
        //如果列表中已经有了这首歌，就把它添加到列表末端
        int id = musicInfoModel.getId();
        System.out.println("这是我的最爱这首歌的id是"+musicInfoModel.getId());
        for(MusicInfoModel musicInfoModel1 : myFavoriteSongList.getMusicList()){
            if(musicInfoModel1.getMusicName().equals(musicInfoModel.getMusicName())){
                Log.e("已经添加过了",musicInfoModel.getMusicName());
                return false;
            }
        }
        //没有添加过
        myFavoriteSongList.getMusicList().add(musicInfoModel);

//        2、数据库添加
        addSongToSongList("我的最爱",musicInfoModel);
        return true;
    }

    //普通歌单添加歌曲
    public static boolean addNormalMusic(SongList songList,MusicInfoModel musicInfoModel){
        for(MusicInfoModel musicInfoModel1 : songList.getMusicList()){
            if(musicInfoModel1.getMusicName().equals(musicInfoModel.getMusicName())){
                Log.e("已经添加过了",musicInfoModel.getMusicName());
                return false;
            }
        }
        //没有添加过 ,先在本地加，再去数据库加
        songList.getMusicList().add(musicInfoModel);
//        2、数据库添加
        addSongToSongList(songList.getSongListName(),musicInfoModel);
        return true;
    }


    //获得我的最爱歌单
    public static SongList getMyFavoriteSongList() {
        return myFavoriteSongList;
    }


    //歌曲初始排序
    public static void setSortSong(MusicInfoModel music){
        if(MusicUtil.checkFirstIsEnglish(music.getMusicName())){
            String name = music.getMusicName();
            music.setSortSongId(""+Character.toLowerCase(name.charAt(0)));
            music.setSortSongName(name);
        }else{
            try {
                String pingYin = PinyinHelper.convertToPinyinString(music.getMusicName(), " ", PinyinFormat.WITHOUT_TONE);
                Log.e("转换",pingYin);
                music.setSortSongId( pingYin.substring(0, 1));
                music.setSortSongName(pingYin);
            } catch (PinyinException e){
                e.printStackTrace();
            }
        }
    }
    public static String formatTime(int time) {
        int miao = (time /= 1000);
        int minute = miao / 60;
        int second = miao % 60;
        return String.format("%02d:%02d", minute, second);
    }
    //通过路径找到歌曲
    public static MusicInfoModel findMusicInfoByPath(String path){
        for(MusicInfoModel musicInfoModel : allMusicList){
            if(musicInfoModel.getPath().equals(path)){
                return musicInfoModel;
            }
        }
        return null;
    }
}
