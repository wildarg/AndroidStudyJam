package com.homesoftwaretools.portmone.adapters;
/*
 * Created by Wild on 07.05.2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.domain.Transfer;
import com.homesoftwaretools.portmone.utils.HashtagUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TransferJournalAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM\nyyyy", Locale.getDefault());
    private final Context context;

    public TransferJournalAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = inflater.inflate(R.layout.transfer_item, parent, false);
        v.setTag(new TransferViewHolder(v));
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TransferViewHolder h = (TransferViewHolder) view.getTag();
        h.populate(new Transfer(cursor));
    }

    private class TransferViewHolder {
        private final View layout;
        private final TextView date;
        private final TextView fromCashType;
        private final TextView description;
        private final TextView sum;
        private final TextView toCashType;

        public TransferViewHolder(View v) {
            layout = v.findViewById(R.id.itemLayout);
            date = (TextView) v.findViewById(R.id.dateTextView);
            fromCashType = (TextView) v.findViewById(R.id.fromCashTypeTextView);
            description = (TextView) v.findViewById(R.id.descriptionTextView);
            sum = (TextView) v.findViewById(R.id.sumTextView);
            toCashType = (TextView) v.findViewById(R.id.toCashTypeTextView);
        }

        public void populate(Transfer transfer) {
            date.setText(sdf.format(transfer.getDate()));
            fromCashType.setText(transfer.getFromCashType().getName());
            description.setText(transfer.getDescription());
            HashtagUtils.addLinks(description);
            sum.setText(String.format("%,.2f", transfer.getSum()));
            toCashType.setText(transfer.getToCashType().getName());
            if (transfer.isPlanned())
                layout.setBackground(context.getResources().getDrawable(R.drawable.item_background));
            else
                layout.setBackground(null);
        }
    }
}
