package com.wild.test.thesmspost;/*
 * Created by Wild on 27.04.2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SmsListAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private Cursor data = null;

    public SmsListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (data != null)
            return data.getCount();
        return 0;
    }

    @Override
    public Cursor getItem(int position) {
        if (data.moveToPosition(position))
            return data;
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            v.setTag(new SmsViewHolder(v));
        }
        if (data.moveToPosition(position))
            bindView(v, data);
        return v;
    }

    private void bindView(View v, Cursor data) {
        SmsViewHolder h = (SmsViewHolder) v.getTag();
        h.header.setText(data.getString(0));
        h.text.setText(data.getString(1));
    }

    private class SmsViewHolder {
        TextView header;
        TextView text;
        public SmsViewHolder(View view) {
            header = (TextView) view.findViewById(android.R.id.text1);
            text = (TextView) view.findViewById(android.R.id.text2);
        }
    }

    public void swapCursor(Cursor data) {
        this.data = data;
        notifyDataSetChanged();
    }

}
