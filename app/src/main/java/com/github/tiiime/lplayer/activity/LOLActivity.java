package com.github.tiiime.lplayer.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.adapter.LOLAdapter;
import com.github.tiiime.lplayer.tool.CreatePlaylistDialog;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

/**
 * List of List Activity
 */

public class LOLActivity extends BaseActivity {
    private Toolbar toolbar = null;
    private ListView listview = null;
    private MusicDBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lol);
        dbHelper = new MusicDBHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listview = (ListView) findViewById(R.id.lol);

        toolbar.setTitle("hello");
        setSupportActionBar(toolbar);

        listview.setAdapter(new LOLAdapter(this, dbHelper.getPlayLists()));

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
