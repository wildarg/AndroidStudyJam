package com.wild.androidstudyjam.weatherforecast2.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Wild on 14.04.2015.
 */
public class WeatherInfo {

    private static final String KEY_DT = "key_dt";
    private static final String KEY_DT_TEXT = "key_dt_text";
    private static final String KEY_MAIN = "key_main";
    private static final String KEY_WEATHER = "key_weather";
    long dt;
    String dt_text;
    WeatherMainData main;
    List<Weather> weather;

    public WeatherInfo() {}

    public WeatherInfo(JSONObject json) {
        dt = json.optLong(KEY_DT);
        dt_text = json.optString(KEY_DT_TEXT);
        main = new WeatherMainData(json.optJSONObject(KEY_MAIN));
        weather = new ArrayList<Weather>();
        weather.add(new Weather(json.optJSONObject(KEY_WEATHER)));
    }

    public String getDtText() {
        return dt_text;
    }

    public WeatherMainData getWeatherMainData() {
        if (main != null)
            return main;
        return WeatherMainData.EMPTY;
    }

    public Date getDate() {
        return new Date(dt * (long) 1000);
    }

    public Weather getWeather() {
        if (weather != null && weather.size() > 0)
            return weather.get(0);
        return Weather.EMPTY;
    }

    public boolean isNightTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dt * (long) 1000);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour > 20;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_DT, dt);
        json.put(KEY_DT_TEXT, dt_text);
        json.put(KEY_MAIN, getWeatherMainData().toJSON());
        json.put(KEY_WEATHER, getWeather().toJSON());
        return json;
    }
}
