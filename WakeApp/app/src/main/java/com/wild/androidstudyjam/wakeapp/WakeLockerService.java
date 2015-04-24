package com.wild.androidstudyjam.wakeapp;/*
 * Created by Wild on 24.04.2015.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class WakeLockerService extends Service {


    private static final String LOG_TAG = "#WakeLockerService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(40);
                    WakeLocker.acquire(getApplicationContext(), 2000);
                    stopSelf();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "error: " + e);
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
        WakeLocker.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }
}
