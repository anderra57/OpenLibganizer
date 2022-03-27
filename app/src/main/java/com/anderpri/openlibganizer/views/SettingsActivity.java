package com.anderpri.openlibganizer.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.anderpri.openlibganizer.R;
import com.anderpri.openlibganizer.utils.Preferences;
import com.anderpri.openlibganizer.utils.Utils;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setParams();

    }

    private void setParams() {

        System.out.println(Utils.getInstance().getLocale(getApplicationContext()).toLowerCase(Locale.ROOT));

        if(Utils.getInstance().getLocale(getApplicationContext()).toLowerCase(Locale.ROOT).equals("es")){
            RadioButton radioButtonEs = findViewById(R.id.lang_radio_es);
            radioButtonEs.setChecked(true);
        }else{
            RadioButton radioButtonEn = findViewById(R.id.lang_radio_en);
            radioButtonEn.setChecked(true);
        }
    }


    public void onRadioButtonClicked(View view) {

        // Fuente: https://developer.android.com/guide/topics/ui/controls/radiobutton?hl=es-419

        Utils ut = Utils.getInstance();
        Preferences pref = Preferences.getInstance();

        boolean checked = ((RadioButton) view).isChecked();
        int idRadio = view.getId();

        String lang = null;

        if (idRadio == R.id.lang_radio_en && checked) {  lang = "en"; }
        else if (idRadio == R.id.lang_radio_es && checked) { lang = "es"; }

        ut.setLocale(this, lang);
        pref.setLocale(lang);
        Toast.makeText(view.getContext(), R.string.lang_change, Toast.LENGTH_SHORT).show();

        //https://developer.android.com/guide/topics/ui/look-and-feel/darktheme
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        //test_string.setText(getString(R.string.test_string_locale)); // actualizar texto prueba

    }

    public void onButtonClicked(View view) {

        // Cerrar la sesión
        Preferences.getInstance().logout();
        // Terminar MainActivity
        Intent intent = new Intent("finish_activity");
        sendBroadcast(intent);
        System.out.println("paro");
        // Terminar SettingsActivity y lanzar LoginActivity
        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
        finish();
        startActivity(i);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("SET CLOSED");
    }

}