package com.wild.androidstudyjam.wakeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {

    private static final String LOG_TAG = "#MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "create");
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_main);

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWakeUpService();
            }
        });

        findViewById(R.id.startActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.wild.androidstudyjam.wakeapp.WAKEUP");
                startActivity(intent);
            }
        });

        findViewById(R.id.wakeUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestTask().execute(Long.valueOf(40000));
            }
        });

        findViewById(R.id.wakeLockerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWakeLockerService();
            }
        });
    }

    private void startWakeLockerService() {
        Log.d(LOG_TAG, "start wakelocker service");
        startService(new Intent(getApplicationContext(), WakeLockerService.class));
    }

    private void startWakeUpService() {
        Intent service = new Intent(this, WakeUpService.class);
        startService(service);
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

    private class TestTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            long sleepTime = params[0];
            try {
                Log.d(LOG_TAG, "doInBackground sleep " + sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "interrup " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(LOG_TAG, "async task onPostExecute");
            wakeUp();
        }
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
