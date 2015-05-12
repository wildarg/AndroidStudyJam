package com.homesoftwaretools.portmone.domain;
/*
 * Created by Wild on 04.05.2015.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

import java.util.Date;

public class Income {

    Long id;
    Date date;
    boolean planned;
    CashType cashType;
    IncomeType incomeType;
    float sum;
    String description;
    String webId;

    public Income() {
        this.date = new Date();
        this.cashType = new CashType();
        this.incomeType = new IncomeType();
    }

    public Income(Cursor c) {
        id = c.getLong(c.getColumnIndex(PortmoneContract.IncomeJournal._ID));
        date = new Date(c.getLong(c.getColumnIndex(PortmoneContract.IncomeJournal.TIMESTAMP)));
        planned = c.getInt(c.getColumnIndex(PortmoneContract.IncomeJournal.PLANNED)) == 1;
        sum = c.getFloat(c.getColumnIndex(PortmoneContract.IncomeJournal.SUM));
        description = c.getString(c.getColumnIndex(PortmoneContract.IncomeJournal.DESCRIPTION));
        webId = c.getString(c.getColumnIndex(PortmoneContract.IncomeJournal.WEB_ID));

        Long id = c.getLong(c.getColumnIndex(PortmoneContract.IncomeJournal.CASH_TYPE_ID));
        String name = c.getString(c.getColumnIndex(PortmoneContract.IncomeJournal.CASH_TYPE_NAME));
        cashType = new CashType(id, name);

        id = c.getLong(c.getColumnIndex(PortmoneContract.IncomeJournal.INCOME_TYPE_ID));
        name = c.getString(c.getColumnIndex(PortmoneContract.IncomeJournal.INCOME_TYPE_NAME));
        incomeType = new IncomeType(id, name);
    }

    public Income(Date date, boolean planned, CashType cashType, IncomeType incomeType, float sum,
                  String description, String webId) {
        this.date = date;
        this.planned = planned;
        this.cashType = cashType;
        this.incomeType = incomeType;
        this.sum = sum;
        this.description = description;
        this.webId = webId;
    }

    public CashType getCashType() {
        return cashType;
    }

    public void setCashType(@NonNull CashType cashType) {
        this.cashType = cashType;
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

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(@NonNull IncomeType incomeType) {
        this.incomeType = incomeType;
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
        values.put(PortmoneContract.Incomes.TIMESTAMP, date.getTime());
        values.put(PortmoneContract.Incomes.PLANNED, planned?1:0);
        values.put(PortmoneContract.Incomes.CASH_TYPE_ID, cashType.getId());
        values.put(PortmoneContract.Incomes.INCOME_TYPE_ID, incomeType.getId());
        values.put(PortmoneContract.Incomes.SUM, sum);
        values.put(PortmoneContract.Incomes.DESCRIPTION, description);
        values.put(PortmoneContract.Incomes.WEB_ID, webId);
        return values;
    }

    public String getWebId() {
        return webId;
    }
}
