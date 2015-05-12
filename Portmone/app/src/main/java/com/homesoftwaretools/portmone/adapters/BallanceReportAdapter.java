package com.homesoftwaretools.portmone.adapters;/*
 * Created by Wild on 08.05.2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.domain.Ballance;
import com.homesoftwaretools.portmone.utils.FormatUtils;
import com.homesoftwaretools.portmone.utils.ParamsManager;

import java.util.Date;

public class BallanceReportAdapter extends CursorAdapter {

    private static final int SECTION_TYPE = 1;
    private static final int ITEM_TYPE = 2;
    private final LayoutInflater inflater;
    private static final String DESCRIPTION_TEMPLATE = "Доход: <b>%,.2f</b>, Расход: <b>%,.2f</b>\n Зачислено: <b>%,.2f</b>, Списано: <b>%,.2f</b>";
    private final Context context;

    public BallanceReportAdapter(Context context) {
        super(context, null, 0);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = null;
        switch (getViewType(cursor)) {
            case SECTION_TYPE:
                v = inflater.inflate(R.layout.report_section, parent, false);
                v.setTag(new SectionHolder(v));
                break;
            case ITEM_TYPE:
                v = inflater.inflate(R.layout.ballance_report_item, parent, false);
                v.setTag(new ViewHolder(v));
                break;
        }
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int gr = cursor.getInt(cursor.getColumnIndex("gr"));
        try {
            switch (getViewType(cursor)) {
                case SECTION_TYPE:
                    SectionHolder sh = (SectionHolder) view.getTag();
                    sh.populate(cursor);
                    break;
                case ITEM_TYPE:
                    ViewHolder h = (ViewHolder) view.getTag();
                    h.populate(cursor);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("#ReportAdapter", "gr: " + gr + "; " + e);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Cursor c = (Cursor) getItem(position);
        return getViewType(c);
    }

    int getViewType(Cursor c) {
        if (c.getLong(c.getColumnIndex("_id")) < 0)
            return SECTION_TYPE;
        return ITEM_TYPE;
    }

    private class ViewHolder {
        private static final String template = "%s: <b>%,.2f</b>; ";
        TextView cashType;
        TextView sum;
        TextView description;

        public ViewHolder(View v) {
            cashType = (TextView) v.findViewById(R.id.cashTypeTextView);
            description = (TextView) v.findViewById(R.id.descriptionTextView);
            sum = (TextView) v.findViewById(R.id.sumTextView);
        }

        public void populate(Cursor c) {
            int gr = c.getInt(c.getColumnIndex("gr"));
            Ballance b = new Ballance(c);
            cashType.setText(b.getName());
            if (gr == 11) {
                sum.setText(String.format("%,.2f", b.getBallance()));
                StringBuilder sb = new StringBuilder();
                if (b.getIncome() != 0) sb.append(String.format(template, "Доход", b.getIncome()));
                if (b.getExpense() != 0)
                    sb.append(String.format(template, "Расход", b.getExpense()));
                if (b.getInTransfer() != 0)
                    sb.append(String.format(template, "Зачислено", b.getInTransfer()));
                if (b.getOutTransfer() != 0)
                    sb.append(String.format(template, "Списано", b.getOutTransfer()));
                description.setText(Html.fromHtml(sb.toString()));
            } else if (gr > 11) {
                sum.setText(String.format("%,.2f", b.getIncome()));
                description.setVisibility(View.GONE);
            }
        }
    }

    private class SectionHolder {
        TextView section;
        TextView sum;
        TextView description;

        public SectionHolder(View v) {
            section = (TextView) v.findViewById(R.id.sectionTextView);
            description = (TextView) v.findViewById(R.id.descriptionTextView);
            sum = (TextView) v.findViewById(R.id.sumTextView);
        }

        public void populate(Cursor c) {
            Ballance b = new Ballance(c);
            section.setText(b.getName());
            sum.setText(String.format("%,.2f", b.getIncome()));
            String s = FormatUtils.DRAWER_DATE_FORMAT.format(ParamsManager.getInstance(context).getStartDate());
            s += " - " + FormatUtils.DRAWER_DATE_FORMAT.format(ParamsManager.getInstance(context).getEndDate());
            description.setText(s);
        }
    }
}
