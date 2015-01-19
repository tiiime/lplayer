package com.github.tiiime.lplayer.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.adapter.PlayListAdapter;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.service.LPlayerService;

import java.util.ArrayList;

import static com.github.tiiime.lplayer.tool.MediaController.*;

/**
 * Created by kang on 15/1/19-下午10:08-下午10:09.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity_Debug";

    private Context mContext = null;

    private Toolbar toolbar = null;
    private ListView playlist = null;
    private Button play = null;//1
    private Button stop = null;//0
    private Button pause = null;//-1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        playlist = (ListView)findViewById(R.id.playlist);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        pause = (Button) findViewById(R.id.pause);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);

        pause.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);

        toolbar.setTitle("hello");
        setSupportActionBar(toolbar);

        ContentResolver cr = getContentResolver();
        ArrayList<MusicInfo> music = new ArrayList<>();

        if (cr != null){
            Cursor cursor = cr.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,null,null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    music.add(new MusicInfo(cursor));
                } while (cursor.moveToNext());
            }
        }

        for (MusicInfo m:music){
            Log.v(TAG, m.toString());
        }

        playlist.setAdapter(new PlayListAdapter(this,music));
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MusicInfo music = (MusicInfo) adapterView.getAdapter().getItem(i);
                Toast.makeText(mContext ,music.getSong() ,Toast.LENGTH_SHORT)
                        .show();


                Intent intent = new Intent(mContext, LPlayerService.class);
                intent.putExtra(INTENT_TYPE,
                        INTENT_MUSICINFO);
                intent.putExtra(INTENT_MUSICINFO, music);
                mContext.startService(intent);

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()){

            case R.id.play:
                Log.v(TAG, "get play");
                intent = new Intent(mContext, LPlayerService.class);
                intent.putExtra(INTENT_TYPE, INTENT_OPERATE);
                intent.putExtra(INTENT_OPERATE, OPERATE_PLAY);
                break;
            case R.id.stop:
                Log.v(TAG, "get stop");
                intent = new Intent(mContext, LPlayerService.class);
                intent.putExtra(INTENT_TYPE, INTENT_OPERATE);
                intent.putExtra(INTENT_OPERATE, OPERATE_STOP);
                break;
            case R.id.pause:
                Log.v(TAG, "get pause");
                intent = new Intent(mContext, LPlayerService.class);
                intent.putExtra(INTENT_TYPE, INTENT_OPERATE);
                intent.putExtra(INTENT_OPERATE, OPERATE_PAUSE);
                break;
        }
        mContext.startService(intent);
    }
}
