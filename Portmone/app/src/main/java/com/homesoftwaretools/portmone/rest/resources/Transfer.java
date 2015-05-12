package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class Transfer extends Operation{

    Long from_cash_type_id;
    Long to_cash_type_id;

    public Transfer(ContentValues values) {
        super(values);
        from_cash_type_id = values.getAsLong(PortmoneContract.Transfers.FROM_CASH_TYPE_ID);
        to_cash_type_id = values.getAsLong(PortmoneContract.Transfers.TO_CASH_TYPE_ID);
    }

    public Long getFromCashTypeId() {
        return from_cash_type_id;
    }

    public void setFromCashTypeId(Long id) {
        this.from_cash_type_id = id;
    }

    public Long getToCashTypeId() {
        return to_cash_type_id;
    }

    public void setToCashTypeId(Long id) {
        this.to_cash_type_id = id;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = super.toValues();
        values.put(PortmoneContract.Transfers.FROM_CASH_TYPE_ID, from_cash_type_id);
        values.put(PortmoneContract.Transfers.TO_CASH_TYPE_ID, to_cash_type_id);
        return values;
    }

}
