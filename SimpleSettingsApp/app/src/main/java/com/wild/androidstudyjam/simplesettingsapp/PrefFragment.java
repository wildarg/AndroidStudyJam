package com.wild.androidstudyjam.simplesettingsapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Wild on 17.04.2015.
 */
public class PrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}