package com.homesoftwaretools.portmone.tasks;
/*
 * Created by Wild on 07.05.2015.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.homesoftwaretools.portmone.domain.CashType;
import com.homesoftwaretools.portmone.domain.Tag;
import com.homesoftwaretools.portmone.domain.Transfer;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.utils.HashtagUtils;

import java.util.List;

public class UpdateTransferAsyncTask extends ProgressAsyncTask<Transfer, Void, Void> {

    private final ContentResolver resolver;
    private final Uri uri;

    public UpdateTransferAsyncTask(Context context, Long id) {
        super(context);
        resolver = context.getContentResolver();
        uri = Uri.withAppendedPath(PortmoneContract.Transfers.CONTENT_URI, String.valueOf(id));
    }

    @Override
    protected Void doInBackground(Transfer... params) {
        Transfer transfer = params[0];
        String fromCashTypeName = transfer.getFromCashType().getName();
        String toCashTypeName = transfer.getToCashType().getName();
        transfer.setFromCashTypeId(getCashTypeByName(fromCashTypeName));
        transfer.setToCashType(getCashTypeByName(toCashTypeName));
        resolver.update(uri, transfer.getValues(), null, null);

        String where = PortmoneContract.TransferTags.TRANSFER_ID + " = ?";
        String[] args = new String[] {uri.getLastPathSegment()};
        resolver.delete(PortmoneContract.TransferTags.CONTENT_URI, where, args);

        List<String> tags = HashtagUtils.getTags(transfer.getDescription());
        for (String tagName: tags) {
            Tag tag = getTagByName(tagName);
            ContentValues values = new ContentValues();
            values.put(PortmoneContract.TransferTags.TRANSFER_ID, uri.getLastPathSegment());
            values.put(PortmoneContract.TransferTags.TAG_ID, tag.getId());
            resolver.insert(PortmoneContract.TransferTags.CONTENT_URI, values);
        }

        return null;
    }

    private CashType getCashTypeByName(String name) {
        CashType cashType;
        String where = PortmoneContract.CashTypes.NAME + "=?";
        String[] args = new String[] {name};
        Cursor c = resolver.query(PortmoneContract.CashTypes.CONTENT_URI, null, where, args, null);
        if (c.moveToFirst())
            cashType = new CashType(c);
        else {
            cashType = new CashType(name);
            Uri uri = resolver.insert(PortmoneContract.CashTypes.CONTENT_URI, cashType.getValues());
            cashType.setId(Long.valueOf(uri.getLastPathSegment()));
        }
        c.close();
        return cashType;
    }

    private Tag getTagByName(String name) {
        Tag tag;
        String where = PortmoneContract.Tags.NAME + "=?";
        String[] args = new String[] {name};
        Cursor c = resolver.query(PortmoneContract.Tags.CONTENT_URI, null, where, args, null);
        if (c.moveToFirst()) {
            tag = new Tag(c);
        } else {
            tag = new Tag(name);
            Uri uri = resolver.insert(PortmoneContract.Tags.CONTENT_URI, tag.getValues());
            tag.setId(Long.valueOf(uri.getLastPathSegment()));
        }
        c.close();
        return tag;
    }


}
