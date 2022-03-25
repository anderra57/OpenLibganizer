package com.anderpri.openlibganizer.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anderpri.openlibganizer.R;
import com.anderpri.openlibganizer.db.AppDatabase;
import com.anderpri.openlibganizer.db.User;
import com.anderpri.openlibganizer.utils.Utils;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }



    public void onRadioButtonClicked(View view) {

        // Fuente: https://developer.android.com/guide/topics/ui/controls/radiobutton?hl=es-419

        boolean checked = ((RadioButton) view).isChecked();
        int idRadio = view.getId();
        TextView test_string = findViewById(R.id.test_string);

        if (idRadio == R.id.radio_en && checked) {
            Utils.setLocale(this, "en");
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(view.getContext(), "ENGLISH", Toast.LENGTH_SHORT).show();
        } else if (idRadio == R.id.radio_es && checked) {
            Utils.setLocale(this, "es");
            //https://developer.android.com/guide/topics/ui/look-and-feel/darktheme
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(view.getContext(), "SPANISH", Toast.LENGTH_SHORT).show();
        }

        test_string.setText(getString(R.string.test_string_locale)); // actualizar texto prueba

    }

    public void onButtonClicked(View view) {

        AppDatabase db  = AppDatabase.getInstance(this.getApplicationContext());

        String input_user = "b";
        String input_pass = "b";

        String lang = "es"; // default language, cambiar
        boolean theme = false; // default

        User user = new User(input_user,input_pass,lang,theme);
        db.userDao().insertUser(user);

    }

}