package com.homesoftwaretools.portmone.fragments;
/*
 * Created by Wild on 06.05.2015.
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.CursorAdapter;

import com.homesoftwaretools.portmone.activities.IncomeEditorActivity;
import com.homesoftwaretools.portmone.adapters.IncomeJournalAdapter;
import com.homesoftwaretools.portmone.domain.Income;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.tasks.AddIncomeAsyncTask;
import com.homesoftwaretools.portmone.tasks.DeleteOperationTask;
import com.homesoftwaretools.portmone.tasks.UpdateIncomeAsyncTask;

public class IncomeJournalFragment extends AbstractJournalFragment {

    @Override
    protected CursorAdapter createAdapter() {
        return new IncomeJournalAdapter(getActivity());
    }

    @Override
    protected void onDeleteActionExecute(long id) {
        DeleteOperationTask task = new DeleteOperationTask(getActivity(), PortmoneContract.Incomes.CONTENT_URI);
        task.execute(id);
    }

    @Override
    protected Uri getJournalContentUri() {
        return PortmoneContract.IncomeJournal.CONTENT_URI;
    }

    @Override
    protected Intent getEditItemIntent(Cursor cursor) {
        Income income = new Income(cursor);
        return IncomeEditorActivity.createIntent(getActivity(), income);
    }

    @Override
    protected void onRecordUpdate(Intent data) {
        Income income = IncomeEditorActivity.getIncomeFromIntent(data);
        Long id = data.getLongExtra(IncomeEditorActivity.EXTRA_ID, -1);
        UpdateIncomeAsyncTask task = new UpdateIncomeAsyncTask(getActivity(), id);
        task.execute(income);
    }

    @Override
    protected void onRecordAdd(Intent data) {
        Income income = IncomeEditorActivity.getIncomeFromIntent(data);
        AddIncomeAsyncTask task = new AddIncomeAsyncTask(getActivity());
        task.execute(income);
    }

    @Override
    protected Intent getAddRecordIntent() {
        return IncomeEditorActivity.createIntent(getActivity(), new Income());
    }

}
