package com.homesoftwaretools.portmone.domain;/*
 * Created by Wild on 04.05.2015.
 */

import android.content.ContentValues;
import android.database.Cursor;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class CashType {
    Long id;
    String name;
    String webId;

    public CashType() {    }

    public CashType(String name) {
        this(null, name);
    }

    public CashType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CashType(Cursor c) {
        id = c.getLong(c.getColumnIndex(PortmoneContract.CashTypes._ID));
        name = c.getString(c.getColumnIndex(PortmoneContract.CashTypes.NAME));
        webId = c.getString(c.getColumnIndex(PortmoneContract.CashTypes.WEB_ID));
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
        values.put(PortmoneContract.CashTypes.NAME, name);
        values.put(PortmoneContract.CashTypes.WEB_ID, webId);
        return values;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
