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
import com.homesoftwaretools.portmone.domain.Expense;
import com.homesoftwaretools.portmone.utils.HashtagUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ExpenseJournalAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM\nyyyy", Locale.getDefault());
    private final Context context;

    public ExpenseJournalAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = inflater.inflate(R.layout.expense_item, parent, false);
        v.setTag(new ExpenseViewHolder(v));
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ExpenseViewHolder h = (ExpenseViewHolder) view.getTag();
        h.populate(new Expense(cursor));
    }

    private class ExpenseViewHolder {
        private final View layout;
        private final TextView date;
        private final TextView expenseType;
        private final TextView description;
        private final TextView sum;
        private final TextView cashType;

        public ExpenseViewHolder(View v) {
            layout = v.findViewById(R.id.itemLayout);
            date = (TextView) v.findViewById(R.id.dateTextView);
            expenseType = (TextView) v.findViewById(R.id.expenseTypeTextView);
            description = (TextView) v.findViewById(R.id.descriptionTextView);
            sum = (TextView) v.findViewById(R.id.sumTextView);
            cashType = (TextView) v.findViewById(R.id.cashTypeTextView);
        }

        public void populate(Expense expense) {
            date.setText(sdf.format(expense.getDate()));
            expenseType.setText(expense.getExpenseType().getName());
            description.setText(expense.getDescription());
            HashtagUtils.addLinks(description);
            sum.setText(String.format("%,.2f", expense.getSum()));
            cashType.setText(expense.getCashType().getName());
            if (expense.isPlanned())
                layout.setBackground(context.getResources().getDrawable(R.drawable.item_background));
            else
                layout.setBackground(null);
        }
    }
}
