package com.anderpri.openlibganizer;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class Utils {

    public static void setLocale(Context context, String lang) {

        // https://www.c-sharpcorner.com/UploadFile/1e5156/how-to-change-locale-of-an-application-dynamically-in-androi/

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        // deprecated!!!!

    }

}
