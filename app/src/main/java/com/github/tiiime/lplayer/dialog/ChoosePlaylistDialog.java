package com.github.tiiime.lplayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.tiiime.lplayer.R;
import com.github.tiiime.lplayer.adapter.PlaylistAdapter;
import com.github.tiiime.lplayer.model.PlayList;
import com.github.tiiime.lplayer.tool.MusicDBHelper;

import java.util.ArrayList;

/**
 * Created by kang on 15/1/21-上午11:40.
 */
public class ChoosePlaylistDialog extends Dialog {
    private ListView list = null;
    private Context mContext = null;
    private OnSubmit onSubmit = null;
    private PlaylistAdapter adapter = null;

    private Button cancel = null;

    public ChoosePlaylistDialog(Context context) {
        super(context, R.style.dialog_create_playlist);
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_choose_playlist);
        list = (ListView) findViewById(R.id.list);
        cancel = (Button) findViewById(R.id.cancel);

        MusicDBHelper dbHelper = new MusicDBHelper(mContext);
        ArrayList<PlayList> arr = dbHelper.getPlayLists();
        adapter = new PlaylistAdapter(mContext, arr);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapter.getItem(i).getListName();
                if (onSubmit != null) {
                    onSubmit.onSubmit(name);
                }
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setOnSubmit(OnSubmit onSubmit){
        this.onSubmit = onSubmit;
    }

    public interface OnSubmit{
        void onSubmit(String name);
    }
}
