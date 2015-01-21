package com.github.tiiime.lplayer.model;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by kang on 15/1/19.
 */
public class MusicInfo implements Serializable {

    String song;
    String artist;
    String album;
    String uri;

    long time;
    long size;

    int status;


    /**
     * test
     * @param song
     * @param artist
     * @param album
     */
    public MusicInfo(String song, String artist, String album) {
        this.song = song;
        this.artist = artist;
        this.album = album;
    }


    public MusicInfo() {
    }

    public MusicInfo(Cursor cursor) {
        String album = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.ALBUM));
        String artist = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.ARTIST));
        long size = cursor.getLong(cursor
                .getColumnIndex(MediaStore.Audio.Media.SIZE));
        long time = cursor.getLong(cursor
                .getColumnIndex(MediaStore.Audio.Media.DURATION));
        String uri = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.DATA));
        String name = cursor
                .getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));


        this.artist = artist;
        this.album = album;
        this.song = name;
        this.time = time;
        this.size = size;
        this.uri = uri;
        this.status = 1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "song:"+song+" artist:"+artist+" album:"+album+" \nuri:"+uri;
    }
}
