package com.github.tiiime.lplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import com.github.tiiime.lplayer.controller.PlayListController;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.model.PlayList;
import com.github.tiiime.lplayer.service.LPlayerService;
import com.github.tiiime.lplayer.tool.MediaController;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

import java.util.ArrayList;


/**
 * Created by kang on 15/1/19-下午10:08-下午10:09.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity_Debug";

    private Context mContext = null;
    private MusicDBHelper dbHelper = null;
    private ArrayList<MusicInfo> all_music;

    private Toolbar toolbar = null;
    private SeekBar seekBar = null;
    private ListView playlist = null;
    private TextView time = null;
    private Button play = null;//1
    private Button stop = null;//0
    private Button pause = null;//-1
    private Button next = null;//2
    private Button last = null;//3


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        init();

        pause.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);

        //play list init
        playlist.setAdapter(new PlayListAdapter(this, PlayListController.getmPlaylist()));
        //设置列表点击事件监听器
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                mContext.startService(intent);
                //设置seekbar时间
                seekBar.setMax(((int) music.getTime()) / 1000);
                mHandler.post(mRunnable);
            }
        });
        //设置列表长按事件监听器
        playlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(mContext, "已加入播放队列", Toast.LENGTH_SHORT).show();
                PlayListController.offerQuene(i);
                return true;
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
                int min = i / 60;
                int sec = i % 60;
                time.setText(min + ":" + (sec < 10 ? "0" + sec : sec));
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

    private void init() {
        dbHelper = new MusicDBHelper(mContext);
        dbHelper.findMusic();

        all_music = dbHelper.getList(MusicDBHelper.ALL_MUSIC);
        PlayListController.setPlaylist(all_music);

        //初始化控件
        playlist = (ListView) findViewById(R.id.playlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        pause = (Button) findViewById(R.id.pause);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        last = (Button) findViewById(R.id.last);
        next = (Button) findViewById(R.id.next);
        time = (TextView) findViewById(R.id.time);

        toolbar.setTitle("hello");
        setSupportActionBar(toolbar);
    }

    private Handler mHandler = new Handler();
    //处理进度条
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            MediaPlayer mediaPlayer = MediaController.getMediaPlayer();
            if (mediaPlayer != null) {
                int mCurrent = MediaController.getMediaPlayer()
                        .getCurrentPosition();
                seekBar.setProgress(mCurrent / 1000);

            }
            mHandler.postDelayed(this, 1000);

        }
    };

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

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, LPlayerService.class);
        intent.putExtra(MediaController.INTENT_TYPE, MediaController.INTENT_OPERATE);

        switch (view.getId()) {

            case R.id.play:
                Log.v(TAG, "get play");
                intent.putExtra(MediaController.INTENT_OPERATE, MediaController.OPERATE_PLAY);
                mHandler.post(mRunnable);
                break;
            case R.id.stop:
                Log.v(TAG, "get stop");
                intent.putExtra(MediaController.INTENT_OPERATE, MediaController.OPERATE_STOP);

                //重置状态
                reset();
                break;
            case R.id.pause:
                Log.v(TAG, "get pause");

                intent.putExtra(MediaController.INTENT_OPERATE, MediaController.OPERATE_PAUSE);
                mHandler.removeCallbacks(mRunnable);
                break;
            case R.id.last:
                intent.putExtra(MediaController.INTENT_OPERATE, MediaController.OPERATE_LAST);
                break;
            case R.id.next:
                intent.putExtra(MediaController.INTENT_OPERATE, MediaController.OPERATE_NEXT);
                break;
        }
        mContext.startService(intent);
    }


    /**
     * 重置播放状态
     */
    private void reset() {
        mHandler.removeCallbacks(mRunnable);
        seekBar.setProgress(0);
    }


}
