package com.wild.androidstudyjam.simplesettingsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Wild on 17.04.2015.
 */
public class DetailActivity extends ActionBarActivity {

    private static final String EXTRA_WEATHER_DETAIL = "extra_weather_detail";

    public static void showDetails(Context context, String detail) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_WEATHER_DETAIL, detail);
        context.startActivity(intent);
    }

    private String detail;
    private TextView textView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                PrefActivity.show(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.helloTextView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detail = bundle.getString(EXTRA_WEATHER_DETAIL);
            setText(detail);
        }
    }

    private void setText(String detail) {
        textView.setText(detail);
    }
}