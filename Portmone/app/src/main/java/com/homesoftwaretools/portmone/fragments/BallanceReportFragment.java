package com.homesoftwaretools.portmone.fragments;/*
 * Created by Wild on 08.05.2015.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.adapters.BallanceReportAdapter;
import com.homesoftwaretools.portmone.adapters.SectionReportAdapter;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.utils.ParamsManager;

public class BallanceReportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SectionReportAdapter adapter;
    private ParamsManager paramsManager;
    private ParamsManager.Observer paramsObserver;
    private CursorLoader loader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.report, container, false);
        ListView list = (ListView) v.findViewById(R.id.reportListView);
        adapter = new SectionReportAdapter(getActivity());
        list.setAdapter(adapter);
        paramsManager = ParamsManager.getInstance(getActivity());
        loader = (CursorLoader) getLoaderManager().initLoader(0, null, this);
        paramsObserver = new ParamsManager.Observer() {
            @Override
            public void onParamsChange(ParamsManager manager) {
                refreshJournal(manager.getJournalSelectionArgs());
            }
        };
        return v;
    }

    private void refreshJournal(String[] journalSelectionArgs) {
//        adapter.swapCursor(null);
        loader.setSelectionArgs(journalSelectionArgs);
        loader.forceLoad();
    }

    @Override
    public void onResume() {
        super.onResume();
        ParamsManager.getInstance(getActivity()).registerObserver(paramsObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        ParamsManager.getInstance(getActivity()).unregisterObserver(paramsObserver);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), PortmoneContract.BallanceReport.CONTENT_URI, null, null,
                paramsManager.getJournalSelectionArgs(), null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("#BallanceReport", "reset loader");
        adapter.swapCursor(null);
    }


}
