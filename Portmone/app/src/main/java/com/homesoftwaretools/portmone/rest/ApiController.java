package com.homesoftwaretools.portmone.rest;/*
 * Created by Wild on 09.05.2015.
 */

import android.app.Activity;
import android.content.Context;

import com.homesoftwaretools.portmone.security.AuthorithationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;

public class ApiController {

    private static ApiController instance;
    private final Context context;
    private PortmoneApi api;

    public static ApiController getInstance(Context context) {
        if (instance == null)
            instance = new ApiController(context);
        return instance;
    }

    public ApiController(Context context) {
        this.context = context;
        api = setupApi();
    }

    private PortmoneApi setupApi() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Apikey", "86:5E:F0:53:05:48:34:2C:B4:70:B1:14:C2:83:AA:65:D4:D7:C2:4F;com.homesoftwaretools.test.portmoneapi");
                AuthorithationManager.getInstance(context).authorize(request);
            }
        };
        RestAdapter a = new RestAdapter.Builder()
                .setEndpoint("http://www.homesoftwaretools.com/portmone")
                .setRequestInterceptor(requestInterceptor)
                .build();
        return a.create(PortmoneApi.class);
    }


    public PortmoneApi getApi() {
        return api;
    }


    public static String getIdFromResponse(Response response) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(sb.toString());
            return json.optString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
