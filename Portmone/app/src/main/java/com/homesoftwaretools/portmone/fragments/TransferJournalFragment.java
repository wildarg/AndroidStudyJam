package com.homesoftwaretools.portmone.fragments;
/*
 * Created by Wild on 06.05.2015.
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.CursorAdapter;

import com.homesoftwaretools.portmone.activities.TransferEditorActivity;
import com.homesoftwaretools.portmone.adapters.TransferJournalAdapter;
import com.homesoftwaretools.portmone.domain.Transfer;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.tasks.AddTransferAsyncTask;
import com.homesoftwaretools.portmone.tasks.DeleteOperationTask;
import com.homesoftwaretools.portmone.tasks.UpdateTransferAsyncTask;

public class TransferJournalFragment extends AbstractJournalFragment {

    @Override
    protected CursorAdapter createAdapter() {
        return new TransferJournalAdapter(getActivity());
    }

    @Override
    protected void onDeleteActionExecute(long id) {
        DeleteOperationTask task = new DeleteOperationTask(getActivity(), PortmoneContract.Transfers.CONTENT_URI);
        task.execute(id);
    }

    @Override
    protected Uri getJournalContentUri() {
        return PortmoneContract.TransferJournal.CONTENT_URI;
    }

    @Override
    protected Intent getEditItemIntent(Cursor cursor) {
        Transfer transfer = new Transfer(cursor);
        return TransferEditorActivity.createIntent(getActivity(), transfer);
    }

    @Override
    protected void onRecordUpdate(Intent data) {
        Transfer transfer = TransferEditorActivity.getTransferFromIntent(data);
        Long id = data.getLongExtra(TransferEditorActivity.EXTRA_ID, -1);
        UpdateTransferAsyncTask task = new UpdateTransferAsyncTask(getActivity(), id);
        task.execute(transfer);
    }

    @Override
    protected void onRecordAdd(Intent data) {
        Transfer transfer = TransferEditorActivity.getTransferFromIntent(data);
        AddTransferAsyncTask task = new AddTransferAsyncTask(getActivity());
        task.execute(transfer);
    }

    @Override
    protected Intent getAddRecordIntent() {
        return TransferEditorActivity.createIntent(getActivity(), new Transfer());
    }

}
