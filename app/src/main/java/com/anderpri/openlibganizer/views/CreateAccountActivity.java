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

public class CreateAccountActivity extends AppCompatActivity {


    EditText mUserText;
    EditText mPassText;
    String mUser;
    String mPass;
    Button mButton;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivity();
    }

    private void setupActivity() {
        db = AppDatabase.getInstance(this.getApplicationContext());
        setContentView(R.layout.activity_create_account);

        mUserText = findViewById(R.id.create_username);
        mPassText = findViewById(R.id.create_password);
        mButton = findViewById(R.id.create_button);

        mButton.setOnClickListener(this::createAccount);
    }

    private void createAccount(View view) {
        mUser = mUserText.getText().toString();
        mPass = mPassText.getText().toString();
        addUser(view);
        returnToLogin();
    }

    private void returnToLogin() {
        // 1) Crear la sesión en SharedPreferences
        Preferences.getInstance().login(mUser);
        // 2) Entrar
        Intent i = new Intent(CreateAccountActivity.this, MainActivity.class);
        finish();
        startActivity(i);
    }

    private void addUser(View view) {
        if (!db.userDao().userExists(mUser)){
            Utils ut = Utils.getInstance();
            User user = new User(mUser, ut.get_SHA_512_SecurePassword(mPass),ut.getLocale(getApplicationContext()),false);
            db.userDao().insertUser(user);
        }  else { Toast.makeText(view.getContext(),R.string.login_incorrect_creds,Toast.LENGTH_SHORT).show(); }
    }
}
