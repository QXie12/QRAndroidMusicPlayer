package com.example.sidebar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.sidebar.Music.Music;
import com.example.sidebar.Music.MusicAdapter;

import java.io.File;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener{

    private SeekBar seekBar;
    private ImageButton playButton;
    private MusicService.MyBinder mm;
    private Timer timer;
    private File file;
    private TextView songName;
    private TextView singer;
    private TextView musicTimes; //当前
    private int musictimes = 0;
    private int musicTimes_f = 0;
    private int musicTimes_s = 0;
    private ImageButton preSong;
    private ImageButton nextSong;
    private List<Music> music_List = new ArrayList<>();
    private MusicAdapter musicAdapter;

    private static final String TAG = "OrderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        System.out.println("建立连接");
        Intent intent = new Intent(OrderActivity.this,MusicService.class);
//        startService(intent);
        bindService(intent,sc, BIND_AUTO_CREATE);
        playButton = findViewById(R.id.play_Song);
        preSong = findViewById(R.id.pre_Song);
        nextSong = findViewById(R.id.next_Song);
        musicTimes = findViewById(R.id.music_currentTime);
        songName = findViewById(R.id.music_name);
        singer = findViewById(R.id.music_author);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playButton.setOnClickListener(this); //监听点击事件
        preSong.setOnClickListener(this);
        nextSong.setOnClickListener(this);
//        musicAdapter = new MusicAdapter(this,music_List);
        //TODO：先不管本机音乐，假设已经有了
        //        LoadLocalData();
//        initMediaPlay();
        if(MusicService.isPlay){
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.music_play));
        }
        else
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.music_suspend));
        bindViews();
    }
    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            System.out.println("初始化service。。。");
            mm = (MusicService.MyBinder)iBinder;
            try {
                seekBar.setMax(mm.getDuration());
                System.out.println("歌曲时长:"+mm.getDuration());
//            System.out.println("音乐名："+file.getName());
//            songName.setText(file.getName());
                //TODO:

                // 显示歌手
            }catch (Exception e){
                e.printStackTrace();
            }
            timer();
            System.out.println("初始化service");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //按钮点击事件
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_Song:
                if (!MusicService.isPlay){
                    ((ImageButton)view).setImageDrawable(getResources().getDrawable(R.drawable.music_play));
                    mm.start();
                    MusicService.isPlay = true;
                    System.out.println("....");
                    timer();
                }else if(MusicService.isPlay) {
                    ((ImageButton)view).setImageDrawable(getResources().getDrawable(R.drawable.music_suspend));
                    MusicService.isPlay = false;
                    mm.pause();
                    timer.cancel();
                }
                break;
            case R.id.next_Song:
                //切下一首
                //TODO:
//                    ??????
                break;
            case R.id.pre_Song:
                //切上一首
                //TODO:
            default:
                break;
        }
    }

    private void timer() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mm.getCurrentPosition());
                musictimes = seekBar.getProgress();
                musicTimes_f=musictimes/60000;
                musicTimes_s=(musictimes%60000)/1000;
                musicTimes.post(new Runnable() {
                    @Override
                    public void run() {
                        musicTimes.setText(musicTimes_f+":"+musicTimes_s+" / "+seekBar.getMax()/60000+":"+(seekBar.getMax()%60000)/1000);
                    }
                });
                //先默认单曲循环
                if (seekBar.getMax()==seekBar.getProgress()){
                    mm.seekTo(0);
                    mm.start();
                }

                //TODO：不同的播放方式：单曲循环，随机播放。。。。
//                if (seekBar.getMax()==seekBar.getProgress() && stop.getText().equals("不单曲循环")){
//                    musictime.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mediaPlayer.seekTo(0);
//                            seekBar.setProgress(0);
//                            onClick(paly);
//                            timer.cancel();
//                        }
//                    });
//                }

                /*    Log.d(String.valueOf(MainActivity.this),"时间长度"+musictimes_s);*/
            }

        },0,100);
    }
    private void bindViews() {
        //进度条监听事件
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //拖动进度条
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser==true){
                    mm.seekTo(progress);
                }
            }

            //开始滑动滑块时调用
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (MusicService.isPlay){
                    mm.pause();
                }
            }
            //结束对滑动滑动时
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mm.seekTo(seekBar.getProgress());
                mm.start();
                if(!MusicService.isPlay){
                    mm.pause();
                }
            }
        });
    }
}