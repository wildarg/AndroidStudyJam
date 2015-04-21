package com.wild.androidstudyjam.weatherforecast2.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/*
 * Created by Wild on 20.04.2015.
 */
public class WeatherContentProvider extends ContentProvider {

    private static UriMatcher matcher;
    private static final int WEATHER_MATCH = 1;
    private static final int WEATHER_ITEM_MATCH = 2;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(WeatherProviderContract.AUTHORITY, WeatherProviderContract.WeatherInfo.WEATHER_INFO, WEATHER_MATCH);
        matcher.addURI(WeatherProviderContract.AUTHORITY, WeatherProviderContract.WeatherInfo.WEATHER_INFO + "/#", WEATHER_ITEM_MATCH);
    }

    SQLiteOpenHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String where = selection;
        Cursor c;
        switch (matcher.match(uri)) {
            case WEATHER_ITEM_MATCH: where = addIdCondition(uri.getLastPathSegment(), selection);
            case WEATHER_MATCH:
                c = db.query(WeatherProviderContract.WeatherInfo.WEATHER_INFO,
                    projection, where, selectionArgs, null, null, sortOrder);
                break;
            default: throw new IllegalArgumentException("Unknown uri " + uri);
        }
        c.setNotificationUri(getResolver(), uri);
        return c;
    }

    private String addIdCondition(String id, String selection) {
        String s = String.format("%s = %s", BaseColumns._ID, id);
        if (!TextUtils.isEmpty(selection))
            s = String.format("%s and (%s)", s, selection);
        return s;
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case WEATHER_MATCH: return "vnd.android.cursor.dir/vnd." +
                    WeatherProviderContract.AUTHORITY + "." + WeatherProviderContract.WeatherInfo.WEATHER_INFO;
            case WEATHER_ITEM_MATCH: return "vnd.android.cursor.item/vnd." +
                    WeatherProviderContract.AUTHORITY + "." + WeatherProviderContract.WeatherInfo.WEATHER_INFO;
            default: throw new IllegalArgumentException("Unknown uri " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (matcher.match(uri) != WEATHER_MATCH)
            throw new IllegalArgumentException("Unknown uri " + uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id = db.insert(WeatherProviderContract.WeatherInfo.WEATHER_INFO, null, values);
        getResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    private ContentResolver getResolver() {
        return getContext().getContentResolver();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = selection;
        int count;
        switch (matcher.match(uri)) {
            case WEATHER_ITEM_MATCH: where = addIdCondition(uri.getLastPathSegment(), where);
            case WEATHER_MATCH:
                count = db.delete(WeatherProviderContract.WeatherInfo.WEATHER_INFO, where, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Unknown uri " + uri);
        }
        if (count > 0)
            getResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = selection;
        int count;
        switch (matcher.match(uri)) {
            case WEATHER_ITEM_MATCH: where = addIdCondition(uri.getLastPathSegment(), where);
            case WEATHER_MATCH:
                count = db.update(WeatherProviderContract.WeatherInfo.WEATHER_INFO, values, where, selectionArgs);
                break;
            default: throw new IllegalArgumentException("Unknown uri " + uri);
        }
        if (count > 0)
            getResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        int count = 0;
        if (matcher.match(uri) != WEATHER_MATCH)
            throw new IllegalArgumentException("Unknown uri " + uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues cv: values) {
                long id = db.insert(WeatherProviderContract.WeatherInfo.WEATHER_INFO, null, cv);
                if (id <= 0)
                    throw new SQLException("Failed to insert row into " + uri);
            }
            db.setTransactionSuccessful();
            count = values.length;
            getResolver().notifyChange(uri, null);
        } finally {
            db.endTransaction();
        }
        return count;
    }

    private class DBHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "weather.db";
        private static final int DB_VERSION = 1;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION, null);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = String.format("create table %s(%s integer primary key autoincrement, %s text)",
                    WeatherProviderContract.WeatherInfo.WEATHER_INFO,
                    WeatherProviderContract.WeatherInfo._ID,
                    WeatherProviderContract.WeatherInfo.JSON);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = String.format("drop table if exists %s", WeatherProviderContract.WeatherInfo.WEATHER_INFO);
            db.execSQL(sql);
            onCreate(db);
        }
    }
    
}
