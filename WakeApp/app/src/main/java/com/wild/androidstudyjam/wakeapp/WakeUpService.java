package com.wild.androidstudyjam.wakeapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by Wild on 17.04.2015.
 */
public class WakeUpService extends Service {
    private static final String LOG_TAG = "#WakeUpService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(40);
                    showWakeUpActivity();
                    Log.d(LOG_TAG, "done");
                    stopSelf();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "interrupt exception");
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void wakeUp() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire(2000);
        Log.d(LOG_TAG, "wake up!");
    }

    private void showWakeUpActivity() {
        Log.d(LOG_TAG, "start wakeup activity");
        Intent intent = new Intent("com.wild.androidstudyjam.wakeapp.WAKEUP");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }
}
