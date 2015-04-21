package com.wild.androidstudyjam.weatherforecast2;/*
 * Created by Wild on 21.04.2015.
 */

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wild.androidstudyjam.weatherforecast2.domain.WeatherForecast;
import com.wild.androidstudyjam.weatherforecast2.domain.WeatherInfo;
import com.wild.androidstudyjam.weatherforecast2.provider.WeatherProviderContract;
import com.wild.androidstudyjam.weatherforecast2.utils.WeatherIconManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class DetailsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String EXTRA_ID = "extra_id";
    private static final int WEATHER_DETAIL_LOADER = 1;
    private long weatherId;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView tempTextView;
    private TextView iconTextView;
    private TextView weatherNameTextView;
    private TextView detailsTextView;

    private Typeface weatherFont;
    private final SimpleDateFormat date_format = new SimpleDateFormat("dd.MM.yyyy");
    private final SimpleDateFormat time_format = new SimpleDateFormat("HH:mm");
    private WeatherInfo weatherInfo;


    public static void show(Context context, long id) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_ID, id);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            weatherId = bundle.getLong(EXTRA_ID, 0);
        }

        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        iconTextView = (TextView) findViewById(R.id.iconTextView);
        tempTextView = (TextView) findViewById(R.id.tempTextView);
        weatherNameTextView = (TextView) findViewById(R.id.weatherNameTextView);
        detailsTextView = (TextView) findViewById(R.id.detailsTextView);

        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/webfont.ttf");
        iconTextView.setTypeface(weatherFont);

        findViewById(R.id.shareButton).setOnClickListener(new ShareButtonClickListener());

        getLoaderManager().initLoader(WEATHER_DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(WeatherProviderContract.WeatherInfo.CONTENT_URI, String.valueOf(weatherId));
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst())
            return;
        String json = data.getString(data.getColumnIndex(WeatherProviderContract.WeatherInfo.JSON));
        try {
            weatherInfo = new WeatherInfo(new JSONObject(json));
            populateWeatherInfo(weatherInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateWeatherInfo(WeatherInfo info) {
        dateTextView.setText(date_format.format(info.getDate()));
        timeTextView.setText(time_format.format(info.getDate()));
        iconTextView.setText(WeatherIconManager.getIconChar(info.getWeather().getIcon()));
        weatherNameTextView.setText(info.getWeather().getMain());
        String details = String.format("%s\nhumidity: %d\npressure: %.2f",
                info.getWeather().getDescription(),
                info.getWeatherMainData().getHumidity(),
                info.getWeatherMainData().getPressure());
        detailsTextView.setText(details);
        tempTextView.setText(info.getWeatherMainData().getTempInCelsius() + " °С");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class ShareButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String text = String.format("%s\n%s\n%s\ntemp: %d°С\nhumidity: %d\npressure: %.2f",
                    weatherInfo.getDtText(),
                    weatherInfo.getWeather().getMain(),
                    weatherInfo.getWeather().getDescription(),
                    weatherInfo.getWeatherMainData().getTempInCelsius(),
                    weatherInfo.getWeatherMainData().getHumidity(),
                    weatherInfo.getWeatherMainData().getPressure());
            shareText(text);
        }

    }

    private void shareText(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Share with..."));
        } else {
            Toast.makeText(this, "Sorry, no activity found for share text", Toast.LENGTH_SHORT).show();
        }
    }

}

