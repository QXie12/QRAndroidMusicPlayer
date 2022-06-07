package com.example.musicplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
//会员中心打开的activity
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_library);
    }
}