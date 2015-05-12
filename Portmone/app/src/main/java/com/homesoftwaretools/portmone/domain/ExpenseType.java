package com.homesoftwaretools.portmone.domain;/*
 * Created by Wild on 04.05.2015.
 */

import android.content.ContentValues;
import android.database.Cursor;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class ExpenseType {

    Long id;
    String name;
    String webId;

    public ExpenseType() {    }

    public ExpenseType(String name) {
        this(null, name);
    }

    public ExpenseType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ExpenseType(Cursor c) {
        id = c.getLong(c.getColumnIndex(PortmoneContract.ExpenseTypes._ID));
        name = c.getString(c.getColumnIndex(PortmoneContract.ExpenseTypes.NAME));
        webId = c.getString(c.getColumnIndex(PortmoneContract.ExpenseTypes.WEB_ID));
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
        values.put(PortmoneContract.ExpenseTypes.NAME, name);
        values.put(PortmoneContract.ExpenseTypes.WEB_ID, webId);
        return values;
    }


    public void setId(Long id) {
        this.id = id;
    }
}
