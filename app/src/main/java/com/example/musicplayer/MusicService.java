package com.example.musicplayer;

import static android.app.Notification.CATEGORY_MESSAGE;
import static android.app.Notification.DEFAULT_ALL;
import static android.app.Notification.FLAG_ONGOING_EVENT;
import static android.app.Notification.PRIORITY_MAX;
import static android.content.ContentValues.TAG;

import static com.example.musicplayer.broadcastReceiver.PlayerReceiver.PLAY_NEXT;
import static com.example.musicplayer.broadcastReceiver.PlayerReceiver.PLAY_PAUSE;
import static com.example.musicplayer.broadcastReceiver.PlayerReceiver.PLAY_PRE;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.broadcastReceiver.PlayerReceiver;
import com.example.musicplayer.common.MusicUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//操作音乐播放
public class MusicService extends Service {
    public static boolean isPlay = false; //音乐播放器的状态
    private static MediaPlayer musicplayer;
    static int time;
    static String musicName;
    static String  singer;
    static Timer timer;
    static String path;
    static String like = "false";
    public final IBinder binder = new MyBinder();
    private MusicUtil musicUtil;; //获取本地音乐
    public static List<MusicInfoModel> musicInfoModelList;
    private static int playStatus = 1; //播放状态：循环播放（0）、顺序播放（1）、随机播放（2）。默认顺序播放
    private static int current = 0; //当前在音乐列表中的位置
    public static boolean isChanged = false;
    private static String MusicActivity_isStart = "false";
    //给用户看的渠道名称
    private String channelId;
    private String channelName;
    private static int notificationId = 1;
    private static NotificationManager notificationManager;
    private static RemoteViews remoteViews;
    private static NotificationCompat.Builder builder;
    private static Notification notification;
    private static BroadcastReceiver broadcastReceiver;
    public static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //下面接受activity发送的消息，更新主页下面歌曲的信息
            Log.d(TAG,"MusicService收到消息");
            MusicActivity_isStart = String.valueOf(msg.getData().get("isStart"));
            System.out.println("musicActivity打开了"+MusicActivity_isStart);
            //musicactivity打开了，service就要向它发送消息
            Log.e("music","ll"+musicInfoModelList);
            time = musicInfoModelList.get(current).getTime();
            musicName = musicInfoModelList.get(current).getMusicName();
            singer = musicInfoModelList.get(current).getSinger();
            path = musicInfoModelList.get(current).getPath();
            Bundle bundle = new Bundle();
            bundle.putString("musicName",musicName);  //往Bundle中存放数据
            bundle.putString("singer",singer);  //往Bundle中put数据
            bundle.putString("path",path);
            if(timer!=null){
                timer.cancel();
                Log.d(TAG,"musicaactivity打开了，不是第一次放歌，需要timer.cancel()");
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //实例化一个Message对象
                    Message msg = Message.obtain();
                    //Message对象的arg1，arg2参数携带音乐当前播放进度信息，类型是int
                    msg.arg1 = time;
                    //在播放过程中点了喜欢，需要更新信息
                    System.out.println("最喜欢的列表"+MusicUtil.getMyFavoriteSongList().getMusicList());
                    for(MusicInfoModel musicInfoModel : MusicUtil.getMyFavoriteSongList().getMusicList()){
                        System.out.println("最喜欢的列表中的歌曲"+musicInfoModel.getMusicName());
                    }
                    System.out.println("当前播放的歌曲"+musicInfoModelList.get(current).getMusicName());
                    if(MusicUtil.getMyFavoriteSongList().getMusicList().contains(musicInfoModelList.get(current))){
                        //设成小红心
                        like = "true";
                    }else
                        like = "false";
                    bundle.putString("like",like);
                    if(musicplayer!=null){
                        msg.arg2 = musicplayer.getCurrentPosition();
                        msg.setData(bundle);//mes利用Bundle传递数据
                        //使用MusicActivity中的handler发送信息
                        if(isPlay){
                            MusicActivity.handler.sendMessage(msg);
                        }
                    }
                }
            }, 0, 100);

            return false;
        }
    });
    //接受广播的msg
    public static Handler handler2 = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message message) {
            Log.e(TAG,"MusicService收到广播接收器的消息");
            if(String.valueOf(message.getData().get("action")).equals("next")){
                //下一首
                next_Song();
            }
            else if(String.valueOf(message.getData().get("action")).equals("pre")){
                //上一首
                pre_Song();
            }
            else {
                if(isPlay){
                    pause();
                    remoteViews.setImageViewResource(R.id.music_notification_play,R.drawable.ic_music_down);
                    notificationManager.notify(notificationId, notification);
                }else{
                    if(!isChanged){
                        System.out.println("3");
                        System.out.println(musicInfoModelList);
                        mPlay();
                    }else {
                        start();
                        remoteViews.setImageViewResource(R.id.music_notification_play,R.drawable.ic_music_on);
                        notificationManager.notify(notificationId, notification);
                    }

                }

            }
            return false;
        }
    });
    public MusicService(){
    }

    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        Log.i("bind","onbind;;;");
        return binder;
    }
    public class MyBinder extends Binder {
        public void pause(){
            if(musicplayer!=null && musicplayer.isPlaying()){
                musicplayer.pause();
                isPlay = false;
            }
        }
        public void start(){
            if (musicplayer!=null && !musicplayer.isPlaying()){
                musicplayer.start();
                isPlay = true;
            }
        }
        public void play(){
            musicPlay(musicInfoModelList.get(current).getPath());
        }
        public boolean isChanged(){
            return isChanged;
        }
        public String getSongName(){
            return musicInfoModelList.get(current).getMusicName();
        }
        public String getSinger(){
            return musicInfoModelList.get(current).getSinger();
        }
        public String getPath(){
            return musicInfoModelList.get(current).getPath();
        }


        public int getCurrentPosition(){
            if (musicplayer!=null){
                return musicplayer.getCurrentPosition();
            }
            return 0;
        }
        public int getDuration(){
            return musicInfoModelList.get(current).getTime();
        }
        public void seekTo(int n){
            musicplayer.seekTo(n);
        }
        //上一首
        public void preSong(){
            pre_Song();
        }
        //下一首
        public void nextSong(){
            next_Song();
        }
        //点击列表歌曲播放
        public void play(String path){
            //相当于切歌
            if(isChanged) { //不是第一次播放
                timer.cancel();
                musicplayer.stop();
                isPlay = false;
                Log.d(TAG,"停止上一首向musicactivity发消息");
            }
            musicPlay(path);
        }
        public int getPlayStatus(){
            return playStatus;
        }
        public void setPlayStatus(int status){
            playStatus = status;
        }
        public void close_timer(){
            timer.cancel();
        }
        public boolean exist(){
            if(musicplayer!=null)
                return true;
            else
                return false;
        }
        public int getCurrent(int musicId) {
            int i;
            for (i=0;i < musicInfoModelList.size(); i++) {
                if (musicInfoModelList.get(i).getId() == musicId) {
                    current = i;
                    Log.d(TAG,"当前current="+current);
                    break;
                }
            }
            return i;
        }
        public void setCurrent(int num){
            current = getCurrent(num);
        }
        public void timer_cancel(){
            timer.cancel();
        }
        public MusicInfoModel getMusic(){
            return musicInfoModelList.get(current);
        }
        public String getLike(){
            return like;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ...");
//        musicUtil = new MusicUtil(this);
        musicplayer = new MediaPlayer();
        musicInfoModelList = MusicUtil.getMusicList(); //获取了本地音乐列表
        Log.d(TAG,"1");
        System.out.println("musicList:"+musicInfoModelList.get(current).getPath());
        broadcastReceiver = new PlayerReceiver(); //接收器
        //TODO 通知栏
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        channelId = "music_notification";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//适配一下高版本
            NotificationChannel channel = new NotificationChannel(channelId,
                    "音乐播放",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);//关了通知默认提示音
            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
        }
        builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.watermelon)//这玩意在通知栏上显示一个logo
                .setCategory(CATEGORY_MESSAGE)
                .setDefaults(DEFAULT_ALL)
                .setOngoing(true);
        Intent intent = new Intent(this,MusicActivity.class); //跳转到musicactivity
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setAutoCancel(false);
        builder.setSound(null);
        builder.setPriority(PRIORITY_MAX);
        builder.setContentIntent(pendingIntent); //跳转activity
        if(MusicUtil.getAlbumArtByPath(String.valueOf(musicInfoModelList.get(current).getPath()))==null){
            remoteViews = initNotification(R.drawable.gai,musicInfoModelList.get(current).getMusicName(),musicInfoModelList.get(current).getSinger());
        }else {
            remoteViews = initNotification(R.drawable.gai,musicInfoModelList.get(current).getMusicName(),musicInfoModelList.get(current).getSinger());
            remoteViews.setImageViewBitmap(R.id.music_notification_image,MusicUtil.getAlbumArtByPath(musicInfoModelList.get(current).getPath()));
        }

        builder.setContent(remoteViews); //设置我们自定义的remoteview
        builder.setCustomBigContentView(remoteViews); //不知道干啥的
        notification = builder.build();
        notification.flags |= FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;//不让手动清除 通知栏常驻
        notification.sound = null;//关了通知默认提示音
        notificationManager.notify(notificationId, notification);
    }
    //播放歌曲必须通过这个方法！
    public static void musicPlay(String path){
        if(timer!=null){
            timer.cancel();
        }
        try {
            System.out.println("当前在不在播放："+isPlay);
            Log.d(TAG,"播放新的一首歌，歌曲路径是："+path);
            if (isChanged){
                Log.d(TAG,"下一首歌，重置musicplayer");
                musicplayer.reset(); //重置
                musicplayer.release();
                musicplayer = null;
                musicplayer = new MediaPlayer();
            }
            musicplayer.setDataSource(path);
            musicplayer.prepareAsync();
            //需要设置一个监听器
            musicplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isChanged = true;
                    Log.d(TAG,"歌曲准备好了");
                    musicplayer.start();
                    MusicUtil.addRecentMusic(musicInfoModelList.get(current));
//                    Log.d("时间",""+System.currentTimeMillis()/1000);
                    isPlay = true;
                    time = musicplayer.getDuration();
                    musicName = musicInfoModelList.get(current).getMusicName();
                    singer = musicInfoModelList.get(current).getSinger();
                    //歌曲信息更新后要更新通知栏
                    if(MusicUtil.getAlbumArtByPath(String.valueOf(musicInfoModelList.get(current).getPath()))==null){
                        remoteViews.setImageViewResource(R.id.music_notification_image,R.drawable.gai);
                    }else {
                        remoteViews.setImageViewBitmap(R.id.music_notification_image,MusicUtil.getAlbumArtByPath(musicInfoModelList.get(current).getPath()));
                    }

                    remoteViews.setTextViewText(R.id.music_notification_name,musicName);
                    remoteViews.setTextViewText(R.id.music_notification_singer,singer);
                    if(isPlay){
                        remoteViews.setImageViewResource(R.id.music_notification_play,R.drawable.ic_music_on);
                    }
                    else
                        remoteViews.setImageViewResource(R.id.music_notification_play,R.drawable.ic_music_down);
                    notificationManager.notify(notificationId, notification);
                    Bundle bundle = new Bundle();
                    bundle.putString("musicName",musicName);  //往Bundle中存放数据
                    bundle.putString("singer",singer);  //往Bundle中put数据
                    bundle.putString("path",path);
                    Message msg2 = Message.obtain();
                    msg2.setData(bundle); //传给mainActivity的
                    MainActivity.handler.sendMessage(msg2);
                    //每隔50毫秒发送音乐进度
                    Log.d(TAG,"接下来给musicActivity发消息");
                    if(MusicActivity_isStart.equals("true")){
                        if(timer!=null){
                            timer.cancel();
                        }
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                //实例化一个Message对象
                                Message msg = Message.obtain();
                                //Message对象的arg1，arg2参数携带音乐当前播放进度信息，类型是int
                                msg.arg1 = time;
                                if(musicplayer!=null){
                                    msg.arg2 = musicplayer.getCurrentPosition();
                                    msg.setData(bundle);//mes利用Bundle传递数据
                                    if(MusicUtil.getMyFavoriteSongList().getMusicList().contains(musicInfoModelList.get(current))){
                                        //设成小红心
                                        like = "true";
                                    }else
                                        like = "false";
                                    Log.d(TAG,like);
                                    bundle.putString("like",like);
                                    //使用MusicActivity中的handler发送信息
                                    MusicActivity.handler.sendMessage(msg);
                                    if(isPlay){
                                        remoteViews.setImageViewResource(R.id.music_notification_play,R.drawable.ic_music_on);
                                    }
                                    else
                                        remoteViews.setImageViewResource(R.id.music_notification_play,R.drawable.ic_music_down);
                                }
                            }
                        }, 0, 100);
                    }
                }
            });
            Log.d(TAG,"开始播放下一首歌了");

        } catch (IOException e) {
            e.printStackTrace();
        }
        //监听音乐播放完
        musicplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(MusicActivity_isStart.equals("true")){
                    timer.cancel();
                }
                Log.d(TAG,"一首歌放完了。");
                if(musicplayer != null){
                    Log.d(TAG,"停止当前播放的这首歌");
                    musicplayer.stop();
//                    musicplayer.release();
//                    musicplayer = null;
                    isPlay = false;
                }
                //实现三种播放方式
                switch (playStatus){
                    case 0:
                        //循环
                        Log.d(TAG,"当前是循环播放");
                        isChanged = true;
                        musicPlay(musicInfoModelList.get(current).getPath());
                        break;
                    case 1:
                        //顺序
                        if(current < musicInfoModelList.size() -1){
                            Log.d(TAG,"顺序播放下一首");
                            isChanged = true;
                            musicPlay(musicInfoModelList.get(++current).getPath());
                        }
                        else{
                            Log.d(TAG,"播完了，从第一首开始");
                            isChanged = true;
                            current = 0;
                            musicPlay(musicInfoModelList.get(0).getPath());
                        }

                        break;
                    case 2:
                        //随机
                        Log.d(TAG,"当前是随机播放");
                        isChanged = true;
                        int index = new Random().nextInt(musicInfoModelList.size()-1);
                        while (true){
                            if(index!=current)
                                break;
                            else{
                                index = new Random().nextInt(musicInfoModelList.size()-1);
                            }
                        }
                        current = index;
                        musicPlay(musicInfoModelList.get(index).getPath());
                        break;
                }

            }
        });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ...");
        if(!musicplayer.isPlaying()){
            musicplayer.start(); //开始播放
            isPlay = true;
        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ....");
        if(musicplayer.isPlaying()){
            musicplayer.stop();
            isPlay = false;
        }
        musicplayer.release();
    }
    //通知栏在此初始化
    private RemoteViews initNotification(int path,String music_name,String singer){
        String packageName = this.getPackageName();
        remoteViews = new RemoteViews(packageName,R.layout.music_notification);
        remoteViews.setImageViewResource(R.id.music_notification_image,path);
        remoteViews.setTextViewText(R.id.music_notification_name,music_name);
        remoteViews.setTextViewText(R.id.music_notification_singer,singer);
        if(isPlay){
            remoteViews.setImageViewResource(R.id.music_notification_play,R.drawable.ic_music_on);
        }
        else
            remoteViews.setImageViewResource(R.id.music_notification_play,R.drawable.ic_music_down);
        remoteViews.setImageViewResource(R.id.music_notification_pre,R.drawable.ic_skip_previous);
        remoteViews.setImageViewResource(R.id.music_notification_next,R.drawable.ic_skip_next);
        Intent prv = new Intent(this, PlayerReceiver.class);//播放上一首
        prv.setAction(PLAY_PRE);
        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, prv,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.music_notification_pre, intent_prev);

        Intent next = new Intent(this,PlayerReceiver.class);//播放下一首
        next.setAction(PLAY_NEXT);
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 2, next,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.music_notification_next, intent_next);

        Intent startpause = new Intent(this,PlayerReceiver.class);//暂停/播放
        startpause.setAction(PLAY_PAUSE);
        PendingIntent intent_pause = PendingIntent.getBroadcast(this, 3, startpause,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.music_notification_play, intent_pause);
        return remoteViews;

    }

    //上一首
    public static void pre_Song(){
        if(timer!=null){
            timer.cancel();
        }
        if(playStatus==0 || playStatus==1){
            if(current > 0){
                Log.d(TAG,"顺序播放上一首");
                isChanged = true;
                musicPlay(musicInfoModelList.get(--current).getPath());
            }
            else{
                Log.d(TAG,"已经是第一首了，就播放最后一首");
                isChanged = true;
                current = musicInfoModelList.size()-1;
                musicPlay(musicInfoModelList.get(musicInfoModelList.size()-1).getPath());
            }
        }else {
            //随机播放上一首
            int index = new Random().nextInt(musicInfoModelList.size()-1);
            while (true){
                if(index!=current)
                    break;
                else{
                    index = new Random().nextInt(musicInfoModelList.size()-1);
                }
            }
            isChanged = true;
            current = index;
            musicPlay(musicInfoModelList.get(index).getPath());
        }


    }
    //下一首
    public static void next_Song() {
        if(timer!=null){
            timer.cancel();
        }
        if (playStatus == 1 || playStatus == 0) {
            //循环或顺序播放就按顺序到下一首
            if (current < musicInfoModelList.size() - 1) {
                Log.d(TAG, "顺序播放下一首");
                isChanged = true;
                musicPlay(musicInfoModelList.get(++current).getPath());
            } else {
                Log.d(TAG, "播完了，从第一首开始");
                isChanged = true;
                current = 0;
                musicPlay(musicInfoModelList.get(0).getPath());
            }
        } else {
            //随机播放下一首
            int index = new Random().nextInt(musicInfoModelList.size() - 1);
            while (true) {
                if (index != current)
                    break;
                else {
                    index = new Random().nextInt(musicInfoModelList.size() - 1);
                }
            }
            isChanged = true;
            current = index;
            musicPlay(musicInfoModelList.get(index).getPath());
        }
    }
    public boolean get_isChanged(){
        return isChanged;
    }
    public static void mPlay(){
        System.out.println("2");
        musicPlay(musicInfoModelList.get(current).getPath());
    }
    public static void pause(){
        if(musicplayer!=null && musicplayer.isPlaying()){
            musicplayer.pause();
            isPlay = false;
        }
    }
    public static void start(){
        if (musicplayer!=null && !musicplayer.isPlaying()){
            musicplayer.start();
            isPlay = true;
        }
    }

}

