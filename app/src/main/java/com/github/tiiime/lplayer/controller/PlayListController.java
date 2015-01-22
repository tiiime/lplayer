package com.github.tiiime.lplayer.controller;

import com.github.tiiime.lplayer.model.MusicInfo;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *控制播放顺序
 * Created by kang on 15/1/19.
 */
public class PlayListController {
    //当前使用的播放列表
    private static ArrayList<MusicInfo> mPlaylist = null;
    private static LinkedList<Integer> tempQuene = new LinkedList<>();

    private static int position = 0;


    private PlayListController(){
    }

    public static void add(MusicInfo music){
        mPlaylist.add(music);
    }



    /**
     * 上一曲
     * @return
     */
    public static MusicInfo getLast(){
        int get = 0;
        //清空 or not清空 临时队列
        tempQuene.clear();

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
        Integer get = 0;
        //判断temp列表中是否有在排队数据
        if ( (get = tempQuene.poll() ) == null ){
            get = (position + 1) % mPlaylist.size();
        }
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

    public static void setPosition(int position) {
        PlayListController.position = position;
    }

    public static ArrayList<MusicInfo> getmPlaylist() {
        return mPlaylist;
    }

    public static void offerQuene(int position){
        tempQuene.offer(position);
    }


}
