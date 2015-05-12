package com.homesoftwaretools.portmone.provider;/*
 * Created by Wild on 07.05.2015.
 */

import android.database.Cursor;
import android.test.ProviderTestCase2;

import com.homesoftwaretools.portmone.domain.Tag;

public class FindTagsProviderTestCase extends ProviderTestCase2<PortmoneProvider> {

    public FindTagsProviderTestCase() {
        super(PortmoneProvider.class, PortmoneContract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Tag tag = new Tag("#зарплата");
        getMockContentResolver().insert(PortmoneContract.Tags.CONTENT_URI, tag.getValues());
        tag = new Tag("#кунцево");
        getMockContentResolver().insert(PortmoneContract.Tags.CONTENT_URI, tag.getValues());
    }

    public void testExistsTwoRecords() {
        Cursor c = getMockContentResolver().query(PortmoneContract.Tags.CONTENT_URI, null, null, null, null);
        assertNotNull(c);
        assertEquals(2, c.getCount());
        c.close();
    }

    public void testLikeWithArgs() {
        String where = PortmoneContract.Tags.NAME + " like ?";
        String[] args = new String[] {"#за%"};
        Cursor c = getMockContentResolver().query(PortmoneContract.Tags.CONTENT_URI, null, where, args, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.close();
    }
}
