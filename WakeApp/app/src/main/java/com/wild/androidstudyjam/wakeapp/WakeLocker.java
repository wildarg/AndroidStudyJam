package com.wild.androidstudyjam.wakeapp;/*
 * Created by Wild on 24.04.2015.
 */

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class WakeLocker {

    private static final String LOG_TAG = "#WakeLocker";
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context context, long timeout) {
        Log.d(LOG_TAG, "acquire");
        release();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(
                PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE
                , "WAKE_UP_TAG");
        wakeLock.acquire(timeout);
    }

    public static void release() {
        Log.d(LOG_TAG, "release");
        if (wakeLock != null) wakeLock.release();
        wakeLock = null;
    }

}
