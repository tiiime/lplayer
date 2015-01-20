package com.github.tiiime.lplayer.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.github.tiiime.lplayer.controller.PlayList;
import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.tool.MediaController;

import java.io.IOException;
import java.util.ArrayList;

import static com.github.tiiime.lplayer.tool.MediaController.*;

/**
 *
 */
public class LPlayerService extends Service {
    private static final String TAG = "LPlayerService";


    private PlayList playList = new PlayList();
    private static MediaPlayer mediaPlayer = null;
    private static Uri nowPlaying = null;
    /**
     * 0---->stop
     * 1---->playing
     * -1--->pause
     */
    private static int playStat = 0;

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onstartComm");
        if (intent == null) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        Bundle b = intent.getExtras();
        String type = b.getString(INTENT_TYPE);
        Log.v(TAG, "get type" + type);

        switch (type) {
            case INTENT_OPERATE:
                mediaControl(b.getInt(INTENT_OPERATE));
                break;
            case INTENT_MUSICINFO:
                MusicInfo m = (MusicInfo) b.get(INTENT_MUSICINFO);
                Log.v(TAG, "rec ----- " + m.getSong());
                nowPlaying = Uri.parse(m.getUri());
                MediaController.play(this, nowPlaying);
                break;
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void mediaControl(int operat) {

        switch (operat) {
            case OPERATE_STOP://stop
                MediaController.stop();
                playStat = OPERATE_STOP;
                break;
            case OPERATE_PLAY://play
                if (playStat == OPERATE_PAUSE) {
                    MediaController.resume();
                } else {
                    if (nowPlaying == null) return;
                    MediaController.play(this, nowPlaying);
                }
                playStat = OPERATE_PLAY;
                break;
            case OPERATE_PAUSE://pause
                if (playStat == OPERATE_PLAY){
                    MediaController.pause();
                    playStat = OPERATE_PAUSE;
                }
                break;
        }

    }

    @Override
    public void onDestroy() {
        MediaController.release();
        super.onDestroy();
    }
}