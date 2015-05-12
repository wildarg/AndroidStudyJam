package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class Expense extends Operation {
    Long cash_type_id;
    Long expense_type_id;

    public Expense(ContentValues values) {
        super(values);
        cash_type_id = values.getAsLong(PortmoneContract.Expenses.CASH_TYPE_ID);
        expense_type_id = values.getAsLong(PortmoneContract.Expenses.EXPENSE_TYPE_ID);
    }

    public Long getCashTypeId() {
        return cash_type_id;
    }

    public void setCashTypeId(Long id) {
        this.cash_type_id = id;
    }

    public Long getExpenseTypeId() {
        return expense_type_id;
    }

    public void setExpenseTypeId(Long id) {
        this.expense_type_id = id;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = super.toValues();
        values.put(PortmoneContract.Expenses.CASH_TYPE_ID, cash_type_id);
        values.put(PortmoneContract.Expenses.EXPENSE_TYPE_ID, expense_type_id);
        return values;
    }


}
