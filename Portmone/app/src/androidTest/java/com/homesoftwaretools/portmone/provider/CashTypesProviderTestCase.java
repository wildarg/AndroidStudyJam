package com.homesoftwaretools.portmone.provider;


/*
 * Created by Wild on 04.05.2015.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

public class CashTypesProviderTestCase extends ProviderTestCase2<PortmoneProvider> {

    private static final String CASH_NAME = "Наличные";
    private static final String BANK_NAME = "Банковский счет";
    private Uri cashUri;
    private Uri bankUri;

    public CashTypesProviderTestCase() {
        super(PortmoneProvider.class, PortmoneContract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cashUri = addCashType(CASH_NAME);
        bankUri = addCashType(BANK_NAME);
    }

    private Uri addCashType(String name) {
        return getResolver().insert(PortmoneContract.CashTypes.CONTENT_URI, newCashTypeValues(name));
    }

    private ContentResolver getResolver() {
        return getMockContentResolver();
    }

    public void testNotNullCursor() {
        Cursor c = getResolver().query(PortmoneContract.CashTypes.CONTENT_URI, null, null, null, null);
        assertNotNull("null cursor", c);
        assertTrue("empty cursor", c.moveToFirst());
        assertEquals("incorrect record count", 2, c.getCount());
        c.close();
    }

    public void testInsertCahsType() {
        ContentValues values = newCashTypeValues("Visa");
        Uri uri = getResolver().insert(PortmoneContract.CashTypes.CONTENT_URI, values);
        assertNotNull("null uri", uri);
        Long id = Long.valueOf(uri.getLastPathSegment());
        assertTrue("incorrect id", id >= 0);
    }

    public void testDeleteCash() {
        int cnt = getResolver().delete(cashUri, null, null);
        assertEquals(1, cnt);

        Cursor c = getResolver().query(cashUri, null, null, null, null);
        assertFalse(c.moveToFirst());
        c.close();
    }

    public void testSelectUri() {
        Cursor c = getResolver().query(bankUri, null, null, null, null);
        assertEquals(1, c.getCount());
        assertTrue(c.moveToFirst());
        assertEquals(BANK_NAME, c.getString(c.getColumnIndex(PortmoneContract.CashTypes.NAME)));
        c.close();
    }

    public void testDeleteAll() {
        int cnt = getResolver().delete(PortmoneContract.CashTypes.CONTENT_URI, null, null);
        assertEquals(2, cnt);

        Cursor c = getResolver().query(PortmoneContract.CashTypes.CONTENT_URI, null, null, null, null);
        assertFalse(c.moveToFirst());
        c.close();
    }

    public void testUpdateRecord() {
        String newName = "Депозит";
        ContentValues values = newCashTypeValues(newName);
        int cnt = getResolver().update(bankUri, values, null, null);
        assertEquals(1, cnt);

        Cursor c = getResolver().query(bankUri, null, null, null, null);
        assertTrue(c.moveToFirst());
        String name = c.getString(c.getColumnIndex(PortmoneContract.CashTypes.NAME));
        assertEquals(newName, name);
        c.close();
    }

    private ContentValues newCashTypeValues(String name) {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.CashTypes.NAME, name);
        return values;
    }

}
