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

    private Button submit = null;
    private Button cancel = null;
    private EditText editText = null;

    private OnSubmit onSubmit = null;

    Context context;

    public CreatePlaylistDialog(Context context) {
        super(context, R.style.dialog_create_playlist);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_create_playlist);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        editText = (EditText) findViewById(R.id.input);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicDBHelper dbHelper = new MusicDBHelper(context);
                dbHelper.addPlayList(editText.getText().toString());
                if (onSubmit != null){
                    onSubmit.onSubmit();
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
        void onSubmit();
    }
}
