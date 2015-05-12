package com.homesoftwaretools.portmone.utils;/*
 * Created by Wild on 07.05.2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParamsManager {

    private static final String PREF_START_DATE = "pref_start_date";
    private static final String PREF_END_DATE = "pref_end_date";
    private static final String PREF_SHOW_PLANNED = "pref_show_planned";
    private static final String PREF_NOTES = "pref_notes";
    private static ParamsManager instance;
    private final SharedPreferences prefs;
    private boolean plannedVisible = false;
    private Date startDate = new Date();
    private Date endDate = new Date();

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        onChange();
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        onChange();
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isPlannedVisible() {
        return plannedVisible;
    }

    public interface Observer {
        void onParamsChange(ParamsManager manager);
    }

    private List<Observer> listeners = new ArrayList<>();

    public static ParamsManager getInstance(Context context) {
        if (instance == null)
            instance = new ParamsManager(context);
        return instance;
    }

    private ParamsManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        loadParams();
    }

    private void loadParams() {
        startDate = getPrefDate(PREF_START_DATE);
        endDate = getPrefDate(PREF_END_DATE);
        plannedVisible = prefs.getInt(PREF_SHOW_PLANNED, 0) == 1;
    }

    private Date getPrefDate(String keyName) {
        long time = prefs.getLong(keyName, 0);
        if (time == 0) return new Date();
        else return new Date(time);
    }

    public void registerObserver(Observer o) {
        if (!listeners.contains(o)) {
            listeners.add(o);
            o.onParamsChange(this);
        }
    }

    public void unregisterObserver(Observer o) {
        listeners.remove(o);
    }

    private void notifyListeners() {
        for (Observer o: listeners) {
            o.onParamsChange(this);
        }
    }

    public String[] getJournalSelectionArgs() {
        return new String[] {String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime()), plannedVisible?"1":"0"};
    }

    public void setPlannedVisible(boolean visible) {
        plannedVisible = visible;
        onChange();
    }

    private void onChange() {
        storeParams();
        notifyListeners();
    }

    private void storeParams() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREF_START_DATE, startDate.getTime());
        editor.putLong(PREF_END_DATE, endDate.getTime());
        editor.putInt(PREF_SHOW_PLANNED, plannedVisible?1:0);
        editor.apply();
    }
}
