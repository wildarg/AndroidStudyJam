package com.homesoftwaretools.portmone.domain;/*
 * Created by Wild on 04.05.2015.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

import java.util.Date;

public class Transfer {

    Long id;
    Date date;
    boolean planned;
    CashType fromCashType;
    CashType toCashType;
    float sum;
    String description;
    String webId;

    public Transfer() {
        this.date = new Date();
        this.fromCashType = new CashType();
        this.toCashType = new CashType();
    }

    public Transfer(Cursor c) {
        id = c.getLong(c.getColumnIndex(PortmoneContract.TransferJournal._ID));
        date = new Date(c.getLong(c.getColumnIndex(PortmoneContract.TransferJournal.TIMESTAMP)));
        planned = c.getInt(c.getColumnIndex(PortmoneContract.TransferJournal.PLANNED)) == 1;
        sum = c.getFloat(c.getColumnIndex(PortmoneContract.TransferJournal.SUM));
        description = c.getString(c.getColumnIndex(PortmoneContract.TransferJournal.DESCRIPTION));
        webId = c.getString(c.getColumnIndex(PortmoneContract.Transfers.WEB_ID));

        Long id = c.getLong(c.getColumnIndex(PortmoneContract.TransferJournal.FROM_CASH_TYPE_ID));
        String name = c.getString(c.getColumnIndex(PortmoneContract.TransferJournal.FROM_CASH_TYPE_NAME));
        fromCashType = new CashType(id, name);

        id = c.getLong(c.getColumnIndex(PortmoneContract.TransferJournal.TO_CASH_TYPE_ID));
        name = c.getString(c.getColumnIndex(PortmoneContract.TransferJournal.TO_CASH_TYPE_NAME));
        toCashType = new CashType(id, name);

    }

    public Transfer(Date date, boolean planned, CashType fromCashType, CashType toCashType, float sum,
                    String description, String webId) {
        this.date = date;
        this.planned = planned;
        this.fromCashType = fromCashType;
        this.toCashType = toCashType;
        this.sum = sum;
        this.description = description;
        this.webId = webId;
    }

    public CashType getFromCashType() {
        return fromCashType;
    }

    public void setFromCashTypeId(CashType cashType) {
        this.fromCashType = cashType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public CashType getToCashType() {
        return toCashType;
    }

    public void setToCashType(CashType cashType) {
        this.toCashType = cashType;
    }

    public boolean isPlanned() {
        return planned;
    }

    public void setPlanned(boolean planned) {
        this.planned = planned;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.Transfers.TIMESTAMP, date.getTime());
        values.put(PortmoneContract.Transfers.PLANNED, planned?1:0);
        values.put(PortmoneContract.Transfers.FROM_CASH_TYPE_ID, fromCashType.getId());
        values.put(PortmoneContract.Transfers.TO_CASH_TYPE_ID, toCashType.getId());
        values.put(PortmoneContract.Transfers.SUM, sum);
        values.put(PortmoneContract.Transfers.DESCRIPTION, description);
        values.put(PortmoneContract.Transfers.WEB_ID, webId);
        return values;
    }


    public String getWebId() {
        return webId;
    }
}
