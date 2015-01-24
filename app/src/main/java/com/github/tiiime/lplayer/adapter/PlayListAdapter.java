package com.github.tiiime.lplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.model.PlayList;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表页面适配器
 * Created by kang on 15/1/21-上午10:51.
 */
public class PlaylistAdapter extends ArrayAdapter<PlayList> {
    private static final String TAG = "loladapter";
    LayoutInflater inflater = null;
    ArrayList<PlayList> playlist = null;

    public PlaylistAdapter(Context context, ArrayList<PlayList> objects) {
        super(context, 0, objects);
        playlist = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lol_item, null);
            viewHolder = new ViewHolder();
            viewHolder.id = (TextView) convertView.findViewById(R.id.listid);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PlayList list = getItem(position);

        viewHolder.id.setText(String.valueOf(list.get_id()));
        viewHolder.name.setText(list.getListName());

        return convertView;
    }

    @Override
    public PlayList getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void refresh(ArrayList<PlayList> list){
        playlist.clear();
        playlist.addAll(list);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView id;
        TextView name;
    }

}
