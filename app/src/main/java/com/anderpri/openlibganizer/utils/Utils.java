package com.anderpri.openlibganizer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class Utils {

    private static Utils INSTANCE;

    public static Utils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Utils();
        }
        return INSTANCE;
    }

    public SharedPreferences getPreferences(Context c) {
        return c.getSharedPreferences("session", 0);
    }

    // Fuente: https://www.c-sharpcorner.com/UploadFile/1e5156/how-to-change-locale-of-an-application-dynamically-in-androi/
    public void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        // deprecated!!!!
    }

    public String getLocale(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.locale.getLanguage();
    }

    // Fuente: https://stackoverflow.com/a/33085670
    public String get_SHA_512_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update("salt".getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
