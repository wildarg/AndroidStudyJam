package com.wild.androidstudyjam.wakeapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Wild on 17.04.2015.
 */
public class WakeUpActivity extends Activity {

    private static final String LOG_TAG = "#WakeUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "create");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_wakeup);
        wakeUp();
    }

    private void wakeUp() {
        Log.d(LOG_TAG, "start wake up");
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        Log.d(LOG_TAG, "power manager " + pm);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        Log.d(LOG_TAG, "wake lock " + wakeLock);
        wakeLock.acquire(2000);
        Log.d(LOG_TAG, "wake up!");
        Toast.makeText(this, "wake up", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "resume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "start");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "destroy");
    }
}
