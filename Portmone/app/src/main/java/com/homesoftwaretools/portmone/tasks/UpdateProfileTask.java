package com.homesoftwaretools.portmone.tasks;
/*
 * Created by Wild on 10.05.2015.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.homesoftwaretools.portmone.domain.User;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.rest.ApiController;

public class UpdateProfileTask extends AsyncTask<Void, Void, Void> {
    private final ContentResolver resolver;
    private final User user;
    private final Context context;

    public UpdateProfileTask(Context context, User user) {
        this.resolver = context.getContentResolver();
        this.user = user;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Cursor c = resolver.query(PortmoneContract.Users.CONTENT_URI, null, null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        if (exists)
            resolver.update(PortmoneContract.Users.CONTENT_URI, user.getValues(), null, null);
        else
            resolver.insert(PortmoneContract.Users.CONTENT_URI, user.getValues());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
