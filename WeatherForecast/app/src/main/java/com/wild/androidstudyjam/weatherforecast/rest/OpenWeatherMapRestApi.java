package com.wild.androidstudyjam.weatherforecast.rest;

import com.wild.androidstudyjam.weatherforecast.model.WeatherForecast;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Wild on 14.04.2015.
 */
public interface OpenWeatherMapRestApi {

    @GET("/data/2.5/forecast?q=Moscow,ru")
    public void getWeatherForecast(Callback<WeatherForecast> callback);

}
