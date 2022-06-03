package com.example.musicplayer.ui.localMusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.FolderAdapter;
import com.example.musicplayer.bean.Folder;
import com.example.musicplayer.bean.MusicInfoModel;
import com.example.musicplayer.common.MusicUtil;
import com.example.musicplayer.widget.LetterSideView;
import com.example.musicplayer.widget.MyItemDecoration;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FolderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    //获取左边控件以及侧边字母索引控件
    RecyclerView recyclerView;
    LetterSideView letterSideView;
    //页面recyclerview的适配器
    private FolderAdapter folderAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;

    private List<Folder> folderList;

    public FolderFragment() {
        // Required empty public constructor
    }

    public static FolderFragment newInstance(int index) {
        FolderFragment fragment = new FolderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_folder, container, false);

        //获得recyclerList和侧边栏
        recyclerView = rootView.findViewById(R.id.folder_list);
        letterSideView = rootView.findViewById(R.id.folderIndexView);
        //管理视图渲染，线性的
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //初始化Adapter
        initAdapter();
        //初始化侧边栏移动的监听器
        initListener();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    private void initAdapter() {
        folderList = new ArrayList<>();
        folderList = MusicUtil.getAllFolderList();
        for(Folder folder: folderList){
            setSortName(folder);
        }

        Collections.sort(folderList, new Comparator<Folder>() {
            @Override
            public int compare(Folder o1, Folder o2) {
                return o1.getSortFolderId().compareToIgnoreCase(o2.getSortFolderId());
            }
        });

        for(Folder folder: folderList){
            Log.e("专辑",folder.getFolderName());
            for(MusicInfoModel musicInfoModel: folder.getMusicList()){
                Log.e("歌曲",musicInfoModel.getMusicName());
            }
        }

        //设置分割线
        recyclerView.addItemDecoration(new MyItemDecoration(getContext(), new MyItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                return folderList.get(position).getSortFolderId();
            }

            @Override
            public String getGroupName(int position) {
                Folder folder = folderList.get(position);
                return folder.getSortFolderId().toUpperCase();
            }
        }));


        folderAdapter = new FolderAdapter(getContext(), folderList);
        recyclerView.setAdapter(folderAdapter);
    }

    private void initListener() {
        letterSideView.setListener(new LetterSideView.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                moveToLetterPosition(letter);
            }
        });
    }

    private void moveToLetterPosition(String letter) {
        //这里主要是为了跳转到最顶端
        if ("#".equals(letter)) {
            letter = "*";
        }
        for (int i = 0; i < folderList.size(); i++) {
            Folder folder = folderList.get(i);
            if (folder.getSortFolderId().toUpperCase().equals(letter)) {
                layoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    private void setSortName(Folder folder){
        String firstChar = folder.getFolderName().charAt(0)+"";
        if(MusicUtil.checkFirstIsEnglish(firstChar) || firstChar.matches("[\\u4E00-\\u9FA5]")) {
            if (MusicUtil.checkFirstIsEnglish(folder.getFolderName())) {
                String folderName = folder.getFolderName();
                folder.setSortFolderId("" + Character.toLowerCase(folderName.charAt(0)));
                folder.setSortFolderName(folderName);
            } else {
                try {
                    String pingYin = PinyinHelper.convertToPinyinString(folder.getFolderName(), " ", PinyinFormat.WITHOUT_TONE);
                    folder.setSortFolderId(pingYin.substring(0, 1));
                    folder.setSortFolderName(pingYin);
                }catch (PinyinException e){
                    e.printStackTrace();
                }
            }
        }else{
            folder.setSortFolderId("#");
            folder.setSortFolderName(folder.getFolderName());
        }
    }
}
