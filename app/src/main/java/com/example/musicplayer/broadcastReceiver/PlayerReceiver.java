package com.example.musicplayer.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.musicplayer.MusicActivity;
import com.example.musicplayer.MusicService;
import com.example.musicplayer.common.MusicUtil;

//广播接收器,通知栏通知music service
public class PlayerReceiver extends BroadcastReceiver {
    public static final String PLAY_PRE = "play_pre";
    public static final String PLAY_NEXT = "play_next";
    public static final String PLAY_PAUSE = "play_pause";
    private String msg;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = new Bundle();
        if (intent.getAction().equals(PLAY_NEXT)) {//PLAY_NEXT
            Log.e("PlayerReceiver", "通知栏点击了下一首");
            msg = "next";
            bundle.putString("action",msg);  //往Bundle中存放数据
            Message msg = Message.obtain();
            msg.setData(bundle); //传给MusicService的
            MusicService.handler2.sendMessage(msg); //告诉service我已经打开了
//            MusicService.next_Song();
        }
        if (intent.getAction().equals(PLAY_PRE)) {
            Log.e("PlayerReceiver", "通知栏点击了上一首");
            msg = "pre";
            bundle.putString("action",msg);  //往Bundle中存放数据
            Message msg = Message.obtain();
            msg.setData(bundle); //传给MusicService的
            MusicService.handler2.sendMessage(msg); //告诉service我已经打开了
        }
        if (intent.getAction().equals(PLAY_PAUSE)) {
            Log.e("PlayerReceiver", "通知栏点击了暂停/播放");
            msg = "play";
            bundle.putString("action",msg);  //往Bundle中存放数据
            Message msg = Message.obtain();
            msg.setData(bundle); //传给MusicService的
            MusicService.handler2.sendMessage(msg); //告诉service我已经打开了
        }
    }
}
