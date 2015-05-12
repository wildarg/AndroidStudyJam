package com.homesoftwaretools.portmone.provider;
/*
 * Created by Wild on 03.05.2015.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.homesoftwaretools.portmone.rest.ApiController;
import com.homesoftwaretools.portmone.rest.PortmoneApi;
import com.homesoftwaretools.portmone.rest.resources.Expense;
import com.homesoftwaretools.portmone.rest.resources.ExpenseTagLink;
import com.homesoftwaretools.portmone.rest.resources.Income;
import com.homesoftwaretools.portmone.rest.resources.IncomeTagLink;
import com.homesoftwaretools.portmone.rest.resources.Resource;
import com.homesoftwaretools.portmone.rest.resources.ResourceFactory;
import com.homesoftwaretools.portmone.rest.resources.Transfer;
import com.homesoftwaretools.portmone.rest.resources.TransferTagLink;
import com.homesoftwaretools.portmone.security.AuthorithationManager;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class PortmoneProvider extends BaseProvider {

    private static final String DB_NAME = "portmone.db";
    private static final int DB_VERSION = 11;
    private AuthorithationManager manager;

    @Override
    public boolean onCreate() {
        manager = AuthorithationManager.getInstance(getContext());
        return super.onCreate();
    }

    @Override
    protected String getDBName() {
        return DB_NAME;
    }

    @Override
    protected int getDBVersion() {
        return DB_VERSION;
    }

    @Override
    protected String getAuthority() {
        return PortmoneContract.AUTHORITY;
    }

    @Override
    protected Map<String, String[]> getDBMap() {
        Map<String,String[]> map = new HashMap<>();
        map.put(PortmoneContract.Users.USERS, PortmoneContract.Users.FIELDS);
        map.put(PortmoneContract.CashTypes.CASH_TYPES, PortmoneContract.CashTypes.FIELDS);
        map.put(PortmoneContract.IncomeTypes.INCOME_TYPES, PortmoneContract.IncomeTypes.FIELDS);
        map.put(PortmoneContract.ExpenseTypes.EXPENSE_TYPES, PortmoneContract.ExpenseTypes.FIELDS);
        map.put(PortmoneContract.Tags.TAGS, PortmoneContract.Tags.FIELDS);
        map.put(PortmoneContract.Incomes.INCOMES, PortmoneContract.Incomes.FIELDS);
        map.put(PortmoneContract.Expenses.EXPENSES, PortmoneContract.Expenses.FIELDS);
        map.put(PortmoneContract.Transfers.TRANSFERS, PortmoneContract.Transfers.FIELDS);
        map.put(PortmoneContract.IncomeTags.INCOME_TAGS, PortmoneContract.IncomeTags.FIELDS);
        map.put(PortmoneContract.ExpenseTags.EXPENSE_TAGS, PortmoneContract.ExpenseTags.FIELDS);
        map.put(PortmoneContract.TransferTags.TRANSFER_TAGS, PortmoneContract.TransferTags.FIELDS);
        return map;
    }

    @Override
    protected Map<String, String[]> getRawQueries() {
        Map<String,String[]> map = new HashMap<>();
        map.put(PortmoneContract.IncomeJournal.INCOME_JOURNAL, PortmoneContract.IncomeJournal.PARAMS);
        map.put(PortmoneContract.ExpenseJournal.EXPENSE_JOURNAL, PortmoneContract.ExpenseJournal.PARAMS);
        map.put(PortmoneContract.TransferJournal.TRANSFER_JOURNAL, PortmoneContract.TransferJournal.PARAMS);
        map.put(PortmoneContract.BallanceReport.BALLANCE_REPORT, PortmoneContract.BallanceReport.PARAMS);
        return map;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return super.delete(uri, addTokenCondition(selection), selectionArgs);
    }

    @Override
    protected void onDelete(String tableName, Uri uri) {
        Cursor c = query(uri, null, null, null, null);
        String webId = null;
        if (c.moveToFirst())
            webId = c.getString(c.getColumnIndex(PortmoneContract.WebColumns.WEB_ID));
        c.close();
        if (webId != null) {
            PortmoneApi api = ApiController.getInstance(getContext()).getApi();
            try {
                Response res = api.delete(tableName.replace("_", ""), webId);
            } catch (RetrofitError e) {
                e.printStackTrace();
                Log.d("#Provider", "delete error: " + e.getMessage());
            }
        }
    }

    private String addTokenCondition(String selection) {
        String where = PortmoneContract.WebColumns.TOKEN + "='" + manager.getToken() + "'";
        if (!TextUtils.isEmpty(selection))
            where += " and (" + selection + ")";
        return where;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        values.put(PortmoneContract.WebColumns.TOKEN, manager.getToken());
        return super.insert(uri, values);
    }

    @Override
    protected ContentValues onInsert(String tableName, ContentValues values) {
        super.onInsert(tableName, values);
        Resource resource = ResourceFactory.create(tableName, values);
        if (tableName.equals(PortmoneContract.Incomes.INCOMES)) {
            Income income = (Income) resource;
            income.setCashTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.CashTypes.CONTENT_URI, income.getCashTypeId().toString())));
            income.setIncomeTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.IncomeTypes.CONTENT_URI, income.getIncomeTypeId().toString())));
        } else if (tableName.equals(PortmoneContract.Expenses.EXPENSES)) {
            Expense expense = (Expense) resource;
            expense.setCashTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.CashTypes.CONTENT_URI, expense.getCashTypeId().toString())));
            expense.setExpenseTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.ExpenseTypes.CONTENT_URI, expense.getExpenseTypeId().toString())));
        } else if (tableName.equals(PortmoneContract.Transfers.TRANSFERS)) {
            Transfer transfer = (Transfer) resource;
            transfer.setFromCashTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.CashTypes.CONTENT_URI, transfer.getFromCashTypeId().toString())));
            transfer.setToCashTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.CashTypes.CONTENT_URI, transfer.getToCashTypeId().toString())));
        } else if (tableName.equals(PortmoneContract.IncomeTags.INCOME_TAGS)) {
            IncomeTagLink link = (IncomeTagLink) resource;
            link.setIncomeId(getWebId(Uri.withAppendedPath(PortmoneContract.Incomes.CONTENT_URI, link.getIncomeId().toString())));
            link.setTagId(getWebId(Uri.withAppendedPath(PortmoneContract.Tags.CONTENT_URI, link.getTagId().toString())));
        } else if (tableName.equals(PortmoneContract.ExpenseTags.EXPENSE_TAGS)) {
            ExpenseTagLink link = (ExpenseTagLink) resource;
            link.setExpenseId(getWebId(Uri.withAppendedPath(PortmoneContract.Incomes.CONTENT_URI, link.getExpenseId().toString())));
            link.setTagId(getWebId(Uri.withAppendedPath(PortmoneContract.Tags.CONTENT_URI, link.getTagId().toString())));
        } else if (tableName.equals(PortmoneContract.TransferTags.TRANSFER_TAGS)) {
            TransferTagLink link = (TransferTagLink) resource;
            link.setTransferId(getWebId(Uri.withAppendedPath(PortmoneContract.Transfers.CONTENT_URI, link.getTransferId().toString())));
            link.setTagId(getWebId(Uri.withAppendedPath(PortmoneContract.Tags.CONTENT_URI, link.getTagId().toString())));
        }

        PortmoneApi api = ApiController.getInstance(getContext()).getApi();
        try {
            Response res = api.insert(tableName.replace("_", ""), resource);
            String webId = ApiController.getIdFromResponse(res);
            values.put(PortmoneContract.WebColumns.WEB_ID, webId);
            return values;
        } catch (RetrofitError e) {
            e.printStackTrace();
            Log.d("#Provider", "insert error: " + e.getMessage());
        }
        return values;
    }

    private static final String[] WEB_ID_PROJECTION = {PortmoneContract.WebColumns.WEB_ID};
    private Long getWebId(Uri uri) {
        Cursor c = query(uri, WEB_ID_PROJECTION, null, null, null);
        Long webId = null;
        if (c.moveToFirst())
            webId = c.getLong(0);
        c.close();
        return webId;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return super.query(uri, projection, addTokenCondition(selection), selectionArgs, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        values.put(PortmoneContract.WebColumns.TOKEN, manager.getToken());
        return super.update(uri, values, addTokenCondition(selection), selectionArgs);
    }

    @Override
    protected void onUpdate(String tableName, ContentValues values) {
        Resource resource = ResourceFactory.create(tableName, values);
        String webId = values.getAsString(PortmoneContract.WebColumns.WEB_ID);

        if (tableName.equals(PortmoneContract.Incomes.INCOMES)) {
            Income income = (Income) resource;
            income.setCashTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.CashTypes.CONTENT_URI, income.getCashTypeId().toString())));
            income.setIncomeTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.IncomeTypes.CONTENT_URI, income.getIncomeTypeId().toString())));
        } else if (tableName.equals(PortmoneContract.Expenses.EXPENSES)) {
            Expense expense = (Expense) resource;
            expense.setCashTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.CashTypes.CONTENT_URI, expense.getCashTypeId().toString())));
            expense.setExpenseTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.ExpenseTypes.CONTENT_URI, expense.getExpenseTypeId().toString())));
        } else if (tableName.equals(PortmoneContract.Transfers.TRANSFERS)) {
            Transfer transfer = (Transfer) resource;
            transfer.setFromCashTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.CashTypes.CONTENT_URI, transfer.getFromCashTypeId().toString())));
            transfer.setToCashTypeId(getWebId(Uri.withAppendedPath(PortmoneContract.CashTypes.CONTENT_URI, transfer.getToCashTypeId().toString())));
        } else if (tableName.equals(PortmoneContract.IncomeTags.INCOME_TAGS)) {
            IncomeTagLink link = (IncomeTagLink) resource;
            link.setIncomeId(getWebId(Uri.withAppendedPath(PortmoneContract.Incomes.CONTENT_URI, link.getIncomeId().toString())));
            link.setTagId(getWebId(Uri.withAppendedPath(PortmoneContract.Tags.CONTENT_URI, link.getTagId().toString())));
        } else if (tableName.equals(PortmoneContract.ExpenseTags.EXPENSE_TAGS)) {
            ExpenseTagLink link = (ExpenseTagLink) resource;
            link.setExpenseId(getWebId(Uri.withAppendedPath(PortmoneContract.Incomes.CONTENT_URI, link.getExpenseId().toString())));
            link.setTagId(getWebId(Uri.withAppendedPath(PortmoneContract.Tags.CONTENT_URI, link.getTagId().toString())));
        } else if (tableName.equals(PortmoneContract.TransferTags.TRANSFER_TAGS)) {
            TransferTagLink link = (TransferTagLink) resource;
            link.setTransferId(getWebId(Uri.withAppendedPath(PortmoneContract.Transfers.CONTENT_URI, link.getTransferId().toString())));
            link.setTagId(getWebId(Uri.withAppendedPath(PortmoneContract.Tags.CONTENT_URI, link.getTagId().toString())));
        }

        PortmoneApi api = ApiController.getInstance(getContext()).getApi();
        try {
            Response response;
            if (TextUtils.isEmpty(webId))
                response = api.update(tableName.replace("_", ""), resource);
            else
                response = api.update(tableName.replace("_", ""), webId, resource);
        } catch (RetrofitError e) {
            e.printStackTrace();
            Log.d("#Provider", "update error: " + e.getMessage());
        }
    }

    @Override
    protected String[] onGetViewSelectionArgs(String[] selectionArgs) {
        String[] tokenArgs = new String[selectionArgs.length + 1];
        String log = "";
        for (int i = 0; i < selectionArgs.length; i++) {
            tokenArgs[i] = selectionArgs[i];
            log += tokenArgs[i] + "; ";
        }
        tokenArgs[tokenArgs.length - 1] = manager.getToken();
        log += tokenArgs[tokenArgs.length - 1];
        Log.d("#PortmoneProvider", "view get args: " + log);
        return tokenArgs;
    }
}
