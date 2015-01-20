package com.github.tiiime.lplayer.tool;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;

import com.github.tiiime.lplayer.model.MusicInfo;

import java.util.ArrayList;

/**
 * Created by kang on 15/1/20-下午5:01.
 */
public class MusicDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MusicDBHelper";

    public static final String DB_NAME   = "music";
    public static final String ALL_LIST  = "all_list";
    public static final String ALL_MUSIC = "all_music";
    private static int version = 1;

    private Context mContext = null;

    public MusicDBHelper(Context context) {
        super(context, DB_NAME, null, version);
        mContext = context;
    }

    /**
     * String song;
     * String artist;
     * String album;
     * String uri;
     * long time;
     * long size;
     */

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_music_table =
                " create table " + ALL_MUSIC +
                        "(song   TEXT   ," +
                        " artist TEXT   ," +
                        " album  TEXT   ," +
                        " uri    TEXT   ," +
                        " time   TNTEGER," +
                        " size   INTEGER)";

        String create_list_table = "create table " + ALL_LIST +
                "( name TEXT)";

        sqLiteDatabase.execSQL(create_list_table);
        sqLiteDatabase.execSQL(create_music_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        String dropMusic = "drop table " + ALL_MUSIC;
        String dropList  = "drop table " + ALL_LIST;
        sqLiteDatabase.execSQL(dropMusic);
        sqLiteDatabase.execSQL(dropList);
        onCreate(sqLiteDatabase);
    }

    public void findMusic(){
        //获取音乐数据
        ContentResolver cr = mContext.getContentResolver();
        ArrayList<MusicInfo> music = new ArrayList<>();

        if (cr != null) {
            Cursor cursor = cr.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MusicInfo m = new MusicInfo(cursor);
                    addMusic(m);
//                    music.add(new MusicInfo(cursor));
                    Log.v(TAG, "---------updating" + m);
                } while (cursor.moveToNext());
            }
        }
    }
    /**
     * add a music to db
     *
     * @param music
     *
     */
    public void addMusic(MusicInfo music) {
        String sql = "insert into " + ALL_MUSIC + " values('" +
                music.getSong() + "','" +
                music.getArtist() + "','" +
                music.getAlbum() + "','" +
                music.getUri() + "'," +
                music.getTime() + "," +
                music.getSize() + ")";
        getWritableDatabase().execSQL(sql);
    }

    public ArrayList<MusicInfo> getList(String tableName){
        ArrayList<MusicInfo> arr = new ArrayList<>();
        String sql = "select * from " + tableName;
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()){
            MusicInfo music = new MusicInfo();

            music.setSong(cursor.getString(0));
            music.setArtist(cursor.getString(1));
            music.setAlbum(cursor.getString(2));
            music.setUri(cursor.getString(3));
            music.setTime(cursor.getInt(4));
            music.setSize(cursor.getInt(5));

            arr.add(music);
        }
        return arr;
    }

    public ArrayList<String> getPlayLists(){
        ArrayList<String> arr = null;
        String sql = "select * from " + ALL_LIST;
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            arr.add(name);
        }
        return arr;
    }

    public void addPlayList(String name){
        String sql = "insert into " +
                ALL_LIST +" values("+name+")";
        getWritableDatabase().execSQL(sql);
    }
}
