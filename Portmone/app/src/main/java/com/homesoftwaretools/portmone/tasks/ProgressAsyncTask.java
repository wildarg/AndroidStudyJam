package com.homesoftwaretools.portmone.tasks;/*
 * Created by Wild on 11.05.2015.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class ProgressAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private ProgressDialog progressDlg;

    public ProgressAsyncTask(Context context) {
        progressDlg = new ProgressDialog(context);
        progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDlg.setIndeterminate(true);
        progressDlg.setMessage("Выполнение операции");
        onProgressDialogCreated(progressDlg);
    }

    protected void onProgressDialogCreated(ProgressDialog dlg) {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDlg.show();
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        stopProgress();
    }


    @Override
    protected Result doInBackground(Params... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        stopProgress();
    }

    private void stopProgress() {
        try {
            if (progressDlg != null) {
                if (progressDlg.isShowing())
                    progressDlg.dismiss();
                progressDlg = null;
            }
        } catch (Exception e) {/*nope*/}
    }

}
