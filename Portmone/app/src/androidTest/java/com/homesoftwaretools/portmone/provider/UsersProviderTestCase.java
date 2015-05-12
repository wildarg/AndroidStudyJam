package com.homesoftwaretools.portmone.provider;/*
 * Created by Wild on 10.05.2015.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

public class UsersProviderTestCase extends ProviderTestCase2<PortmoneProvider> {

    public UsersProviderTestCase() {
        super(PortmoneProvider.class, PortmoneContract.AUTHORITY);
    }

    public void testEmptyCursor() {
        Cursor c = getMockContentResolver().query(PortmoneContract.Users.CONTENT_URI, null, null, null, null);
        assertNotNull(c);
        assertFalse(c.moveToFirst());
        assertTrue(c.getColumnIndex(PortmoneContract.Users._ID) >= 0);
        c.close();
    }

    public void testInsertWithOtherId() {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.Users._ID, 2505);
        values.put(PortmoneContract.Users.NAME, "Wild");
        Uri uri = getMockContentResolver().insert(PortmoneContract.Users.CONTENT_URI, values);
        long id = Long.valueOf(uri.getLastPathSegment());
        assertEquals(2505, id);
    }


}
