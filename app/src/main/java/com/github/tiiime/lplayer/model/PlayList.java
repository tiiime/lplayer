package com.github.tiiime.lplayer.model;

import java.io.Serializable;

/**
 * Created by kang on 15/1/21-上午10:07.
 */

public class PlayList implements Serializable {
    String listName;
    int _id;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
