package com.homesoftwaretools.portmone.activities;
/*
 * Created by Wild on 04.05.2015.
 */


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;;
import android.os.PersistableBundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.astuetz.PagerSlidingTabStrip;
import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.adapters.JournalsPageAdapter;
import com.homesoftwaretools.portmone.fragments.AbstractJournalFragment;
import com.homesoftwaretools.portmone.rest.ApiController;
import com.homesoftwaretools.portmone.security.AuthorithationManager;
import com.homesoftwaretools.portmone.tasks.RefreshFromServerTask;

public class JournalActivity extends AppCompatActivity {

    private JournalsPageAdapter adapter;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;

    public static void show(Context context) {
        Intent intent = new Intent(context, JournalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawer != null && drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AuthorithationManager.getInstance(this).isFirstEnter())
            new RefreshFromServerTask(this, ApiController.getInstance(this).getApi()).execute();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawer != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journals);
        getSupportActionBar().setElevation(0);
        setUpDrawer();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new JournalsPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
    }

    private void setUpDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.setDrawerShadow(R.drawable.slide_shadow, GravityCompat.START);
            drawerToggle = new ActionBarDrawerToggle(this, drawer, R.mipmap.ic_drawer, 0, 0);
            drawer.setDrawerListener(drawerToggle);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (drawer != null)
            drawerToggle.syncState();
    }

}
