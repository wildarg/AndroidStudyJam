package com.wild.androidstudyjam.weatherforecast.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wild on 14.04.2015.
 */
public class WeatherMainData {

    public static final WeatherMainData EMPTY = new WeatherMainData();
    private static final String KEY_TEMP = "key_temp";
    private static final String KEY_HUMIDITY = "key_humidity";
    private static final String KEY_PRESSURE = "key_pressure";
    double temp;
    int humidity;
    float pressure;

    public WeatherMainData() { }

    public WeatherMainData(JSONObject json) {
        temp = json.optDouble(KEY_TEMP);
        humidity = json.optInt(KEY_HUMIDITY);
        pressure = (float) json.optDouble(KEY_PRESSURE);
    }

    public double getTemp() {
        return temp;
    }

    public int getTempInCelsius() {
        return (int)(temp - 273.15);
    }

    public int getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_TEMP, getTemp());
        json.put(KEY_HUMIDITY, getHumidity());
        json.put(KEY_PRESSURE, getPressure());
        return json;
    }
}
