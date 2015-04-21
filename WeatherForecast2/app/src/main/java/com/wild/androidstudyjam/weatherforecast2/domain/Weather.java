package com.wild.androidstudyjam.weatherforecast2.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wild on 14.04.2015.
 */
public class Weather {
    public static final Weather EMPTY = new Weather();
    private static final String KEY_MAIN = "key_main";
    private static final String KEY_DESCRIPTION = "key_description";
    private static final String KEY_ICON = "key_icon";

    String main;
    String description;
    String icon;

    public Weather() { }

    public Weather(JSONObject json) {
        main = json.optString(KEY_MAIN);
        description = json.optString(KEY_DESCRIPTION);
        icon = json.optString(KEY_ICON);
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_MAIN, getMain());
        json.put(KEY_DESCRIPTION, getDescription());
        json.put(KEY_ICON, getIcon());
        return json;
    }
}
