package com.homesoftwaretools.portmone;/*
 * Created by Wild on 04.05.2015.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    private Date date;

    public static void show(FragmentManager manager, Date currentDate, DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment f = new DatePickerFragment();
        f.setDate(currentDate);
        f.setListener(listener);
        f.show(manager, "");
    }

    private DatePickerDialog.OnDateSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
