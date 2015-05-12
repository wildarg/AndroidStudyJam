package com.homesoftwaretools.portmone.domain;/*
 * Created by Wild on 09.05.2015.
 */

import android.content.ContentValues;

import com.homesoftwaretools.portmone.provider.PortmoneContract;

public class User {

    String id;
    String name;
    String email;
    String token;
    String password;
    String notes;

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(PortmoneContract.Users.NAME, name);
        values.put(PortmoneContract.Users.EMAIL, email);
        values.put(PortmoneContract.Users.TOKEN, token);
        values.put(PortmoneContract.Users.NOTES, notes);
        return values;
    }
}
