package com.wild.androidstudyjam.weatherforecast2.adapters;/*
 * Created by Wild on 21.04.2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.wild.androidstudyjam.weatherforecast2.R;
import com.wild.androidstudyjam.weatherforecast2.domain.WeatherInfo;
import com.wild.androidstudyjam.weatherforecast2.provider.WeatherProviderContract;
import com.wild.androidstudyjam.weatherforecast2.utils.WeatherIconManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class WeatherCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private Typeface weatherFont;
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm, dd.MM.yyyy");

    public WeatherCursorAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
        weatherFont = Typeface.createFromAsset(context.getAssets(), "fonts/webfont.ttf");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = inflater.inflate(R.layout.weather_item_2, parent, false);
        WeatherViewHolder h = new WeatherViewHolder(v);
        v.setTag(h);
        h.iconTextView.setTypeface(weatherFont);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        WeatherViewHolder h = (WeatherViewHolder) view.getTag();
        String json = c.getString(c.getColumnIndex(WeatherProviderContract.WeatherInfo.JSON));
        try {
            WeatherInfo info = new WeatherInfo(new JSONObject(json));
            h.populate(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class WeatherViewHolder {
        TextView timeTextView;
        TextView tempTextView;
        TextView mainTextView;
        TextView descriptionTextView;
        TextView humidityTextView;
        TextView pressureTextView;
        TextView iconTextView;

        public WeatherViewHolder(View v) {
            timeTextView = (TextView) v.findViewById(R.id.timeTextView);
            tempTextView = (TextView) v.findViewById(R.id.tempTextView);
            mainTextView = (TextView) v.findViewById(R.id.mainTextView);
            descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
            pressureTextView = (TextView) v.findViewById(R.id.pressureTextView);
            humidityTextView = (TextView) v.findViewById(R.id.humidityTextView);
            iconTextView = (TextView) v.findViewById(R.id.iconTextView);
            iconTextView.setTypeface(weatherFont);
        }

        public void populate(WeatherInfo info) {
            timeTextView.setText(df.format(info.getDate()));
            iconTextView.setText( WeatherIconManager.getIconChar(info.getWeather().getIcon()) );
            mainTextView.setText(info.getWeather().getMain());
            descriptionTextView.setText(info.getWeather().getDescription());

            tempTextView.setText(info.getWeatherMainData().getTempInCelsius() + "°С");
            pressureTextView.setText("pressure:" + info.getWeatherMainData().getPressure());
            humidityTextView.setText("humidity:" + info.getWeatherMainData().getHumidity() + "%");
        }
    }
}
