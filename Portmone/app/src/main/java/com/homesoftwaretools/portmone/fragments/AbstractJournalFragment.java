package com.homesoftwaretools.portmone.fragments;
/*
 * Created by Wild on 07.05.2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.utils.ParamsManager;

public abstract class AbstractJournalFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ADD_REQUEST = 1;
    private static final int EDIT_REQUEST = 2;
    private static final int JOURNAL_LOADER = 3;
    private CursorAdapter adapter;
    private View floatingButton;
    private Animation floatingAnimation;
    private Loader<Cursor> loader;
    private ParamsManager.Observer paramsObserver;
    private ListView listView;

    @Override
    public void onResume() {
        super.onResume();
        animateFloatingButton();
        ParamsManager.getInstance(getActivity()).registerObserver(paramsObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        ParamsManager.getInstance(getActivity()).unregisterObserver(paramsObserver);
    }

    public void animateFloatingButton() {
        floatingButton.startAnimation(floatingAnimation);
    }

    protected abstract void onDeleteActionExecute(long id);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.journal, container, false);
        listView = (ListView) v.findViewById(R.id.journalListView);
        adapter = createAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListItemClickListener());
        listView.setOnCreateContextMenuListener(new CreateContextMenuListener());

        floatingButton = v.findViewById(R.id.floatingButton);
        floatingButton.setOnClickListener(new FloatingButtonClickListener());
        floatingAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.floating_button_show);

        loader = getLoaderManager().initLoader(JOURNAL_LOADER, null, this);
        paramsObserver = new ParamsManager.Observer() {
            @Override
            public void onParamsChange(ParamsManager manager) {
                refreshJournal(manager.getJournalSelectionArgs());
            }
        };

        return v;
    }

    protected abstract CursorAdapter createAdapter();

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getJournalContentUri(), null, null,
                ParamsManager.getInstance(getActivity()).getJournalSelectionArgs(), PortmoneContract.Journal.TIMESTAMP);
    }

    protected abstract Uri getJournalContentUri();

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private class ListItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = getEditItemIntent(adapter.getCursor());
            startActivityForResult(intent, EDIT_REQUEST);
        }
    }

    protected abstract Intent getEditItemIntent(Cursor cursor);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST && resultCode == Activity.RESULT_OK) {
            onRecordAdd(data);
        } else if (requestCode == EDIT_REQUEST && resultCode == Activity.RESULT_OK) {
            onRecordUpdate(data);
        }
    }

    protected abstract void onRecordUpdate(Intent data);
    protected abstract void onRecordAdd(Intent data);

    private class FloatingButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = getAddRecordIntent();
            startActivityForResult(intent, ADD_REQUEST);
        }
    }

    protected abstract Intent getAddRecordIntent();

    private void refreshJournal(String[] args) {
        ((CursorLoader) loader).setSelectionArgs(args);
        loader.forceLoad();
    }

    private class CreateContextMenuListener implements View.OnCreateContextMenuListener {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final AdapterView.AdapterContextMenuInfo inf = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Cursor c = (Cursor) adapter.getItem(inf.position);
            final Intent editIntent = getEditItemIntent(c);
            MenuItem item = menu.add("Редактировать");
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivityForResult(editIntent, EDIT_REQUEST);
                    return true;
                }
            });
            item = menu.add("Удалить");
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onDeleteActionExecute(inf.id);
                    return true;
                }
            });
        }
    }
}
