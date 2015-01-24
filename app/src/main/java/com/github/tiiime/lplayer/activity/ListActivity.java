package com.github.tiiime.lplayer.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.adapter.MusicInfoAdapter;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

import java.util.ArrayList;

public class ListActivity extends ActionBarActivity {
    private ListView list = null;
    private MusicDBHelper dbHelper = null;
    private MusicInfoAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Bundle b = getIntent().getExtras();
        String name = b.getString("ListName");
        dbHelper = new MusicDBHelper(this);
        list = (ListView) findViewById(R.id.list);

        ArrayList<MusicInfo> arr = dbHelper.getList(name);
        adapter = new MusicInfoAdapter(this, arr);

        list.setAdapter(adapter);
        

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

}
