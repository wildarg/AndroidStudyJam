package com.wild.androidstudyjam.weatherforecast2.rest;


import com.wild.androidstudyjam.weatherforecast2.domain.WeatherForecast;

import retrofit.http.GET;

/**
 * Created by Wild on 20.04.2015.
 */
public interface OpenWeatherApi {

    @GET("/data/2.5/forecast?q=Moscow,ru")
    public WeatherForecast getWeatherForecast();

}
