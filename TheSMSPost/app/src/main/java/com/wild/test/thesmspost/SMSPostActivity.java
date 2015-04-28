package com.wild.test.thesmspost;/*
 * Created by Wild on 24.04.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSPostActivity extends Activity {

    private static final String EXTRA_DATE = "extra_date";
    private static final String EXTRA_AUTHOR_NAME = "extra_author_name";
    private static final String EXTRA_TEXT = "extra_text";
    private static final SimpleDateFormat sdf_date = new SimpleDateFormat("EEEE, dd MMMM, yyyy");
    private static final SimpleDateFormat sdf_weekday = new SimpleDateFormat("HH:mm");
    private static final String LOG_TAG = "#SMSPostActivity";
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView authorNameTextView;
    private TextView smsContentTextView;
    private ImageView photoImageView;
    private View paperLayout;
    private Animation paperAnimation;

    public static void showSMSPost(Context context, Date date, String authorName, String text, Uri photoUri) {
        Intent intent = new Intent(context, SMSPostActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_DATE, date.getTime());
        intent.putExtra(EXTRA_AUTHOR_NAME, authorName);
        intent.putExtra(EXTRA_TEXT, text);
        intent.setData(photoUri);
        context.startActivity(intent);
    }

    public static void showSMSPost(Context context, Date date, String phone, String text) {
        Intent intent = new Intent(context, SMSPostActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_DATE, date.getTime());
        intent.putExtra(EXTRA_TEXT, text);

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String[] projection = new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_URI};
        Cursor c =context.getContentResolver().query(uri, projection, null, null, null);
        if (c.moveToFirst()) {
            intent.putExtra(EXTRA_AUTHOR_NAME, c.getString(0));
            String uriString = c.getString(1);
            if (!TextUtils.isEmpty(uriString))
                intent.setData(Uri.parse(uriString));
        } else {
            intent.putExtra(EXTRA_AUTHOR_NAME, phone);
        }
        c.close();

        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_bkg);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        findViews();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            Date date = new Date(b.getLong(EXTRA_DATE));
            String authorName = b.getString(EXTRA_AUTHOR_NAME);
            String text = b.getString(EXTRA_TEXT);
            Uri uri = getIntent().getData();
            populate(date, authorName, text, uri);
        }

        wakeUp();
        paperAnimation = AnimationUtils.loadAnimation(this, R.anim.sms_post_rotate);

    }

    @Override
    protected void onResume() {
        super.onResume();
        paperLayout.startAnimation(paperAnimation);
    }

    private void wakeUp() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire(10000);
    }


    private void populate(Date date, String authorName, String text, Uri photoUri) {
        dateTextView.setText(sdf_date.format(date));
        timeTextView.setText(sdf_weekday.format(date));
        authorNameTextView.setText(authorName);
        smsContentTextView.setText(text);
        Log.d(LOG_TAG, "photo uri: " + photoUri);
        if (photoUri != null)
            photoImageView.setImageBitmap(toGrayscale(BitmapFactory.decodeStream(getBitmapStream(photoUri))));
    }

    private InputStream getBitmapStream(Uri photoUri) {
        try {
            AssetFileDescriptor fd =
                    getContentResolver().openAssetFileDescriptor(photoUri, "r");
            return fd.createInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    private void findViews() {
        paperLayout = findViewById(R.id.paperLayout);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        authorNameTextView = (TextView) findViewById(R.id.authorNameTextView);
        smsContentTextView = (TextView) findViewById(R.id.smsContentTextView);
        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        Typeface headlineFont = Typeface.createFromAsset(getAssets(), "fonts/Headlinetext.ttf");
        TextView headline = (TextView) findViewById(R.id.headerTextView);
        headline.setTypeface(headlineFont);
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        final int height = bmpOriginal.getHeight();
        final int width = bmpOriginal.getWidth();

        final Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas c = new Canvas(bmpGrayscale);
        final Paint paint = new Paint();
        final ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        final ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
}
