package com.wild.androidstudyjam.weatherforecast2.controllers;/*
 * Created by Wild on 21.04.2015.
 */

import android.content.Context;

import com.wild.androidstudyjam.weatherforecast2.rest.OpenWeatherApi;
import com.wild.androidstudyjam.weatherforecast2.tasks.RefreshWeatherAsyncTask;

import retrofit.RestAdapter;

public class WeatherForecastController {
    private final Context context;
    private OpenWeatherApi api;

    public WeatherForecastController(Context context) {
        this.context = context;
        setUp();
    }

    private void setUp() {
        RestAdapter a = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .build();
        api = a.create(OpenWeatherApi.class);
    }

    public void refreshWeatherForecast() {
        new RefreshWeatherAsyncTask(context, api).execute();
    }


}
