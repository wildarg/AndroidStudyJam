package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class ExpenseTagLink extends TagLink{

    Long expense_id;

    public ExpenseTagLink(ContentValues values) {
        super(values);
        expense_id = values.getAsLong(PortmoneContract.ExpenseTags.EXPENSE_ID);
    }

    public Long getExpenseId() {
        return expense_id;
    }

    public void setExpenseId(Long id) {
        this.expense_id = id;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = super.toValues();
        values.put(PortmoneContract.ExpenseTags.EXPENSE_ID, expense_id);
        return values;
    }

}
