package com.github.tiiime.lplayer.tool;

import android.media.MediaPlayer;

/**
 * Created by kang on 15/1/19.
 */
public class MediaController {
    public static final int OPERATE_PLAY = 1;
    public static final int OPERATE_STOP = 0;
    public static final int OPERATE_PAUSE = -1;

    public static final String INTENT_TYPE = "type";
    public static final String INTENT_OPERATE = "operate";
    public static final String INTENT_MUSICINFO = "musicinfo";

    private static MediaPlayer mp = new MediaPlayer();




}
