package com.homesoftwaretools.portmone.fragments;
/*
 * Created by Wild on 08.05.2015.
 */

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.homesoftwaretools.portmone.DatePickerFragment;
import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.StartActivity;
import com.homesoftwaretools.portmone.domain.User;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.rest.ApiController;
import com.homesoftwaretools.portmone.security.AuthorithationManager;
import com.homesoftwaretools.portmone.tasks.RefreshFromServerTask;
import com.homesoftwaretools.portmone.utils.FormatUtils;
import com.homesoftwaretools.portmone.utils.ParamsManager;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class SlideLeftFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView startDate;
    private TextView endDate;
    private ParamsManager paramsManager;
    private TextView personTextView;
    private TextView emailTextView;
    private TextView notesTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer_layout, container, false);
        paramsManager = ParamsManager.getInstance(getActivity());
        startDate = (TextView) v.findViewById(R.id.startDateTextView);
        endDate = (TextView) v.findViewById(R.id.endDateTextView);
        startDate.setText(FormatUtils.DRAWER_DATE_FORMAT.format(paramsManager.getStartDate()));
        endDate.setText(FormatUtils.DRAWER_DATE_FORMAT.format(paramsManager.getEndDate()));
        DateClickListener dateClickListener = new DateClickListener();
        startDate.setOnClickListener(dateClickListener);
        endDate.setOnClickListener(dateClickListener);
        CheckBox plannedCheckBox = (CheckBox) v.findViewById(R.id.plannedCheckBox);
        plannedCheckBox.setChecked(paramsManager.isPlannedVisible());
        plannedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                paramsManager.setPlannedVisible(isChecked);
            }
        });
        personTextView = (TextView) v.findViewById(R.id.personNameTextView);
        emailTextView = (TextView) v.findViewById(R.id.emailTextView);
        notesTextView = (TextView) v.findViewById(R.id.notesTextView);
        v.findViewById(R.id.logoutButton).setOnClickListener(new LogoutButtonClickListener());
        v.findViewById(R.id.notesEditButton).setOnClickListener(new NotesEditClickListener());
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), PortmoneContract.Users.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            personTextView.setText(data.getString(data.getColumnIndex(PortmoneContract.Users.NAME)));
            emailTextView.setText(data.getString(data.getColumnIndex(PortmoneContract.Users.EMAIL)));
            notesTextView.setText(data.getString(data.getColumnIndex(PortmoneContract.Users.NOTES)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class DateClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            Date d;
            try {
                d = FormatUtils.DRAWER_DATE_FORMAT.parse( ((TextView) v).getText().toString() );
            } catch (ParseException e) {
                e.printStackTrace();
                d = new Date();
            }
            DatePickerFragment.show(getFragmentManager(), d, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    final Calendar c = Calendar.getInstance();
                    c.set(year, monthOfYear, dayOfMonth);
                    ((TextView) v).setText(FormatUtils.DRAWER_DATE_FORMAT.format(c.getTime()));
                    switch (v.getId()) {
                        case R.id.startDateTextView:
                            paramsManager.setStartDate(c.getTime());
                            break;
                        case R.id.endDateTextView:
                            paramsManager.setEndDate(c.getTime());
                            break;
                    }
                }
            });
        }
    }

    private class LogoutButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AuthorithationManager manager = AuthorithationManager.getInstance(getActivity());
            manager.logout();
            StartActivity.show(getActivity());
            getActivity().finish();
        }
    }

    private class NotesEditClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DialogFragment dlg = new NotesEditorDialogFragment();
            Bundle args = new Bundle();
            args.putString(NotesEditorDialogFragment.EXTRA_NOTES, notesTextView.getText().toString());
            dlg.setArguments(args);
            dlg.show(getFragmentManager(), null);
        }
    }
}
