package com.homesoftwaretools.portmone.activities;/*
 * Created by Wild on 08.05.2015.
 */

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.adapters.JournalsPageAdapter;

public class TestActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private JournalsPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new JournalsPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);

    }
}
