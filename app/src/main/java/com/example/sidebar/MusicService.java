package com.example.sidebar;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

//操作音乐播放
public class MusicService extends Service {
//    private static MediaPlayer musicplayer;
    public static boolean isPlay; //音乐播放器的状态
    private MediaPlayer musicplayer;
    public final IBinder binder = new MyBinder();
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
            }
        }
        public void start(){
            if (musicplayer!=null && !musicplayer.isPlaying()){
                musicplayer.start();
            }
        }
        public void stop() {
            if (musicplayer!=null){
                musicplayer.stop();
                try {
                    musicplayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public int getCurrentPosition(){
            if (musicplayer!=null){
                return musicplayer.getCurrentPosition();
            }
            return 0;
        }
        public int getDuration(){
            return musicplayer.getDuration();
        }
        public void seekTo(int n){
            musicplayer.seekTo(n);
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ...");
        System.out.println("oncreate了啦啦啦啦啦啦啦啦啦啦啦啦啦");
        musicplayer = new MediaPlayer();
        musicplayer = MediaPlayer.create(this,R.raw.whistle);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ...");
        System.out.println("onstartCommand了啦啦啦啦啦啦啦啦啦");
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
}
