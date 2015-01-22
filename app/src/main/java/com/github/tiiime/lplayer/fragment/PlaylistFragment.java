package com.github.tiiime.lplayer.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.adapter.PlayListAdapter;
import com.github.tiiime.lplayer.controller.PlayListController;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.service.LPlayerService;
import com.github.tiiime.lplayer.tool.MediaController;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext = null;
    private MusicDBHelper dbHelper = null;
    private OnItemClick onClick = null;

    public interface OnItemClick{
        void onItemClick();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = getActivity();
        dbHelper = new MusicDBHelper(mContext);
        PlayListController.setPlaylist(dbHelper.getList(MusicDBHelper.ALL_MUSIC));
    }

    private ListView listView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        listView = (ListView) view.findViewById(R.id.playlist);
        //play list init
        listView.setAdapter(new PlayListAdapter(mContext, PlayListController.getPlaylist()));
        //设置列表点击事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                if (onClick != null){
                    onClick.onItemClick();
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
}
