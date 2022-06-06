package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.musicplayer.adapter.RecentAdapter;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.bean.SongList;
import com.example.musicplayer.common.MusicUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.databinding.ActivitySongListBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//歌单详情界面的activity，包含上面的折叠的toolbar和下面的歌曲列表
public class SongListActivity extends AppCompatActivity {

    //下面的歌单展示有关的东西
    RecyclerView recyclerView;
    //todo 我的最爱的音乐list，应该是要从数据库读进来
//    private List<MusicInfoModel> list;
    //页面recyclerview的适配器，目前是重用了recent的那个布局
    private RecentAdapter recentAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;
    private Button button;

    private SongList songList;

    //歌单的title、封面
    private String title;
    private int cover;

    //下拉界面状态相关的内容
    private ActivitySongListBinding binding;
    private CollapsingToolbarLayoutState state;
    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySongListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // todo 打开歌单时，应该显示这个歌单里面的歌曲，同时获取到title、封面、list
        //获取数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //采取分开传的方式
//        title = (String) bundle.getSerializable("songListName");
//        cover = (Integer)bundle.getSerializable("cover");
//        //让没有封面的传-1过来
//        if(cover == -1){
//            cover = R.drawable.album;
//        }
//        list = new ArrayList<>();
//        list = (List<MusicInfoModel>)bundle.getSerializable("musicList");
//        Log.e("已经跳转过来了",title);
//        for(MusicInfoModel musicInfoModel : list){
//            Log.e("歌曲",musicInfoModel.getMusicName()+" " + musicInfoModel.getBitmap());
//        }

        //采取传对象的方式
        songList = ((SongList)bundle.getSerializable("SongList"));
        title = songList.getSongListName();
        if(title.equals("我的最爱")){//我的最爱歌单特有的封面
            cover = R.drawable.avatar10;
        }else if(songList.getCover() == null){
            cover = R.drawable.songlist;
        }
        Log.e("我接了歌单来了",songList.getSongListName()+" " + songList.getMusicList());


        //工具栏
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        //返回到主页面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo 返回到主界面的时候，需要往util里面改一下
                MusicUtil.findSongListBySongListName(title).setMusicList(songList.getMusicList());
//                finish();

                Intent myIntent = new Intent(SongListActivity.this, MainActivity.class);
                //启动新的intent
                startActivity(myIntent);
            }
        });
        //折叠显示的区域
        CollapsingToolbarLayout collapsingToolbarLayout = binding.toolbarLayout;
        collapsingToolbarLayout.setTitle("");//设置title不显示
        //整个上面部分
        AppBarLayout appBarLayout = binding.appBar;
        //todo 封面
        ImageView imageView = binding.imageView;
        Glide.with(this).load(cover).into(imageView);

        //todo 歌单名字
        TextView textView = binding.textView;
        textView.setText(title);

        //动态修改title
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //展开状态
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        toolbar.setTitle("歌单");
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        toolbar.setTitle(textView.getText());
                    }
                } else {//变化的过程中
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                        toolbar.setTitle(textView.getText());

                    }
                }
            }
        });

        //添加歌曲button按钮，这个按钮只会在歌单中没有歌曲的时候出现
        button = binding.addButton;
        if(songList.getMusicList()!= null && songList.getMusicList().size() >0){
            //加载下面的歌单
            binding.favoritePlay.setVisibility(View.VISIBLE);
            initMusic();

        }else{
            button.setVisibility(View.VISIBLE);
            binding.favoritePlay.setVisibility(View.INVISIBLE);
        }
        //todo 点击按钮，打开歌曲预览界面，选择要添加的歌曲，添加到歌单里面
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 创建intent，打开二级页面
                Intent myIntent = new Intent(SongListActivity.this,MusicListActivity.class);
                //把歌单对象传给他
                //传递数据
                Bundle bundle = new Bundle();
                bundle.putSerializable("SongList",(Serializable) songList);//把这个歌单对象传过去
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                Log.e("点击按钮","点击添加歌曲按钮弹出新的界面选择要添加的歌曲");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    //todo 传入这个歌单里面的歌曲,初始化加载列表的view
    private void initMusic(){
        //获得recyclerList和侧边栏
        recyclerView = binding.loveSongList;
        //管理视图渲染，线性的
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //避免list置顶
        recyclerView.setFocusable(false);
        //初始化Adapter
        initAdapter();
    }
    //初始化加载下面列表的adapter
    private void initAdapter(){
        //todo 拿到我的最爱的歌曲列表
//        list = MusicUtil.getMusicList();

        Collections.sort(songList.getMusicList(), new Comparator<MusicInfoModel>() {
            @Override
            public int compare(MusicInfoModel o1, MusicInfoModel o2) {
                return o1.getSortSongId().compareToIgnoreCase(o2.getSortSongId());
            }
        });
        Log.e("我加载了新的吗",songList.getSongListName()+songList.getMusicList().size());
        recentAdapter = new RecentAdapter(this, songList.getMusicList());
        recyclerView.setAdapter(recentAdapter);
    }

    //三点更多
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // todo展开操作
        switch (item.getItemId()){
            case R.id.add_settings:
                Log.e("添加歌曲","111");
                return true;
            case R.id.sort_settings:
                Log.e("选择排序方式","111");
                return true;
            case R.id.download_settings:
                Log.e("全部下载","111");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}