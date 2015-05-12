package com.homesoftwaretools.portmone.domain;/*
 * Created by Wild on 08.05.2015.
 */

import android.database.Cursor;

import java.util.Date;

public class Ballance {

    long id;
    String name;
    float oldIncome;
    float oldExpense;
    float oldInTransfer;
    float oldOutTransfer;
    float income;
    float expense;
    float inTransfer;
    float outTransfer;

    public Ballance(Cursor c) {
        id = c.getLong(c.getColumnIndex("_id"));
        name = c.getString(c.getColumnIndex("name"));
        oldIncome = c.getFloat(c.getColumnIndex("old_in_sum"));
        oldExpense = c.getFloat(c.getColumnIndex("old_out_sum"));
        oldInTransfer = c.getFloat(c.getColumnIndex("old_tr_in_sum"));
        oldOutTransfer = c.getFloat(c.getColumnIndex("old_tr_out_sum"));
        income = c.getFloat(c.getColumnIndex("in_sum"));
        expense = c.getFloat(c.getColumnIndex("out_sum"));
        inTransfer = c.getFloat(c.getColumnIndex("tr_in_sum"));
        outTransfer = c.getFloat(c.getColumnIndex("tr_out_sum"));
    }

    public float getBallance() {
        return getStartBallance() + income - expense + inTransfer - outTransfer;
    }

    public float getStartBallance() {
        return oldIncome - oldExpense + oldInTransfer - oldOutTransfer;
    }

    public float getIncome() {
        return income;
    }

    public float getExpense() {
        return expense;
    }

    public float getInTransfer() {
        return inTransfer;
    }

    public float getOutTransfer() {
        return outTransfer;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
