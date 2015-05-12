package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 12.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class Notes implements Resource {

    String notes;

    public Notes() {}

    public Notes(ContentValues values) {
        notes = values.getAsString(PortmoneContract.Users.NOTES);
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.Users.NOTES, notes);
        return values;
    }
}
