package com.github.tiiime.lplayer.controller;

import com.github.tiiime.lplayer.model.MusicInfo;

import java.util.ArrayList;

/**
 *
 * Created by kang on 15/1/19.
 */
public class PlayListController {
    //当前使用的播放列表
    private static ArrayList<MusicInfo> mPlaylist = null;
    private static ArrayList<MusicInfo> tempQuene = new ArrayList<>();

    private static int position = 0;


    private PlayListController(){
    }

    public static void add(MusicInfo music){
        mPlaylist.add(music);
    }

    /**
     * 移除列表中最后一个
     * @param music
     */
    public static void removeOne(MusicInfo music){
        int index = mPlaylist.lastIndexOf(music);
        if (index >= 0){
            mPlaylist.remove(index);
        }
    }

    /**
     * 上一曲
     * @return
     */
    public static MusicInfo getLast(){
        int get = 0;

        if (position <= 0){
            get = mPlaylist.size() - 1;
        } else {
            get = position - 1;
        }
        position = get;
        return mPlaylist.get(position);
    }


    public static MusicInfo getNow(){
        return mPlaylist.get(position);
    }

    /**
     * 下一曲
     * @return music
     */
    public static MusicInfo getNext(){
        int get = (position + 1) % mPlaylist.size();
        position = get;
        return mPlaylist.get(position);
    }


    /**
     * 获取列表中position item
     * @param position
     * @return
     */
    public static MusicInfo getItem(int position){
        return mPlaylist.get(position);
    }

    /**
     * getCount
     * @return
     */
    public static int getCount(){
        if (mPlaylist == null){
            return 0;
        }
        return mPlaylist.size();
    }

    /**
     * 返回第一个music
     * @return
     */
    public static MusicInfo getFirst(){
        if ( (mPlaylist != null ) && (mPlaylist.size() != 0) ) {
            return mPlaylist.get(0);
        }else {
            return null;
        }
    }

    public static void playCompleate(){
    }

    public static void setPlaylist(ArrayList<MusicInfo> list) {
        mPlaylist = (ArrayList<MusicInfo>) list.clone();
    }

    public static ArrayList<MusicInfo> getmPlaylist() {
        return mPlaylist;
    }
}
