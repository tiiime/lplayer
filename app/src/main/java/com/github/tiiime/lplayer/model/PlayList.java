package com.github.tiiime.lplayer.model;

import java.util.ArrayList;

/**
 * Created by kang on 15/1/19.
 */
public class PlayList {
    ArrayList<MusicInfo> playlist = null;



    public PlayList(ArrayList<MusicInfo> playlist) {
        this.playlist = playlist;
    }



    /**
     * 获取列表中position item
     * @param position
     * @return
     */
    public MusicInfo getItem(int position){
        return playlist.get(position);
    }

    /**
     * getCount
     * @return
     */
    public int getCount(){
        if (playlist == null){
            return 0;
        }
        return playlist.size();
    }


}
