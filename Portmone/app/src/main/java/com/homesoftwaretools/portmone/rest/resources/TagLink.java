package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class TagLink implements Resource {

    String id;
    Long tag_id;
    String token;

    public TagLink() {}

    public TagLink(ContentValues values) {
        tag_id = values.getAsLong(PortmoneContract.TagLink.TAG_ID);
        token = values.getAsString(PortmoneContract.Journal.TOKEN);
    }

    public Long getTagId() {
        return tag_id;
    }

    public void setTagId(Long id) {
        this.tag_id = id;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.TagLink.WEB_ID, id);
        values.put(PortmoneContract.TagLink.TAG_ID, tag_id);
        values.put(PortmoneContract.TagLink.TOKEN, token);
        return values;
    }
}
