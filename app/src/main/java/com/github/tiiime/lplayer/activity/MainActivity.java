package com.github.tiiime.lplayer.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.adapter.PlayListAdapter;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.service.LPlayerService;
import com.github.tiiime.lplayer.tool.MediaController;

import java.util.ArrayList;


/**
 * Created by kang on 15/1/19-下午10:08-下午10:09.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity_Debug";

    private Context mContext = null;

    private Toolbar toolbar = null;
    private SeekBar seekBar = null;
    private ListView playlist = null;
    private TextView time = null;
    private Button play = null;//1
    private Button stop = null;//0
    private Button pause = null;//-1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        playlist = (ListView) findViewById(R.id.playlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        pause = (Button) findViewById(R.id.pause);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        time = (TextView) findViewById(R.id.time);

        pause.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);

        toolbar.setTitle("hello");
        setSupportActionBar(toolbar);

        //获取音乐数据
        ContentResolver cr = getContentResolver();
        ArrayList<MusicInfo> music = new ArrayList<>();

        if (cr != null) {
            Cursor cursor = cr.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    music.add(new MusicInfo(cursor));
                } while (cursor.moveToNext());
            }
        }

        for (MusicInfo m : music) {
            Log.v(TAG, m.toString());
        }

        //play list init
        playlist.setAdapter(new PlayListAdapter(this, music));
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MusicInfo music = (MusicInfo) adapterView.getAdapter().getItem(i);
                Toast.makeText(mContext, music.getSong(), Toast.LENGTH_SHORT)
                        .show();

                Intent intent = new Intent(mContext, LPlayerService.class);
                intent.putExtra(MediaController.INTENT_TYPE,
                        MediaController.INTENT_MUSICINFO);
                intent.putExtra(MediaController.INTENT_MUSICINFO, music);
                mContext.startService(intent);
                //设置seekbar时间
                seekBar.setMax(((int) music.getTime()) / 1000);
                mHandler.post(mRunnable);
            }
        });

        //set seekar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                MediaPlayer m = MediaController.getMediaPlayer();
                if (m != null && fromUser) {
                    m.seekTo(i * 1000);
                }
                int min = i  / 60;
                int sec = i  % 60;
                time.setText(min+":"+ (sec<10? "0"+sec : sec));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //reset
        reset();
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            MediaPlayer mediaPlayer = MediaController.getMediaPlayer();
            if (mediaPlayer != null) {
                int mCurrent = MediaController.getMediaPlayer().getCurrentPosition();
                seekBar.setProgress(mCurrent / 1000);

            }
            mHandler.postDelayed(this, 1000);

        }
    };

    @Override
    protected void onRestart() {
        Log.v(TAG, "activity onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "activity onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "activity onDestory");
        super.onDestroy();
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

        switch (view.getId()) {

            case R.id.play:
                Log.v(TAG, "get play");
                intent = new Intent(mContext, LPlayerService.class);
                intent.putExtra(MediaController.INTENT_TYPE, MediaController.INTENT_OPERATE);
                intent.putExtra(MediaController.INTENT_OPERATE, MediaController.OPERATE_PLAY);
                mHandler.post(mRunnable);
                break;
            case R.id.stop:
                Log.v(TAG, "get stop");
                intent = new Intent(mContext, LPlayerService.class);
                intent.putExtra(MediaController.INTENT_TYPE, MediaController.INTENT_OPERATE);
                intent.putExtra(MediaController.INTENT_OPERATE, MediaController.OPERATE_STOP);

                //重置状态
                reset();
                break;
            case R.id.pause:
                Log.v(TAG, "get pause");
                intent = new Intent(mContext, LPlayerService.class);
                intent.putExtra(MediaController.INTENT_TYPE, MediaController.INTENT_OPERATE);
                intent.putExtra(MediaController.INTENT_OPERATE, MediaController.OPERATE_PAUSE);
                mHandler.removeCallbacks(mRunnable);
                break;
        }
        mContext.startService(intent);
    }


    /**
     * 充值状态
     */
    private void reset() {
        mHandler.removeCallbacks(mRunnable);
        seekBar.setProgress(0);

    }



}
