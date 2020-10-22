package com.example.pelaporanbakesbangpolmalang.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionHelper {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;

    public static final String PREF_NAME = "LOGIN";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";
    public static final String ID_USER = "ID_USER";
    public static final String LEVEL = "LEVEL";

    public SessionHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String email, String username, String id_user, String level) {
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putString(EMAIL, email);
        editor.putString(USERNAME, username);
        editor.putString(ID_USER, id_user);
        editor.putString(LEVEL, level);
        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN_STATUS, false);
    }

    public boolean logout(){
        editor.clear();
        editor.commit();

        return true;
    }

    public String getEMAIL() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public String getUsername() {
        return sharedPreferences.getString(USERNAME, null);
    }

    public String getIdUser() {
        return sharedPreferences.getString(ID_USER, null);
    }

    public String getLevel() {
        return sharedPreferences.getString(LEVEL, null);
    }
}
