package com.wild.androidstudyjam.weatherforecast2.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wild.androidstudyjam.weatherforecast2.domain.WeatherForecast;
import com.wild.androidstudyjam.weatherforecast2.domain.WeatherInfo;
import com.wild.androidstudyjam.weatherforecast2.provider.WeatherProviderContract;
import com.wild.androidstudyjam.weatherforecast2.rest.OpenWeatherApi;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Wild on 20.04.2015.
 */
public class RefreshWeatherAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String LOG_TAG = "RefreshWeatherAsyncTask";
    private final OpenWeatherApi api;
    private final ContentResolver resolver;

    public RefreshWeatherAsyncTask(Context context, OpenWeatherApi api) {
        resolver = context.getContentResolver();
        this.api = api;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            WeatherForecast forecast = api.getWeatherForecast();
            Log.d(LOG_TAG, "success refresh");
            refreshContentProvider(forecast.getWeatherInfoList());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "error in refresh: " + e);
        }
        return null;
    }

    private void refreshContentProvider(List<WeatherInfo> list) throws JSONException {
        ContentValues[] values = new ContentValues[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = new ContentValues();
            String json = list.get(i).toJSON().toString();
            values[i].put(WeatherProviderContract.WeatherInfo.JSON, json);
        }
        resolver.delete(WeatherProviderContract.WeatherInfo.CONTENT_URI, null, null);
        resolver.bulkInsert(WeatherProviderContract.WeatherInfo.CONTENT_URI, values);
    }


}
