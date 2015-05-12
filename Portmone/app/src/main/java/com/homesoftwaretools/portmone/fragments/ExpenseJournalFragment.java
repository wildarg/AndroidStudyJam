package com.homesoftwaretools.portmone.fragments;
/*
 * Created by Wild on 06.05.2015.
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.CursorAdapter;

import com.homesoftwaretools.portmone.activities.ExpenseEditorActivity;
import com.homesoftwaretools.portmone.adapters.ExpenseJournalAdapter;
import com.homesoftwaretools.portmone.domain.Expense;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.tasks.AddExpenseAsyncTask;
import com.homesoftwaretools.portmone.tasks.DeleteOperationTask;
import com.homesoftwaretools.portmone.tasks.UpdateExpenseAsyncTask;

public class ExpenseJournalFragment extends AbstractJournalFragment {

    @Override
    protected CursorAdapter createAdapter() {
        return new ExpenseJournalAdapter(getActivity());
    }

    @Override
    protected void onDeleteActionExecute(long id) {
        DeleteOperationTask task = new DeleteOperationTask(getActivity(), PortmoneContract.Expenses.CONTENT_URI);
        task.execute(id);
    }

    @Override
    protected Uri getJournalContentUri() {
        return PortmoneContract.ExpenseJournal.CONTENT_URI;
    }

    @Override
    protected Intent getEditItemIntent(Cursor cursor) {
        Expense expense = new Expense(cursor);
        return ExpenseEditorActivity.createIntent(getActivity(), expense);
    }

    @Override
    protected void onRecordUpdate(Intent data) {
        Expense expense = ExpenseEditorActivity.getExpenseFromIntent(data);
        Long id = data.getLongExtra(ExpenseEditorActivity.EXTRA_ID, -1);
        UpdateExpenseAsyncTask task = new UpdateExpenseAsyncTask(getActivity(), id);
        task.execute(expense);
    }

    @Override
    protected void onRecordAdd(Intent data) {
        Expense expense = ExpenseEditorActivity.getExpenseFromIntent(data);
        AddExpenseAsyncTask task = new AddExpenseAsyncTask(getActivity());
        task.execute(expense);
    }

    @Override
    protected Intent getAddRecordIntent() {
        return ExpenseEditorActivity.createIntent(getActivity(), new Expense());
    }

}
