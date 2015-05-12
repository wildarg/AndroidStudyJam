package com.homesoftwaretools.portmone.tasks;/*
 * Created by Wild on 12.05.2015.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class UpdateNotesTask extends ProgressAsyncTask<Void, Void, Void> {

    private final String notes;
    private final ContentResolver resolver;

    public UpdateNotesTask(Context context, String notes) {
        super(context);
        this.notes = notes;
        this.resolver = context.getContentResolver();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.Users.NOTES, notes);
        resolver.update(PortmoneContract.Users.CONTENT_URI, values, null, null);
        return super.doInBackground(params);
    }
}
