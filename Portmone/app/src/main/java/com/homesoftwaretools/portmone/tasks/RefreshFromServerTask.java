package com.homesoftwaretools.portmone.tasks;/*
 * Created by Wild on 12.05.2015.
 */

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.rest.PortmoneApi;
import com.homesoftwaretools.portmone.rest.resources.Expense;
import com.homesoftwaretools.portmone.rest.resources.ExpenseTagLink;
import com.homesoftwaretools.portmone.rest.resources.Income;
import com.homesoftwaretools.portmone.rest.resources.IncomeTagLink;
import com.homesoftwaretools.portmone.rest.resources.Lib;
import com.homesoftwaretools.portmone.rest.resources.Operation;
import com.homesoftwaretools.portmone.rest.resources.Resource;
import com.homesoftwaretools.portmone.rest.resources.Transfer;
import com.homesoftwaretools.portmone.rest.resources.TransferTagLink;

import java.util.List;

import retrofit.RetrofitError;

public class RefreshFromServerTask extends ProgressAsyncTask<Void, Void, Void> {

    private final ContentResolver resolver;
    private final PortmoneApi api;

    public RefreshFromServerTask(Context context, PortmoneApi api) {
        super(context);
        this.resolver = context.getContentResolver();
        this.api = api;
    }

    @Override
    protected void onProgressDialogCreated(ProgressDialog dlg) {
        super.onProgressDialogCreated(dlg);
        dlg.setMessage("Получение данных с сервера");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            refreshLib("cashtypes", PortmoneContract.CashTypes.CONTENT_URI);
            refreshLib("incometypes", PortmoneContract.IncomeTypes.CONTENT_URI);
            refreshLib("expensetypes", PortmoneContract.ExpenseTypes.CONTENT_URI);
            refreshLib("tags", PortmoneContract.Tags.CONTENT_URI);
            refreshIncomes();
            refreshExpenses();
            refreshTransfers();
            refreshIncomeTags();
            refreshExpenseTags();
            refreshTransferTags();
        } catch (RetrofitError e) {
            e.printStackTrace();
            Log.d("#RefreshTask", "error: " + e.getMessage());
        }
        return super.doInBackground(params);
    }

    private void refreshLib(String name, Uri uri) {
        List<Lib> lib = api.getLib(name);
        ContentValues[] values = new ContentValues[lib.size()];
        for (int i = 0; i < lib.size(); i++) {
            values[i] = lib.get(i).toValues();
            values[i].put(PortmoneContract.WebColumns._ID, values[i].getAsLong(PortmoneContract.WebColumns.WEB_ID));
        }
        resolver.bulkInsert(uri, values);
    }

    private void refreshIncomes() {
        List<Income> journal = api.getIncomes();
        ContentValues[] values = new ContentValues[journal.size()];
        for (int i = 0; i < journal.size(); i++) {
            values[i] = journal.get(i).toValues();
            values[i].put(PortmoneContract.WebColumns._ID, values[i].getAsLong(PortmoneContract.WebColumns.WEB_ID));
        }
        resolver.bulkInsert(PortmoneContract.Incomes.CONTENT_URI, values);
    }

    private void refreshExpenses() {
        List<Expense> journal = api.getExpenses();
        ContentValues[] values = new ContentValues[journal.size()];
        for (int i = 0; i < journal.size(); i++) {
            values[i] = journal.get(i).toValues();
            values[i].put(PortmoneContract.WebColumns._ID, values[i].getAsLong(PortmoneContract.WebColumns.WEB_ID));
        }
        resolver.bulkInsert(PortmoneContract.Expenses.CONTENT_URI, values);
    }

    private void refreshTransfers() {
        List<Transfer> journal = api.getTransfers();
        ContentValues[] values = new ContentValues[journal.size()];
        for (int i = 0; i < journal.size(); i++) {
            values[i] = journal.get(i).toValues();
            values[i].put(PortmoneContract.WebColumns._ID, values[i].getAsLong(PortmoneContract.WebColumns.WEB_ID));
        }
        resolver.bulkInsert(PortmoneContract.Transfers.CONTENT_URI, values);
    }

    private void refreshIncomeTags() {
        List<IncomeTagLink> links = api.getIncomeTags();
        ContentValues[] values = new ContentValues[links.size()];
        for (int i = 0; i < links.size(); i++) {
            values[i] = links.get(i).toValues();
            values[i].put(PortmoneContract.WebColumns._ID, values[i].getAsLong(PortmoneContract.WebColumns.WEB_ID));
        }
        resolver.bulkInsert(PortmoneContract.IncomeTags.CONTENT_URI, values);
    }

    private void refreshExpenseTags() {
        List<ExpenseTagLink> links = api.getExpenseTags();
        ContentValues[] values = new ContentValues[links.size()];
        for (int i = 0; i < links.size(); i++) {
            values[i] = links.get(i).toValues();
            values[i].put(PortmoneContract.WebColumns._ID, values[i].getAsLong(PortmoneContract.WebColumns.WEB_ID));
        }
        resolver.bulkInsert(PortmoneContract.ExpenseTags.CONTENT_URI, values);
    }

    private void refreshTransferTags() {
        List<TransferTagLink> links = api.getTransferTags();
        ContentValues[] values = new ContentValues[links.size()];
        for (int i = 0; i < links.size(); i++) {
            values[i] = links.get(i).toValues();
            values[i].put(PortmoneContract.WebColumns._ID, values[i].getAsLong(PortmoneContract.WebColumns.WEB_ID));
        }
        resolver.bulkInsert(PortmoneContract.TransferTags.CONTENT_URI, values);
    }

    private <T extends Resource> void bulkInsertResources(List<T> data, Uri uri) {
        ContentValues[] values = new ContentValues[data.size()];
        for (int i = 0; i < data.size(); i++) {
            values[i] = data.get(i).toValues();
            values[i].put(PortmoneContract.WebColumns._ID, values[i].getAsLong(PortmoneContract.WebColumns.WEB_ID));
        }
        resolver.bulkInsert(uri, values);
    }
}
