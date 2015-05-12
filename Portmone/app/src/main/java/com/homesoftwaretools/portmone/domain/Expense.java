package com.homesoftwaretools.portmone.domain;/*
 * Created by Wild on 04.05.2015.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

import java.util.Date;

public class Expense {

    Long id;
    Date date;
    boolean planned;
    CashType cashType;
    ExpenseType expenseType;
    float sum;
    String description;
    String webId;

    public Expense() {
        date = new Date();
        cashType = new CashType();
        expenseType = new ExpenseType();
    }

    public Expense(Cursor c) {
        id = c.getLong(c.getColumnIndex(PortmoneContract.ExpenseJournal._ID));
        date = new Date(c.getLong(c.getColumnIndex(PortmoneContract.ExpenseJournal.TIMESTAMP)));
        planned = c.getInt(c.getColumnIndex(PortmoneContract.ExpenseJournal.PLANNED)) == 1;
        sum = c.getFloat(c.getColumnIndex(PortmoneContract.ExpenseJournal.SUM));
        description = c.getString(c.getColumnIndex(PortmoneContract.ExpenseJournal.DESCRIPTION));
        webId = c.getString(c.getColumnIndex(PortmoneContract.ExpenseJournal.WEB_ID));

        Long id = c.getLong(c.getColumnIndex(PortmoneContract.ExpenseJournal.CASH_TYPE_ID));
        String name = c.getString(c.getColumnIndex(PortmoneContract.ExpenseJournal.CASH_TYPE_NAME));
        cashType = new CashType(id, name);

        id = c.getLong(c.getColumnIndex(PortmoneContract.ExpenseJournal.EXPENSE_TYPE_ID));
        name = c.getString(c.getColumnIndex(PortmoneContract.ExpenseJournal.EXPENSE_TYPE_NAME));
        expenseType = new ExpenseType(id, name);

    }

    public Expense(Date date, boolean planned, CashType cashType, ExpenseType expenseType, float sum,
                   String description, String webId) {
        this.date = date;
        this.planned = planned;
        this.cashType = cashType;
        this.expenseType = expenseType;
        this.sum = sum;
        this.description = description;
        this.webId = webId;
    }

    public CashType getCashType() {
        return cashType;
    }

    public void setCashType(CashType cashType) {
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

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
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
        values.put(PortmoneContract.Expenses.TIMESTAMP, date.getTime());
        values.put(PortmoneContract.Expenses.PLANNED, planned?1:0);
        values.put(PortmoneContract.Expenses.CASH_TYPE_ID, cashType.getId());
        values.put(PortmoneContract.Expenses.EXPENSE_TYPE_ID, expenseType.getId());
        values.put(PortmoneContract.Expenses.SUM, sum);
        values.put(PortmoneContract.Expenses.DESCRIPTION, description);
        values.put(PortmoneContract.Expenses.WEB_ID, webId);
        return values;
    }

    public String getWebId() {
        return webId;
    }
}
