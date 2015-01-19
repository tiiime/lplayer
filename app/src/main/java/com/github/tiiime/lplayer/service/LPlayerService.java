package com.github.tiiime.lplayer.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.tool.MediaController;

import java.io.IOException;

import static com.github.tiiime.lplayer.tool.MediaController.*;

/**
 *
 */
public class LPlayerService extends IntentService {
    private static final String TAG = "LPlayerService";

    private static MediaPlayer mediaPlayer = null;

    private static Uri nowPlaying = null;
    /**
     * 0---->stop
     * 1---->playing
     * -1--->pause
     */
    private static int playStat = 0;

    public LPlayerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle b = intent.getExtras();
        String type = b.getString(INTENT_TYPE);
        Log.v(TAG, "get type"+type);

        switch (type){
            case INTENT_OPERATE:
                mediaControl(b.getInt(INTENT_OPERATE));
                break;
            case INTENT_MUSICINFO:
                MusicInfo m = (MusicInfo)b.get(INTENT_MUSICINFO);
                Log.v(TAG, "rec ----- " + m.getSong());
                nowPlaying = Uri.parse(m.getUri());
                start(nowPlaying);
                break;
        }

    }


    private void mediaControl(int operat){

        switch (operat){
            case OPERATE_STOP://stop
                stop();
                break;
            case OPERATE_PLAY://play
                if (playStat == -1){
                    mediaPlayer.start();
                }
                if (nowPlaying != null){
                    start(nowPlaying);
                }
                break;
            case OPERATE_PAUSE://pause
                if ( (mediaPlayer != null) && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playStat = -1;
                }
                break;
        }
    }
    /**
     * stop music
     */
    private void stop(){
        playStat = 0;

        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            Log.v(TAG, "media release");
        } else {
            Log.v(TAG, "media is null");
        }
    }

    /**
     * start play
     * @param uri
     */
    private void start(Uri uri){
        Log.v(TAG, "current thread id:"+Thread.currentThread().getId());

        stop();

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            Log.v(TAG, "media not null");
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playStat = 1;
        } catch (IOException e) {
            e.printStackTrace();
            playStat = 0;
        }
        Log.v(TAG, "play stat:"+playStat);
    }

}
