package com.homesoftwaretools.portmone.adapters;
/*
 * Created by Wild on 06.05.2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.domain.Income;
import com.homesoftwaretools.portmone.utils.HashtagUtils;

import java.text.SimpleDateFormat;

public class IncomeJournalAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM\nyyyy");
    private final Context context;

    public IncomeJournalAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = inflater.inflate(R.layout.income_item, parent, false);
        v.setTag(new IncomeViewHolder(v));
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        IncomeViewHolder h = (IncomeViewHolder) view.getTag();
        h.populate(new Income(cursor));
    }

    private class IncomeViewHolder {
        private final View layout;
        private final TextView date;
        private final TextView incomeType;
        private final TextView description;
        private final TextView sum;
        private final TextView cashType;

        public IncomeViewHolder(View v) {
            layout = v.findViewById(R.id.itemLayout);
            date = (TextView) v.findViewById(R.id.dateTextView);
            incomeType = (TextView) v.findViewById(R.id.incomeTypeTextView);
            description = (TextView) v.findViewById(R.id.descriptionTextView);
            sum = (TextView) v.findViewById(R.id.sumTextView);
            cashType = (TextView) v.findViewById(R.id.cashTypeTextView);
        }

        public void populate(Income income) {
            date.setText(sdf.format(income.getDate()));
            incomeType.setText(income.getIncomeType().getName());
            description.setText(income.getDescription());
            HashtagUtils.addLinks(description);
            sum.setText(String.format("%,.2f", income.getSum()));
            cashType.setText(income.getCashType().getName());
            if (income.isPlanned())
                layout.setBackground(context.getResources().getDrawable(R.drawable.item_background));
            else
                layout.setBackground(null);
        }
    }
}
