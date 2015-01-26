package com.github.tiiime.lplayer.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.github.tiiime.lplayer.activity.ListActivity;
import com.github.tiiime.lplayer.adapter.MusicInfoAdapter;
import com.github.tiiime.lplayer.adapter.PlaylistAdapter;
import com.github.tiiime.lplayer.controller.PlayListController;
import com.github.tiiime.lplayer.dialog.ChoosePlaylistDialog;
import com.github.tiiime.lplayer.dialog.CreatePlaylistDialog;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.model.PlayList;
import com.github.tiiime.lplayer.service.LPlayerService;
import com.github.tiiime.lplayer.tool.MediaController;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

import java.util.ArrayList;


public class PlaylistFragment extends Fragment {
    private static final String TAG = "playlistfragment";

    private ListView listview = null;
    private MusicDBHelper dbHelper = null;
    private ArrayList<PlayList> arr = null;
    private PlaylistAdapter adapter = null;

    private Context mContext = null;

    public PlaylistFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mContext = getActivity();
        dbHelper = new MusicDBHelper(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        listview = (ListView) view.findViewById(R.id.list);

        arr = dbHelper.getPlayLists();
        adapter = new PlaylistAdapter(mContext, arr);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, ListActivity.class);
                intent.putExtra("ListName", adapter.getItem(i).getListName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//      super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_lol, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                CreatePlaylistDialog dialog = new CreatePlaylistDialog(mContext);
                dialog.setOnSubmit(new CreatePlaylistDialog.OnSubmit() {
                    @Override
                    public void onSubmit() {
                        arr = dbHelper.getPlayLists();
                        for (PlayList p : arr) {
                            Log.v(TAG, p.toString());
                        }
                        adapter.refresh(arr);
                    }
                });
                dialog.show();
                break;
            case R.id.action_edit:
                break;
            case R.id.action_del:
                break;
            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
