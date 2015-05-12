package com.homesoftwaretools.portmone.security;
/*
 * Created by Wild on 09.05.2015.
 */

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.homesoftwaretools.portmone.domain.User;
import com.homesoftwaretools.portmone.provider.PortmoneContract;
import com.homesoftwaretools.portmone.rest.ApiController;
import com.homesoftwaretools.portmone.rest.PortmoneApi;
import com.homesoftwaretools.portmone.tasks.RefreshFromServerTask;
import com.homesoftwaretools.portmone.tasks.UpdateProfileTask;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;

public class AuthorithationManager {

    private static final String PREF_TOKEN = "pref_token";
    private static AuthorithationManager instance;
    private final SharedPreferences pref;
    private final ContentResolver resolver;
    private final Context context;
    private String token;
    private boolean firstEnter = false;

    public static AuthorithationManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthorithationManager(context);
        }
        return instance;
    }

    public AuthorithationManager(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        resolver = context.getContentResolver();
        this.context = context;
        load();
    }

    private void load() {
        token = pref.getString(PREF_TOKEN, null);
    }

    public void setToken(String token) {
        this.token = token;
        save();
    }

    private void save() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return token;
    }

    public void authorize(RequestInterceptor.RequestFacade request) {
        if (!TextUtils.isEmpty(token))
            request.addHeader("Token", token);
    }

    public void login(User user) {
        setToken(user.getToken());
        UpdateProfileTask task = new UpdateProfileTask(context, user);
        task.execute();
        firstEnter = true;
    }

    public void logout() {
        setToken(null);
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(token);
    }

    public boolean isFirstEnter() {
        boolean res = firstEnter;
        firstEnter = false;
        return res;
    }
}
