package com.example.musicplayer;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.common.MusicUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    private static SeekBar seekBar;
    private ImageButton playButton;
    private MusicService.MyBinder mm;
    private Timer timer;
    private File file;
    private static TextView songName;
    private static TextView singer;
    private static TextView musicTimes; //当前
    private static int musictimes = 0;
    private static int musicTimes_f = 0;
    private static int musicTimes_s = 0;
    private ImageButton preSong;
    private ImageButton nextSong;
    private ImageButton playStatus;
    private ImageButton music_return;
    private static ImageView music_fengmian;
    public static String isStart = "false";
    private int way = 0; //默认单曲循环
    public static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //下面接受service发送的消息，更新seekbar
            // super.handleMessage(msg);
            // 将SeekBar位置设置到当前播放位置，
            // msg.arg1是service传过来的音乐播放进度信息,将其设置为进度条进度
            seekBar.setMax(msg.arg1); //设置进度条的长度
            seekBar.setProgress(msg.arg2);
            songName.setText(String.valueOf(msg.getData().get("musicName")));
            singer.setText(String.valueOf(msg.getData().get("singer")));
            if(MusicUtil.getAlbumArtByPath(String.valueOf(msg.getData().get("path")))==null){
                music_fengmian.setImageResource(R.drawable.gai);
            }else {
                music_fengmian.setImageBitmap(MusicUtil.getAlbumArtByPath(String.valueOf(msg.getData().get("path"))));
            }
            musictimes = seekBar.getProgress();
            musicTimes_f=musictimes/60000;
            musicTimes_s=(musictimes%60000)/1000;
            musicTimes.setText(formatTime(musictimes)+"/"+formatTime(seekBar.getMax()));
//            musicTimes.setText(musicTimes_f+":"+musicTimes_s+" / "+seekBar.getMax()/60000+":"+(seekBar.getMax()%60000)/1000);
//            musicTimes.post(new Runnable() {
//                @Override
//                public void run() {
//                    musicTimes.setText(musicTimes_f+":"+musicTimes_s+" / "+seekBar.getMax()/60000+":"+(seekBar.getMax()%60000)/1000);
//                }
//            });
            return false;
        }
    });
    private static String formatTime(int time) {
        int miao = (time /= 1000);
        int minute = miao / 60;
        int second = miao % 60;
        return String.format("%02d:%02d", minute, second);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Intent intent = new Intent(MusicActivity.this,MusicService.class);
//        startService(intent);
        Bundle bundle = new Bundle();
        isStart = "true";
        bundle.putString("isStart",isStart);  //往Bundle中存放数据
        Message msg = Message.obtain();
        msg.setData(bundle); //传给MusicService的
        MusicService.handler.sendMessage(msg); //告诉service我已经打开了
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
        playStatus = findViewById(R.id.music_way);
        playStatus.setOnClickListener(this);
        music_return = findViewById(R.id.music_return);
        music_return.setOnClickListener(this);
        music_fengmian = findViewById(R.id.profile_image);

        bindViews();
    }
    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            System.out.println("初始化service。。。");
            mm = (MusicService.MyBinder)iBinder;
            //一些音乐播放界面初始化操作在这里完成
            way = mm.getPlayStatus();
            if(way==0){
                playStatus.setImageDrawable(getResources().getDrawable(R.drawable.music_xunhuan)); //单曲循环
            }
            else if(way==1){
                playStatus.setImageDrawable(getResources().getDrawable(R.drawable.music_shunxu)); //顺序播放
            }
            else
                playStatus.setImageDrawable(getResources().getDrawable(R.drawable.music_suiji)); //随机播放

            if(MusicService.isPlay){
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.music_play));
            }
            else
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.music_suspend));
            try {
                seekBar.setMax(mm.getDuration());
                System.out.println("歌曲时长:"+mm.getDuration());
            }catch (Exception e){
                e.printStackTrace();
            }
//            timer();
            if(MusicUtil.getAlbumArtByPath(String.valueOf(mm.getPath()))==null){
//                music_fengmian.setImageResource(R.drawable.gai);
                Glide.with(MusicActivity.this).load(R.drawable.gai).into(music_fengmian);
            }else {
//                music_fengmian.setImageBitmap(MusicUtil.getAlbumArtByPath(mm.getPath()));
                Glide.with(MusicActivity.this).load(MusicUtil.getAlbumArtByPath(mm.getPath())).into(music_fengmian);

            }
            seekBar.setProgress(mm.getCurrentPosition());
            songName.setText(mm.getSongName());
            singer.setText(mm.getSinger());
            musictimes = seekBar.getProgress();
            musicTimes_f=musictimes/60000;
            musicTimes_s=(musictimes%60000)/1000;
//            musicTimes.setText(musicTimes_f+":"+musicTimes_s+" / "+seekBar.getMax()/60000+":"+(seekBar.getMax()%60000)/1000);
            musicTimes.setText(formatTime(musictimes)+"/"+formatTime(seekBar.getMax()));
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
                    if(!mm.isChanged()){
                        mm.play(); //第一首歌初始化需要
                    }else{
                        MusicService.isPlay = true;
                        mm.start();//后面的歌就是暂停之后再开始了
                    }
                    MusicService.isPlay = true;
                    System.out.println("....");
//                    timer();
                }else if(MusicService.isPlay) {
                    ((ImageButton)view).setImageDrawable(getResources().getDrawable(R.drawable.music_suspend));
                    MusicService.isPlay = false;
                    mm.pause();
                    mm.timer_cancel();
//                    timer.cancel();
                }
                break;
            case R.id.next_Song:
                //切下一首
                mm.nextSong();
                break;
            case R.id.pre_Song:
                //切上一首
                mm.preSong();
                break;
            case R.id.music_return:
                mm.close_timer();
                finish();
                break;
            case R.id.music_way:
                Log.d(TAG,"切换播放模式了。");
                if(way!=2){
                    way++;
                }
                else
                    way=0;
                mm.setPlayStatus(way); //告诉service当前播放模式
                if(way==0)
                    playStatus.setImageDrawable(getResources().getDrawable(R.drawable.music_xunhuan)); //单曲循环
                else if(way==1)
                    playStatus.setImageDrawable(getResources().getDrawable(R.drawable.music_shunxu)); //顺序播放
                else
                    playStatus.setImageDrawable(getResources().getDrawable(R.drawable.music_suiji));  //随机播放
            default:
                break;
        }
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