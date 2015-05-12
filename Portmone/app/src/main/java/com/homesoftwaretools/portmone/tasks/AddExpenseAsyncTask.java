package com.homesoftwaretools.portmone.tasks;
/*
 * Created by Wild on 06.05.2015.
 */

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.homesoftwaretools.portmone.domain.CashType;
import com.homesoftwaretools.portmone.domain.Expense;
import com.homesoftwaretools.portmone.domain.ExpenseType;
import com.homesoftwaretools.portmone.domain.Tag;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.utils.HashtagUtils;

import java.util.List;

public class AddExpenseAsyncTask extends ProgressAsyncTask<Expense, Void, Void> {

    private final ContentResolver resolver;
    private Context context;
    private ProgressDialog dlg;

    public AddExpenseAsyncTask(Context context) {
        super(context);
        resolver = context.getContentResolver();
        this.context = context;
    }

    @Override
    protected Void doInBackground(Expense... params) {
        Expense expense = params[0];
        String cashTypeName = expense.getCashType().getName();
        String expenseTypeName = expense.getExpenseType().getName();
        expense.setCashType(getCashTypeByName(cashTypeName));
        expense.setExpenseType(getExpenseTypeByName(expenseTypeName));
        Uri uri = resolver.insert(PortmoneContract.Expenses.CONTENT_URI, expense.getValues());
        List<String> tags = HashtagUtils.getTags(expense.getDescription());
        for (String tagName: tags) {
            Tag tag = getTagByName(tagName);
            ContentValues values = new ContentValues();
            values.put(PortmoneContract.ExpenseTags.EXPENSE_ID, uri.getLastPathSegment());
            values.put(PortmoneContract.ExpenseTags.TAG_ID, tag.getId());
            resolver.insert(PortmoneContract.ExpenseTags.CONTENT_URI, values);
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

    private ExpenseType getExpenseTypeByName(String name) {
        ExpenseType expenseType;
        String where = PortmoneContract.ExpenseTypes.NAME + "=?";
        String[] args = new String[] {name};
        Cursor c = resolver.query(PortmoneContract.ExpenseTypes.CONTENT_URI, null, where, args, null);
        if (c.moveToFirst())
            expenseType = new ExpenseType(c);
        else {
            expenseType = new ExpenseType(name);
            Uri uri = resolver.insert(PortmoneContract.ExpenseTypes.CONTENT_URI, expenseType.getValues());
            expenseType.setId(Long.valueOf(uri.getLastPathSegment()));
        }
        c.close();
        return expenseType;
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
