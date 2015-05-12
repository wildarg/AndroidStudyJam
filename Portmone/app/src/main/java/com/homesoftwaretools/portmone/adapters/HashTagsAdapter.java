package com.homesoftwaretools.portmone.adapters;/*
 * Created by Wild on 07.05.2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class HashTagsAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    public HashTagsAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText(cursor.getString(cursor.getColumnIndex(PortmoneContract.Tags.NAME)));
    }

    @Override
    public String getItem(int position) {
        return "WILD";
    }
}
