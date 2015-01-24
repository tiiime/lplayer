package com.github.tiiime.lplayer.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.adapter.MusicInfoAdapter;
import com.github.tiiime.lplayer.controller.PlayListController;
import com.github.tiiime.lplayer.dialog.ChoosePlaylistDialog;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.service.LPlayerService;
import com.github.tiiime.lplayer.tool.MediaController;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

import java.util.ArrayList;


public class PlaylistFragment extends Fragment {
    private Context mContext = null;
    private ListView listView = null;
    private OnItemClick onClick = null;
    private MusicDBHelper dbHelper = null;
    private MusicInfoAdapter adapter = null;

    //添加到播放列表中的音乐
    private ArrayList<MusicInfo> toList = null;

    public interface OnItemClick {
        void onItemClick();
    }


    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mContext = getActivity();
        dbHelper = new MusicDBHelper(mContext);
        PlayListController.setPlaylist(dbHelper.getList(MusicDBHelper.ALL_MUSIC));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_playlist, null, false);
        listView = (ListView) view.findViewById(R.id.playlist);
        //play list init
        adapter = new MusicInfoAdapter(mContext, PlayListController.getPlaylist());
        listView.setAdapter(adapter);
        //设置列表点击事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.isShowCheckBox()) {
                    if (toList == null) {
                        toList = new ArrayList<MusicInfo>();
                    }

                    CheckBox c = (CheckBox) view.findViewById(R.id.checkbox);
                    if (c.isChecked()) {
                        toList.remove(adapter.getItem(i));
                        c.setChecked(false);
                    } else {
                        toList.add(adapter.getItem(i));
                        c.setChecked(true);
                    }
                } else {

                    MusicInfo music = (MusicInfo) adapterView.getAdapter().getItem(i);
                    Toast.makeText(mContext, music.getSong(), Toast.LENGTH_SHORT)
                            .show();

                    //设置playlist位置
                    PlayListController.setPosition(i);

                    Intent intent = new Intent(mContext, LPlayerService.class);
                    intent.putExtra(MediaController.INTENT_TYPE,
                            MediaController.INTENT_OPERATE);
                    intent.putExtra(MediaController.INTENT_OPERATE,
                            MediaController.OPERATE_PLAY);
                    getActivity().startService(intent);
                    //设置seekbar时间
                    if (onClick != null) {
                        onClick.onItemClick();
                    }
                }
            }
        });
        //设置列表长按事件监听器
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(mContext, "已加入播放队列", Toast.LENGTH_SHORT).show();
                PlayListController.offerQuene(i);
                return true;
            }
        });

        return view;
    }


    public void setOnClick(OnItemClick onClick) {
        this.onClick = onClick;
    }

    public MusicInfoAdapter getAdapter() {
        return adapter;
    }

    MenuItem hiddenItem = null;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem itemAdd = menu.add(0, 2, 2, "add");
        MenuItem submit = menu.add(0, 3, 3, "submit");

        submit.setVisible(false);
        hiddenItem = submit;

        itemAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        submit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        MenuItem item = menu.getItem(1);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        item = menu.getItem(2);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == 2) {//进入选择模式
            getAdapter().showCheckBox();
            item.setVisible(false);
            hiddenItem.setVisible(true);
            hiddenItem = item;
            return true;
        } else if (id == 3) {//添加到播放列表
            getAdapter().hideCheckBox();
            item.setVisible(false);
            hiddenItem.setVisible(true);
            hiddenItem = item;

            //判断有没有选择歌曲
            if ( (toList == null) || (toList.size() <= 0) ) {
                return true;
            }
            ChoosePlaylistDialog dialog = new ChoosePlaylistDialog(mContext);
            dialog.setOnSubmit(new ChoosePlaylistDialog.OnSubmit() {
                @Override
                public void onSubmit(String name) {
                    addToDb(name);
                }
            });
            dialog.show();
            return true;
        } else {
            return false;
        }

    }

    private void addToDb(String name){
        if (toList == null) return;
        dbHelper.addMuisc(toList, name);
    }
}
