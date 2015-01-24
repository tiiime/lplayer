package com.github.tiiime.lplayer.fragment;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.controller.PlayListController;
import com.github.tiiime.lplayer.service.LPlayerService;
import com.github.tiiime.lplayer.tool.MediaController;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ControlFragment";

    private SeekBar seekBar = null;
    private TextView time = null;
    private Button play = null;//1
    private Button stop = null;//0
    private Button pause = null;//-1
    private Button next = null;//2
    private Button last = null;//3
    private Button find = null;

    private Context mContext = null;

    public static ControlFragment newInstance(Context context){
        ControlFragment fragment = new ControlFragment();
        return fragment;
    }


    public ControlFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_control, container, false);
        initView(view);
        return view;
    }

    void initView(View view){

        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        pause = (Button) view.findViewById(R.id.pause);
        play = (Button) view.findViewById(R.id.play);
        stop = (Button) view.findViewById(R.id.stop);
        last = (Button) view.findViewById(R.id.last);
        next = (Button) view.findViewById(R.id.next);
        time = (TextView) view.findViewById(R.id.time);
        find = (Button) view.findViewById(R.id.find);

        pause.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);
        find.setOnClickListener(this);

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

        reset();
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
            case R.id.find:
                MusicDBHelper dbHelper = new MusicDBHelper(mContext);
                dbHelper.findMusic();
                PlayListController.setPlaylist(dbHelper.getList(MusicDBHelper.ALL_MUSIC));
                break;
        }
        mContext.startService(intent);
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

    /**
     * 重置播放状态
     */
    private void reset() {
        mHandler.removeCallbacks(mRunnable);
        seekBar.setProgress(0);
    }

    public void startPlay(){
        mHandler.postDelayed(mRunnable,1000);
    }
}
