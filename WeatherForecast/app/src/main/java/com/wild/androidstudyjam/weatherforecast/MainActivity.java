package com.wild.androidstudyjam.weatherforecast;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.wild.androidstudyjam.weatherforecast.controller.WeatherForecastController;
import com.wild.androidstudyjam.weatherforecast.model.WeatherForecast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    private static final String KEY_FORECAST = "key_forecast";
    private static final String LOG_TAG = "#MainActivity";
    private WeatherForecastAdapter adapter;
    private WeatherForecastController controller;
    private WeatherForecast forecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new WeatherForecastAdapter(this);
        ListView weatherListView = (ListView) findViewById(R.id.weatherListView);
        weatherListView.setAdapter(adapter);

        controller = new WeatherForecastController();
        if (savedInstanceState == null)
            controller.refreshWeatherForecast(new ControllerCallback());
    }

    private class ControllerCallback implements Callback<WeatherForecast> {
        @Override
        public void success(WeatherForecast weatherForecast, Response response) {
            setForecast(weatherForecast);
            Toast.makeText(MainActivity.this, "Success refresh", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(MainActivity.this, "Error loading data. " + error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (forecast != null)
            storeForecast(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreForecast(savedInstanceState);
    }

    private void storeForecast(Bundle outState) {
        try {
            outState.putString(KEY_FORECAST, forecast.toJSON().toString());
            Log.d(LOG_TAG, "store forecast");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void restoreForecast(Bundle bundle) {
        try {
            JSONObject json = new JSONObject(bundle.getString(KEY_FORECAST));
            setForecast(new WeatherForecast(json));
            Log.d(LOG_TAG, "restore forecast");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setForecast(WeatherForecast forecast) {
        this.forecast = forecast;
        adapter.swapData(forecast);
    }
}
