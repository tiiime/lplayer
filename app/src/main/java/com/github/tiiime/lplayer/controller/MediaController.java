package com.github.tiiime.lplayer.controller;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.github.tiiime.lplayer.model.MusicInfo;

import java.io.IOException;

/**
 * Created by kang on 15/1/19.
 */
public class MediaController {
    public  static final String TAG = "Media_Controller";

    public static final int OPERATE_PLAY = 1;
    public static final int OPERATE_STOP = 0;
    public static final int OPERATE_PAUSE = -1;

    public static final int OPERATE_NEXT = 2;
    public static final int OPERATE_LAST = 3;
    public static final int OPERATE_RANDOM = 4;


    public static final String INTENT_TYPE = "type";
    public static final String INTENT_OPERATE = "operate";

    // intent直接接受uri时使用
    public static final String INTENT_MUSICINFO = "musicinfo";

    private static MediaPlayer mediaPlayer = new MediaPlayer();

    private static int playMode = 0;//播放模式
                                    //正常
                                    //列表循环
                                    //随机


    public static int getPlayMode() {
        return playMode;
    }

    public static void setPlayMode(int playMode) {
        MediaController.playMode = playMode;
    }

    /**
     * get mediaplayer
     * @return
     */
    private static MediaPlayer getPlayer(){
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer() ;
        }
        return mediaPlayer;
    }


    /**
     *
     * @param music
     */
    public static void play(Context context,MusicInfo music){

//      if (mediaPlayer == null) {
//          mediaPlayer = getPlayer();
//      } else {
//          Log.v(TAG, "media not null");
//      }
        stop();
        mediaPlayer = getPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(context, music.toUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * stop music
     */
    public static void stop(){

        if (mediaPlayer != null){
            mediaPlayer.reset();
            Log.v(TAG, "media release");
        } else {
            Log.v(TAG, "media is null");
        }
    }


    public static void pause() {
        if ( (mediaPlayer != null) && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * resume pause
     */
    public static void resume(){
        mediaPlayer.start();
    }

    public static void release(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


}
