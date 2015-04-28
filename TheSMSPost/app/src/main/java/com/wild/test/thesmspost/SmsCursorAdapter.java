package com.wild.test.thesmspost;/*
 * Created by Wild on 27.04.2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class SmsCursorAdapter extends CursorAdapter {

    public SmsCursorAdapter(Context context) {
        super(context, null, 0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        view.setTag(new SmsViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        SmsViewHolder h = (SmsViewHolder) view.getTag();
        h.header.setText(c.getString(0));
        h.text.setText(c.getString(1));
    }

    private class SmsViewHolder {
        TextView header;
        TextView text;
        public SmsViewHolder(View view) {
            header = (TextView) view.findViewById(android.R.id.text1);
            text = (TextView) view.findViewById(android.R.id.text2);
        }
    }
}
