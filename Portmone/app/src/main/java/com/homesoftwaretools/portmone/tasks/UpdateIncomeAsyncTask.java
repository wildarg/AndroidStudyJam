package com.homesoftwaretools.portmone.tasks;
/*
 * Created by Wild on 07.05.2015.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.homesoftwaretools.portmone.domain.CashType;
import com.homesoftwaretools.portmone.domain.Income;
import com.homesoftwaretools.portmone.domain.IncomeType;
import com.homesoftwaretools.portmone.domain.Tag;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.utils.HashtagUtils;

import java.util.List;

public class UpdateIncomeAsyncTask extends ProgressAsyncTask<Income, Void, Void> {

    private final ContentResolver resolver;
    private final Uri uri;

    public UpdateIncomeAsyncTask(Context context, Long id) {
        super(context);
        resolver = context.getContentResolver();
        uri = Uri.withAppendedPath(PortmoneContract.Incomes.CONTENT_URI, String.valueOf(id));
    }

    @Override
    protected Void doInBackground(Income... params) {
        Income income = params[0];
        String cashTypeName = income.getCashType().getName();
        String incomeTypeName = income.getIncomeType().getName();
        income.setCashType(getCashTypeByName(cashTypeName));
        income.setIncomeType(getIncomeTypeByName(incomeTypeName));
        resolver.update(uri, income.getValues(), null, null);

        String where = PortmoneContract.IncomeTags.INCOME_ID + " = ?";
        String[] args = new String[] {uri.getLastPathSegment()};
        resolver.delete(PortmoneContract.IncomeTags.CONTENT_URI, where, args);

        List<String> tags = HashtagUtils.getTags(income.getDescription());
        for (String tagName: tags) {
            Tag tag = getTagByName(tagName);
            ContentValues values = new ContentValues();
            values.put(PortmoneContract.IncomeTags.INCOME_ID, uri.getLastPathSegment());
            values.put(PortmoneContract.IncomeTags.TAG_ID, tag.getId());
            resolver.insert(PortmoneContract.IncomeTags.CONTENT_URI, values);
        }

        return null;
    }

    private CashType getCashTypeByName(String name) {
        CashType cashType;
        String where = PortmoneContract.CashTypes.NAME + "=?";
        String[] args = new String[] {name};
        Cursor c = resolver.query(PortmoneContract.CashTypes.CONTENT_URI, null, where, args, null);
        if (c.moveToFirst())
            cashType = new CashType(c);
        else {
            cashType = new CashType(name);
            Uri uri = resolver.insert(PortmoneContract.CashTypes.CONTENT_URI, cashType.getValues());
            cashType.setId(Long.valueOf(uri.getLastPathSegment()));
        }
        c.close();
        return cashType;
    }

    private IncomeType getIncomeTypeByName(String name) {
        IncomeType incomeType;
        String where = PortmoneContract.IncomeTypes.NAME + "=?";
        String[] args = new String[] {name};
        Cursor c = resolver.query(PortmoneContract.IncomeTypes.CONTENT_URI, null, where, args, null);
        if (c.moveToFirst())
            incomeType = new IncomeType(c);
        else {
            incomeType = new IncomeType(name);
            Uri uri = resolver.insert(PortmoneContract.IncomeTypes.CONTENT_URI, incomeType.getValues());
            incomeType.setId(Long.valueOf(uri.getLastPathSegment()));
        }
        c.close();
        return incomeType;
    }

    private Tag getTagByName(String name) {
        Tag tag;
        String where = PortmoneContract.Tags.NAME + "=?";
        String[] args = new String[] {name};
        Cursor c = resolver.query(PortmoneContract.Tags.CONTENT_URI, null, where, args, null);
        if (c.moveToFirst()) {
            tag = new Tag(c);
        } else {
            tag = new Tag(name);
            Uri uri = resolver.insert(PortmoneContract.Tags.CONTENT_URI, tag.getValues());
            tag.setId(Long.valueOf(uri.getLastPathSegment()));
        }
        c.close();
        return tag;
    }


}
