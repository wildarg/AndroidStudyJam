package com.wild.androidstudyjam.weatherforecast.controller;

import com.wild.androidstudyjam.weatherforecast.model.WeatherForecast;
import com.wild.androidstudyjam.weatherforecast.rest.OpenWeatherMapRestApi;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by Wild on 14.04.2015.
 */
public class WeatherForecastController {

    private OpenWeatherMapRestApi api;

    public WeatherForecastController() {
        setUp();
    }

    private void setUp() {
        RestAdapter a = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .build();
        api = a.create(OpenWeatherMapRestApi.class);
    }

    public void refreshWeatherForecast(Callback<WeatherForecast> callback) {
        api.getWeatherForecast(callback);
    }


}
