package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class Income extends Operation {

    Long cash_type_id;
    Long income_type_id;

    public Income(ContentValues values) {
        super(values);
        cash_type_id = values.getAsLong(PortmoneContract.Incomes.CASH_TYPE_ID);
        income_type_id = values.getAsLong(PortmoneContract.Incomes.INCOME_TYPE_ID);
    }

    public Long getCashTypeId() {
        return cash_type_id;
    }

    public void setCashTypeId(Long id) {
        this.cash_type_id = id;
    }

    public Long getIncomeTypeId() {
        return income_type_id;
    }

    public void setIncomeTypeId(Long id) {
        this.income_type_id = id;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = super.toValues();
        values.put(PortmoneContract.Incomes.CASH_TYPE_ID, cash_type_id);
        values.put(PortmoneContract.Incomes.INCOME_TYPE_ID, income_type_id);
        return values;
    }
}
