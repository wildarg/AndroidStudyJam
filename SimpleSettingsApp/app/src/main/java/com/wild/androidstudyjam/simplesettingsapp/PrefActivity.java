package com.wild.androidstudyjam.simplesettingsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Wild on 17.04.2015.
 */
public class PrefActivity extends Activity {

    public static void show(Context context) {
        Intent intent = new Intent(context, PrefActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_fragment);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new PrefFragment())
                    .commit();
        }
    }

}
