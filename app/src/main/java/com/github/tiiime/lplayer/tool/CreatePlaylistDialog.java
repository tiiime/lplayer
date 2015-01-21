package com.github.tiiime.lplayer.tool;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.tiiime.lplayer.R;

/**
 * Created by kang on 15/1/21-上午11:40.
 */
public class CreatePlaylistDialog extends Dialog {

    private Button button = null;
    private EditText editText = null;


    Context context;
    public CreatePlaylistDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_create_playlist);
        button = (Button) findViewById(R.id.submit);
        editText = (EditText) findViewById(R.id.input);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicDBHelper dbHelper = new MusicDBHelper(context);
                dbHelper.addPlayList(editText.getText().toString());
            }
        });
    }
}
