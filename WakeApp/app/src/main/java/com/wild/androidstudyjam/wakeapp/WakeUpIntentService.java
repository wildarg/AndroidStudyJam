package com.wild.androidstudyjam.wakeapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Wild on 17.04.2015.
 */
public class WakeUpIntentService extends IntentService {

    private static final String LOG_TAG = "#WakeUpIntentService";

    public WakeUpIntentService() {
        super("wake up service");
        Log.d(LOG_TAG, "create service");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "service handle wake up");
        try {
            Thread.sleep(6000);
            wakeUp();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "interrupt service");
        }
    }

    private void wakeUp() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire(2000);
        Log.d(LOG_TAG, "wake up!");
        Toast.makeText(getApplicationContext(), "wake up", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "destroy service");
        super.onDestroy();
    }
}
