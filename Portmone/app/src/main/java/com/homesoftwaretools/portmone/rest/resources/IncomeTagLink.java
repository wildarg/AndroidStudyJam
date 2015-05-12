package com.homesoftwaretools.portmone.rest.resources;/*
 * Created by Wild on 11.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class IncomeTagLink extends TagLink {

    Long income_id;

    public IncomeTagLink(ContentValues values) {
        super(values);
        income_id = values.getAsLong(PortmoneContract.IncomeTags.INCOME_ID);
    }

    public Long getIncomeId() {
        return income_id;
    }

    public void setIncomeId(Long id) {
        this.income_id = id;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = super.toValues();
        values.put(PortmoneContract.IncomeTags.INCOME_ID, income_id);
        return values;
    }
}
