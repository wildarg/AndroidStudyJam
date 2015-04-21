package com.wild.androidstudyjam.weatherforecast2.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Wild on 20.04.2015.
 */
public class WeatherProviderContract {

    public static final String AUTHORITY = "com.wild.androidstudyjam.weatherforecast2.provider.WeatherProvider";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static class WeatherInfo implements BaseColumns {

        public static final String WEATHER_INFO = "weather_info";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, WEATHER_INFO);
        public static final String JSON = "json";

        private WeatherInfo() {}
    }

}
