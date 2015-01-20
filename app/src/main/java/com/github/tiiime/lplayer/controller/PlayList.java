package com.github.tiiime.lplayer.controller;

import com.github.tiiime.lplayer.model.MusicInfo;

import java.util.ArrayList;

/**
 * Created by kang on 15/1/19.
 */
public class PlayList {
    ArrayList<MusicInfo> playlist = null;
    ArrayList<MusicInfo> overlist = null;

    public PlayList() {
        playlist = new ArrayList<>();

    }


    public void add(MusicInfo music){
        playlist.add(music);
    }

    /**
     * 移除列表中最后一个
     * @param music
     */
    public void removeOne(MusicInfo music){
        int index = playlist.lastIndexOf(music);
        if (index >= 0){
            playlist.remove(index);
        }
    }

    /**
     * 上一曲
     * @return
     */
    public  MusicInfo getLast(){
        return overlist.get(overlist.size());
    }

    /**
     * 下一曲
     * @return
     */
    public MusicInfo getNext(){
        return playlist.get(0);
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
