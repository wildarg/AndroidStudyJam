package com.wild.androidstudyjam.weatherforecast.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wild on 14.04.2015.
 */
public class WeatherForecast {

    private static final String KEY_COD = "key_cod";
    private static final String KEY_CITY = "key_city";
    private static final String KEY_WEATHER_LIST = "key_weather_info_list";
    private int cod;
    private City city;
    List<WeatherInfo> list;

    public WeatherForecast() {
    }

    public int getCod() {
        return cod;
    }

    public City getCity() {
        if (city != null)
            return city;
        return City.EMPTY_CITY;
    }

    public List<WeatherInfo> getWeatherInfoList() {
        if (list != null)
            return list;
        return new ArrayList<WeatherInfo>();
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_COD, cod);
        json.put(KEY_CITY, getCity().toJSON());
        json.put(KEY_WEATHER_LIST, getWeatherInfoJSONArrayList());
        return json;
    }

    private JSONArray getWeatherInfoJSONArrayList() throws JSONException {
        JSONArray array = new JSONArray();
        for (WeatherInfo info: getWeatherInfoList()) {
            array.put(info.toJSON());
        }
        return array;
    }

    public WeatherForecast(JSONObject json) {
        cod = json.optInt(KEY_COD);
        city = new City(json.optJSONObject(KEY_CITY));
        loadList(json.optJSONArray(KEY_WEATHER_LIST));
    }

    private void loadList(JSONArray array) {
        list = new ArrayList<WeatherInfo>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new WeatherInfo(array.optJSONObject(i)));
        }
    }
}
