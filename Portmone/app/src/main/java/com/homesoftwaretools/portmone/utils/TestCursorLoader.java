package com.homesoftwaretools.portmone.utils;
/*
 * Created by Wild on 08.05.2015.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

public class TestCursorLoader extends CursorLoader {
    public TestCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public void refreshSelectionArgs(String[] args) {
        setSelectionArgs(args);
        forceLoad();
    }

}
