package com.homesoftwaretools.portmone.domain;/*
 * Created by Wild on 04.05.2015.
 */

import android.content.ContentValues;
import android.database.Cursor;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class IncomeType {

    Long id;
    String name;
    String webId;

    public IncomeType() {    }

    public IncomeType(String name) {
        this(null, name);
    }

    public IncomeType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public IncomeType(Cursor c) {
        id = c.getLong(c.getColumnIndex(PortmoneContract.IncomeTypes._ID));
        name = c.getString(c.getColumnIndex(PortmoneContract.IncomeTypes.NAME));
        webId = c.getString(c.getColumnIndex(PortmoneContract.IncomeTypes.WEB_ID));
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
        values.put(PortmoneContract.IncomeTypes.NAME, name);
        values.put(PortmoneContract.IncomeTypes.WEB_ID, webId);
        return values;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
