package com.homesoftwaretools.portmone.provider;/*
 * Created by Wild on 08.05.2015.
 */

import android.database.Cursor;
import android.test.ProviderTestCase2;

import java.util.Date;

public class BallanceReportProviderTestCase extends ProviderTestCase2<PortmoneProvider> {

    public BallanceReportProviderTestCase() {
        super(PortmoneProvider.class, PortmoneContract.AUTHORITY);
    }

    public void testNotNullCursor() {
        String[] args = {"0", String.valueOf(new Date().getTime()), "1"};
        Cursor c = getMockContentResolver().query(PortmoneContract.BallanceReport.CONTENT_URI, null, null, null, null);
        assertNotNull(c);
        c.close();
    }
}
