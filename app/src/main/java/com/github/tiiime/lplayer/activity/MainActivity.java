package com.github.tiiime.lplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.controller.PlayListController;
import com.github.tiiime.lplayer.fragment.ControlFragment;
import com.github.tiiime.lplayer.fragment.PlaylistFragment;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.service.LPlayerService;
import com.github.tiiime.lplayer.tool.MediaController;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

import java.util.ArrayList;


/**
 * Created by kang on 15/1/19-下午10:08-下午10:09.
 */

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity_Debug";

    private Context mContext = null;
    private ArrayList<MusicInfo> all_music;
    private PlaylistFragment playlistFragment = null;
    private ControlFragment controlFragment = null;

    private Toolbar toolbar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "activity onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        init();
    }

    private void init() {


        //初始化控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar.setTitle("hello");
        setSupportActionBar(toolbar);

        playlistFragment = (PlaylistFragment) getFragmentManager().
                findFragmentById(R.id.playlist_fragment);
        controlFragment = (ControlFragment) getFragmentManager().
                findFragmentById(R.id.control);

        playlistFragment.setOnClick(new PlaylistFragment.OnItemClick() {
            @Override
            public void onItemClick() {
                controlFragment.startPlay();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.v(TAG, "option menu on create");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_playlist) {
            Intent intent = new Intent(this, LOLActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }







}
