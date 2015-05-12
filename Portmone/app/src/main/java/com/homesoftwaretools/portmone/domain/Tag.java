package com.homesoftwaretools.portmone.domain;/*
 * Created by Wild on 04.05.2015.
 */

import android.content.ContentValues;
import android.database.Cursor;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class Tag {

    Long id;
    String name;
    String webId;

    public Tag() {    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Cursor c) {
        id = c.getLong(c.getColumnIndex(PortmoneContract.Tags._ID));
        name = c.getString(c.getColumnIndex(PortmoneContract.Tags.NAME));
        webId = c.getString(c.getColumnIndex(PortmoneContract.Tags.WEB_ID));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.Tags.NAME, name);
        values.put(PortmoneContract.Tags.WEB_ID, webId);
        return values;
    }


    public void setId(Long id) {
        this.id = id;
    }
}
