package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class Operation implements Resource {

    String id;
    Long timestamp;
    Float sum;
    String description;
    Integer planned;
    Integer deleted;
    String token;

    public Operation() {}

    public Operation(ContentValues values) {
        timestamp = values.getAsLong(PortmoneContract.Journal.TIMESTAMP);
        sum = values.getAsFloat(PortmoneContract.Journal.SUM);
        description = values.getAsString(PortmoneContract.Journal.DESCRIPTION);
        planned = values.getAsInteger(PortmoneContract.Journal.PLANNED);
        token = values.getAsString(PortmoneContract.Journal.TOKEN);
        deleted = values.getAsInteger(PortmoneContract.Journal.DELETED);
        if (deleted == null) deleted = 0;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.Journal.WEB_ID, id);
        values.put(PortmoneContract.Journal.PLANNED, planned);
        values.put(PortmoneContract.Journal.TIMESTAMP, timestamp);
        values.put(PortmoneContract.Journal.SUM, sum);
        values.put(PortmoneContract.Journal.DELETED, deleted);
        values.put(PortmoneContract.Journal.TOKEN, token);
        return values;
    }
}
