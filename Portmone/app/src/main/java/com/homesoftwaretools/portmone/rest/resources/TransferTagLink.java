package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class TransferTagLink extends TagLink {

    Long transfer_id;

    public TransferTagLink(ContentValues values) {
        super(values);
        transfer_id = values.getAsLong(PortmoneContract.TransferTags.TRANSFER_ID);
    }

    public Long getTransferId() {
        return transfer_id;
    }

    public void setTransferId(Long id) {
        this.transfer_id = id;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = super.toValues();
        values.put(PortmoneContract.TransferTags.TRANSFER_ID, transfer_id);
        return values;
    }

}
