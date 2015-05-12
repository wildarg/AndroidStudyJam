package com.homesoftwaretools.portmone;/*
 * Created by Wild on 12.05.2015.
 */

import android.util.Log;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTestCase extends TestCase {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public void testConvertDate() throws ParseException {
        String s = "25.05.2015";
        Date date = sdf.parse(s);

        long time = date.getTime();
        Log.d("#DateTest", "time: " + time);
        Date convertDate = new Date(time);

        assertTrue(convertDate.equals(date));
        assertEquals(s, sdf.format(convertDate));
    }

}
