package com.homesoftwaretools.portmone.utils;/*
 * Created by Wild on 07.05.2015.
 */

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleCursorAdapter;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

import java.util.ArrayList;
import java.util.List;

public class LibAutocompleteManager {

    private static final String[] FROM_FIELDS = {PortmoneContract.BaseLib.NAME};
    private static final int[] TO_VIEWS = {android.R.id.text1};

    private final Context context;
    private final LoaderManager loaderManager;
    private List<AutocompleteController> controllers = new ArrayList<>();

    public LibAutocompleteManager(Context context, LoaderManager loaderManager){
        this.context = context;
        this.loaderManager = loaderManager;
    }

    public LibAutocompleteManager addAutocompleteTextView(AutoCompleteTextView textView, Uri contentUri) {
        controllers.add(new AutocompleteController(textView, contentUri));
        return this;
    }

    public LibAutocompleteManager addMultiAutocompleteTextView(MultiAutoCompleteTextView textView, Uri contentUri,
                                             MultiAutoCompleteTextView.Tokenizer tokenizer) {
        controllers.add(new AutocompleteController(textView, contentUri, tokenizer));
        return this;
    }

    private class AutocompleteController implements LoaderManager.LoaderCallbacks<Cursor>{

        private final Uri libContentUri;
        private SimpleCursorAdapter adapter;

        public AutocompleteController(AutoCompleteTextView textView, final Uri libContentUri) {
            this.libContentUri = libContentUri;
            adapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, null, FROM_FIELDS, TO_VIEWS, 0);
            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence s) {
                    String where = PortmoneContract.BaseLib.NAME + " like ?";
                    String[] args = new String[]{s + "%"};
                    return context.getContentResolver().query(libContentUri, null, where, args, PortmoneContract.BaseLib.NAME);
                }
            });
            adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
                @Override
                public CharSequence convertToString(Cursor cursor) {
                    return cursor.getString(cursor.getColumnIndex(PortmoneContract.BaseLib.NAME));
                }
            });
            textView.setAdapter(adapter);
            loaderManager.initLoader(0, null, this);
        }

        public AutocompleteController(MultiAutoCompleteTextView textView, final Uri libContentUri,
                                      MultiAutoCompleteTextView.Tokenizer tokenizer) {
            this(textView, libContentUri);
            textView.setTokenizer(tokenizer);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(context, libContentUri, null, null, null, PortmoneContract.BaseLib.NAME);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            adapter.swapCursor(null);
        }
    }

}
