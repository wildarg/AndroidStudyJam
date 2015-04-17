package com.wild.androidstudyjam.weatherforecast.model;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wild on 14.04.2015.
 */
public class City {
    public static final City EMPTY_CITY = new City();
    private static final String KEY_NAME = "key_name";
    private String name;

    public String getName() {
        return name;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_NAME, name);
        return json;
    }

    public City() { }

    public City(JSONObject json) {
        name = json.optString(KEY_NAME);
    }
}
