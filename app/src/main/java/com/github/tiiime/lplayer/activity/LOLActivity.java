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
    private ListView listview = null;
    private MusicDBHelper dbHelper = null;
    private ArrayList<PlayList> arr = null;
    private PlaylistAdapter adapter = null;

    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lol);
        mContext = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listview = (ListView) findViewById(R.id.lol);

        dbHelper = new MusicDBHelper(this);
        arr = dbHelper.getPlayLists();
        adapter = new PlaylistAdapter(this, arr);


        toolbar.setTitle("hello");
        setSupportActionBar(toolbar);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lol, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                CreatePlaylistDialog dialog = new CreatePlaylistDialog(this);
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
