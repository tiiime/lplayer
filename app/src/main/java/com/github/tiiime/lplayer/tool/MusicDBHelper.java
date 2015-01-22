package com.github.tiiime.lplayer.tool;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;

import com.github.tiiime.lplayer.model.MusicInfo;
import com.github.tiiime.lplayer.model.PlayList;

import java.util.ArrayList;

/**
 * Created by kang on 15/1/20-下午5:01.
 */
public class MusicDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MusicDBHelper";

    public static final String DB_NAME = "music";
    public static final String ALL_LIST = "all_list";
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
                        " size   INTEGER," +
                        " status INTEGET)";

        String create_list_table = "create table " + ALL_LIST +
                "( _id INTEGER PRIMARY KEY , name TEXT)";

        sqLiteDatabase.execSQL(create_list_table);
        sqLiteDatabase.execSQL(create_music_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    private void clear() {
        String dropMusic = "drop table " + ALL_MUSIC;
        String dropList = "drop table " + ALL_LIST;
        getWritableDatabase().execSQL(dropMusic);
        getWritableDatabase().execSQL(dropList);
        onCreate(getWritableDatabase());
    }

    public void findMusic() {
        clear();

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
     */
    public void addMusic(MusicInfo music) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("song", music.getSong());
        contentValues.put("artist", music.getArtist());
        contentValues.put("album", music.getAlbum());
        contentValues.put("uri", music.getUri());
        contentValues.put("time", music.getTime());
        contentValues.put("size", music.getSize());
        contentValues.put("status", music.getStatus());

        getWritableDatabase().insert(ALL_MUSIC, null, contentValues);
    }

    String convert(String str) {
        return str.replace("'", "");
    }

    /**
     * 根据表名获取音乐
     *
     * @param tableName
     * @return
     */
    public ArrayList<MusicInfo> getList(String tableName) {
        ArrayList<MusicInfo> arr = new ArrayList<>();
        String sql = "select * from " + tableName;
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MusicInfo music = new MusicInfo();

            music.setSong(cursor.getString(0));
            music.setArtist(cursor.getString(1));
            music.setAlbum(cursor.getString(2));
            music.setUri(cursor.getString(3));
            music.setTime(cursor.getInt(4));
            music.setSize(cursor.getInt(5));
            music.setStatus(cursor.getInt(6));
            arr.add(music);
        }
        return arr;
    }

    /**
     * 获取所有playlist
     *
     * @return
     */
    public ArrayList<PlayList> getPlayLists() {
        ArrayList<PlayList> arr = new ArrayList<>();
        String sql = "select * from " + ALL_LIST;
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PlayList list = new PlayList();
            list.set_id(cursor.getInt(0));
            list.setListName(cursor.getString(1));
            arr.add(list);
        }

        return arr;
    }

    /**
     * 添加 playlist
     *
     * @param name
     */
    public void addPlayList(String name) {
        String add_playlist_index = "insert into " +
                ALL_LIST + " values( null, '" + name + "')";
        String add_playlist = "create table " + name;

        getWritableDatabase().execSQL(add_playlist);
        getWritableDatabase().execSQL(add_playlist_index);
    }
}
