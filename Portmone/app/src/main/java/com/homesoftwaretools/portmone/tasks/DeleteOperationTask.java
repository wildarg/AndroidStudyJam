package com.homesoftwaretools.portmone.tasks;
/*
 * Created by Wild on 07.05.2015.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class DeleteOperationTask extends ProgressAsyncTask<Long, Void, Uri> {

    private final ContentResolver resolver;
    private final Context context;
    private final Uri journalUri;

    public DeleteOperationTask(Context context, Uri journalUri) {
        super(context);
        resolver = context.getContentResolver();
        this.context = context.getApplicationContext();
        this.journalUri = journalUri;
    }

    private Toast createCancelToast(final Context context, final Uri uri) {
        View toastView = View.inflate(context, R.layout.custom_toast, null);
        TextView tv = (TextView) toastView.findViewById(R.id.toastTextView);
        tv.setText("Отменить удаление");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new RestoreRecord(uri)).start();
            }
        });
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 34);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        return toast;
    }

    @Override
    protected Uri doInBackground(Long... params) {
        Uri uri = Uri.withAppendedPath(journalUri, params[0].toString());
        ContentValues values = getValues(uri);
        values.put(PortmoneContract.Journal.DELETED, 1);
        resolver.update(uri, values, null, null);
        return uri;
    }

    protected ContentValues getValues(Uri uri) {
        Cursor c = resolver.query(uri, null, null, null, null);
        c.moveToFirst();
        ContentValues values = new ContentValues();
        for (String col: c.getColumnNames()) {
            values.put(col, c.getString(c.getColumnIndex(col)));
        }
        return values;
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        Toast toast = createCancelToast(context, uri);
        toast.show();
    }

    private class RestoreRecord implements Runnable {
        private final Uri uri;

        public RestoreRecord(Uri uri) {
            this.uri = uri;
        }

        @Override
        public void run() {
            ContentValues values = getValues(uri);
            values.put(PortmoneContract.Journal.DELETED, 0);
            resolver.update(uri, values, null, null);
        }
    }

    protected ContentResolver getContentResolver() {
        return resolver;
    }


}
