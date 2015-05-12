package com.homesoftwaretools.portmone.rest.resources;
/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class Lib implements Resource {

    String id;
    String name;
    String token;

    public Lib() {}

    public Lib(ContentValues values) {
        name = values.getAsString(PortmoneContract.BaseLib.NAME);
        token = values.getAsString(PortmoneContract.BaseLib.TOKEN);
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.BaseLib.NAME, name);
        values.put(PortmoneContract.BaseLib.WEB_ID, id);
        values.put(PortmoneContract.BaseLib.TOKEN, token);
        return values;
    }
}
