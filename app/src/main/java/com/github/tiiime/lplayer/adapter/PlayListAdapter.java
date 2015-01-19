package com.github.tiiime.lplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.model.MusicInfo;

import java.util.List;

/**
 * Created by kang on 15/1/19.
 */
public class PlayListAdapter extends ArrayAdapter<MusicInfo>{
    LayoutInflater inflater = null;

    public PlayListAdapter(Context context, List<MusicInfo> objects) {
        super(context, 0, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.playlist_item, null);
            holder = new ViewHolder();
            holder.song = (TextView)convertView.findViewById(R.id.song);
            holder.album = (TextView)convertView.findViewById(R.id.album);
            holder.artist = (TextView)convertView.findViewById(R.id.artist);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MusicInfo music = getItem(position);

        holder.song.setText(music.getSong());
        holder.album.setText(music.getAlbum());
        holder.artist.setText(music.getArtist());

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public MusicInfo getItem(int position) {
        return super.getItem(position);
    }

    static class ViewHolder {
        TextView song;
        TextView album;
        TextView artist;
    }
}
