package com.wild.test.thesmspost;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;

import android.provider.ContactsContract;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "#MainActivity";
    private SmsListAdapter adapter;
    private Switch stateSwitch;
    private CheckStateReceiver checkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupHeaderFont();

        stateSwitch = (Switch) findViewById(R.id.serviceActiveSwitch);
        stateSwitch.setOnCheckedChangeListener(new StateServiceChangeListener());
        //stateSwitch.setOnClickListener(new StateServiceClickListener());
        checkState();

        adapter = new SmsListAdapter(this);
        ListView smsListView = (ListView) findViewById(R.id.smsListView);
        smsListView.setAdapter(adapter);
        smsListView.setOnItemClickListener(new OnSmsClickListener());
        getLoaderManager().initLoader(0, null, this);
    }

    private void checkState() {
        if (checkReceiver == null) {
            checkReceiver = new CheckStateReceiver();
            registerReceiver(checkReceiver, new IntentFilter(SMSService.RESULT_BROADCAST));
        }
        Log.d(LOG_TAG, "send broadcast check state");
        Intent check = new Intent(SMSService.CHECK_STATE_BROADCAST);
        sendBroadcast(check);
    }

    private void setupHeaderFont() {
        final Typeface headlineFont = Typeface.createFromAsset(getAssets(), "fonts/Headlinetext.ttf");
        ((TextView) findViewById(R.id.appNameTextView)).setTypeface(headlineFont);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {Telephony.TextBasedSmsColumns.ADDRESS,
                Telephony.TextBasedSmsColumns.BODY, Telephony.TextBasedSmsColumns.DATE_SENT};
        return new CursorLoader(this, Uri.parse("content://sms/inbox"), projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private class StateServiceChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(LOG_TAG, "change state button " + buttonView);
            Intent intent = new Intent(MainActivity.this, SMSService.class);
            if (isChecked) {
                Log.d(LOG_TAG, "start service");
                startService(intent);
            } else {
                Log.d(LOG_TAG, "stop service");
                stopService(intent);
            }
        }
    }

    private class OnSmsClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor c = adapter.getItem(position);
            if (c != null) {
                String phone = c.getString(0);
                String text = c.getString(1);
                long time = c.getLong(2);
                SMSPostActivity.showSMSPost(MainActivity.this, new Date(time), phone, text);
            }
        }
    }

    private class CheckStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "receive check state");
            int state = intent.getIntExtra(SMSService.EXTRA_STATE, 0);
            stateSwitch.setChecked(state == SMSService.STATE_ACTIVE);
        }
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
        super.onStop();
        if (checkReceiver != null) {
            Log.d(LOG_TAG, "unregister receiver check state");
            MainActivity.this.unregisterReceiver(checkReceiver);
            checkReceiver = null;
        }
    }

    private class StateServiceClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "switch click");
            Intent intent = new Intent(MainActivity.this, SMSService.class);
            if (stateSwitch.isChecked())
                startService(intent);
            else
                stopService(intent);

        }
    }
}
