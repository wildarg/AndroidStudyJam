package com.wild.test.thesmspost;/*
 * Created by Wild on 24.04.2015.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;


public class SMSService extends Service {

    private static final String LOG_TAG = "#SMSService";
    public static final String CHECK_STATE_BROADCAST = "com.wild.test.thesmspost.SMSService.CHECK_STATE";
    public static final String RESULT_BROADCAST = "com.wild.test.thesmspost.SMSService";
    public static final String EXTRA_TAG = "extra_tag";
    public static final String EXTRA_STATE = "extra_state";
    public static final int STATE_ACTIVE = 1;
    private BroadcastReceiver smsReceiver;
    private BroadcastReceiver checkReceiver;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(LOG_TAG, "on start");
        initializeSMSReceiver();
        registerSMSReceiver();

        registerCheckStateReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
        Log.d(LOG_TAG, "on start command, super res: " + res);
        return Service.START_STICKY_COMPATIBILITY;
    }

    private void registerCheckStateReceiver() {
        checkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(LOG_TAG, "receive broadcast check state");
                Intent result = new Intent(SMSService.RESULT_BROADCAST);
                result.putExtra(EXTRA_TAG, intent.getStringExtra(EXTRA_TAG));
                result.putExtra(EXTRA_STATE, STATE_ACTIVE);
                sendBroadcast(result);
            }
        };
        registerReceiver(checkReceiver, new IntentFilter(CHECK_STATE_BROADCAST));
        Log.d(LOG_TAG, "register check state receiver");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "on destroy");
        if (smsReceiver != null) {
            Log.d(LOG_TAG, "unregister sms receiver");
            unregisterReceiver(smsReceiver);
        }
        if (checkReceiver != null) {
            Log.d(LOG_TAG, "unregister check receiver");
            unregisterReceiver(checkReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerSMSReceiver() {
        Log.d(LOG_TAG, "register sms receiver");
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }

    private void initializeSMSReceiver(){
        Log.d(LOG_TAG, "initialize sms receiver");
        smsReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(LOG_TAG, "on receive");
                Bundle bundle = intent.getExtras();
                if(bundle!=null){
                    Object[] pdus = (Object[])bundle.get("pdus");
                    for(int i=0;i<pdus.length;i++){
                        byte[] pdu = (byte[])pdus[i];
                        SmsMessage message = SmsMessage.createFromPdu(pdu);
                        showSms(message);
                    }
                }

            }
        };
    }

    private void showSms(SmsMessage sms) {
        SMSPostActivity.showSMSPost(getApplicationContext(), new Date(sms.getTimestampMillis()),
                sms.getOriginatingAddress(), sms.getDisplayMessageBody());
    }

}
