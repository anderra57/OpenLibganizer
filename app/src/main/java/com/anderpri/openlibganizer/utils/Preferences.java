package com.anderpri.openlibganizer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class Preferences {

    @SuppressLint("StaticFieldLeak")
    private static Preferences INSTANCE;
    private Context CONTEXT;

    public static Preferences getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Preferences();
        }
        return INSTANCE;
    }

    public void setContext(Context applicationContext) {
        CONTEXT = applicationContext;
    }

    // Fuente: https://www.tutorialspoint.com/android/android_session_management.htm
    // Fuente: https://stackoverflow.com/a/20678802

    public SharedPreferences getPreferences(){
        return CONTEXT.getSharedPreferences("session", 0);
    }

    public boolean checkUsernameNull() {
        return getPreferences().getString("username",null)==null;
    }

    public String getUsername() {
        return getPreferences().getString("username",null);
    }

    public String getLocale() { return getPreferences().getString("lang","en"); }

    public void setLocale(String lang){
        SharedPreferences pref = getPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lang",lang);
        editor.commit();
    }

    public boolean isFirst() {
        return getPreferences().getBoolean("notfirst",false);
    }

    public void login(String sUser) {
        SharedPreferences pref = getPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", sUser);
        editor.putBoolean("notfirst",true);
        editor.putString("lang",Utils.getInstance().getLocale(CONTEXT).toLowerCase(Locale.ROOT));
        editor.commit();
    }


    public void logout() {
        SharedPreferences pref = getPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
