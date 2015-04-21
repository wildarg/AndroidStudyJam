package com.wild.androidstudyjam.weatherforecast2;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wild.androidstudyjam.weatherforecast2.adapters.WeatherCursorAdapter;
import com.wild.androidstudyjam.weatherforecast2.controllers.WeatherForecastController;
import com.wild.androidstudyjam.weatherforecast2.provider.WeatherProviderContract;
import com.wild.androidstudyjam.weatherforecast2.rest.OpenWeatherApi;
import com.wild.androidstudyjam.weatherforecast2.tasks.RefreshWeatherAsyncTask;

import retrofit.RestAdapter;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WEATHER_LOADER = 1;
    private WeatherCursorAdapter adapter;
    private WeatherForecastController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.weatherListView);
        adapter = new WeatherCursorAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new WeatherItemClickListener());
        getLoaderManager().initLoader(WEATHER_LOADER, null, this);
        controller = new WeatherForecastController(this);
        if (savedInstanceState == null) {
            controller.refreshWeatherForecast();
        }
        findViewById(R.id.refreshButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.refreshWeatherForecast();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, WeatherProviderContract.WeatherInfo.CONTENT_URI, null, null, null, BaseColumns._ID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private class WeatherItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DetailsActivity.show(MainActivity.this, id);
        }
    }
}
