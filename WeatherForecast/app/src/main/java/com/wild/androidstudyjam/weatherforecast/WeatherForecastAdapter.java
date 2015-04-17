package com.wild.androidstudyjam.weatherforecast;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wild.androidstudyjam.weatherforecast.model.WeatherForecast;
import com.wild.androidstudyjam.weatherforecast.model.WeatherInfo;
import com.wild.androidstudyjam.weatherforecast.utils.WeatherIconManager;


import java.text.SimpleDateFormat;

/**
 * Created by Wild on 14.04.2015.
 */
public class WeatherForecastAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final Context context;
    private WeatherForecast forecast;
    private Typeface weatherFont;
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm, dd.MM.yyyy");

    public WeatherForecastAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        setUpFont();
    }

    private void setUpFont() {
        weatherFont = Typeface.createFromAsset(context.getAssets(), "fonts/webfont.ttf");
    }

    @Override
    public int getCount() {
        if (forecast != null)
            return forecast.getWeatherInfoList().size();
        return 0;
    }

    @Override
    public WeatherInfo getItem(int position) {
        WeatherInfo info = forecast.getWeatherInfoList().get(position);
        return info;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = createView(parent);
        bindView(view, getItem(position));
        return view;
    }

    private void bindView(View view, WeatherInfo item) {
        WeatherItemViewHolder h = (WeatherItemViewHolder) view.getTag();
        h.populate(item);
    }

    private View createView(ViewGroup parent) {
        View v = inflater.inflate(R.layout.weather_item_2, parent, false);
        v.setTag(new WeatherItemViewHolder(v));
        return v;
    }

    public void swapData(WeatherForecast forecast) {
        this.forecast = forecast;
        notifyDataSetChanged();  // уведомляем адаптер об изменении данных
    }

    private class WeatherItemViewHolder {
        TextView timeTextView;
        TextView tempTextView;
        TextView mainTextView;
        TextView descriptionTextView;
        TextView humidityTextView;
        TextView pressureTextView;
        TextView iconTextView;
        View layout;

        public WeatherItemViewHolder(View v) {
            timeTextView = (TextView) v.findViewById(R.id.timeTextView);
            tempTextView = (TextView) v.findViewById(R.id.tempTextView);
            mainTextView = (TextView) v.findViewById(R.id.mainTextView);
            descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
            pressureTextView = (TextView) v.findViewById(R.id.pressureTextView);
            humidityTextView = (TextView) v.findViewById(R.id.humidityTextView);
            iconTextView = (TextView) v.findViewById(R.id.iconTextView);
            iconTextView.setTypeface(weatherFont);
            layout = v.findViewById(R.id.itemLayout);
        }

        public void populate(WeatherInfo info) {
            timeTextView.setText(df.format(info.getDate()));
            iconTextView.setText( WeatherIconManager.getIconChar(info.getWeather().getIcon()) );
            mainTextView.setText(info.getWeather().getMain());
            descriptionTextView.setText(info.getWeather().getDescription());

            tempTextView.setText(info.getWeatherMainData().getTempInCelsius() + "°С");
            pressureTextView.setText("pressure:" + info.getWeatherMainData().getPressure());
            humidityTextView.setText("humidity:" + info.getWeatherMainData().getHumidity() + "%");

            if (info.isNightTime())
                layout.setBackground(context.getResources().getDrawable(R.drawable.night_background));
            else
                layout.setBackground(context.getResources().getDrawable(R.drawable.day_background));
        }
    }
}
