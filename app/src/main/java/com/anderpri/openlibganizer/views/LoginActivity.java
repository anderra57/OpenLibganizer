package com.anderpri.openlibganizer.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anderpri.openlibganizer.R;
import com.anderpri.openlibganizer.db.AppDatabase;
import com.anderpri.openlibganizer.db.User;
import com.anderpri.openlibganizer.utils.Preferences;
import com.anderpri.openlibganizer.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    EditText mUserText;
    EditText mPassText;
    Button mButtonLogin;
    Button mButtonCreate;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(this.getApplicationContext());
        configureView();
    }

    private void configureView() {
        setContentView(R.layout.activity_login);

        mUserText = findViewById(R.id.login_username);
        mPassText = findViewById(R.id.login_password);
        mButtonLogin = findViewById(R.id.login_button);
        mButtonCreate = findViewById(R.id.login_create_account);

        mButtonLogin.setOnClickListener(this::checkLogin);
        mButtonCreate.setOnClickListener(this::openCreate);
    }

    private void openCreate(View view) {
        Intent i = new Intent(LoginActivity.this, CreateAccountActivity.class);
        finish();
        startActivity(i);
    }

    private void checkLogin(View view) {
        String sUser = mUserText.getText().toString();
        String sPass = Utils.getInstance().get_SHA_512_SecurePassword(mPassText.getText().toString());
        if (db.userDao().getUser(sUser, sPass) != null){ // si el usuario está en la base de datos...
            // 1) Crear la sesión en SharedPreferences
            Preferences.getInstance().login(sUser);
            // 2) Entrar al MainActivity
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        } else { Toast.makeText(view.getContext(),R.string.login_incorrect_creds,Toast.LENGTH_SHORT).show(); }

    }
}