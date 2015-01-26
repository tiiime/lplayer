package com.github.tiiime.lplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.adapter.PlaylistAdapter;
import com.github.tiiime.lplayer.model.PlayList;
import com.github.tiiime.lplayer.dialog.CreatePlaylistDialog;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

import java.util.ArrayList;

/**
 * List of List Activity
 */

public class LOLActivity extends BaseActivity {
    private static final String TAG = "LOLActivity";
    private Toolbar toolbar = null;


    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lol);
        mContext = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);



        toolbar.setTitle("hello");
        setSupportActionBar(toolbar);



    }




}
