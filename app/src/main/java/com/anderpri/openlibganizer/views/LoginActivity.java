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
import com.anderpri.openlibganizer.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    EditText mUserText;
    EditText mPassText;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocale(this,"en");
        
        addUser();

        setContentView(R.layout.activity_login);

        mUserText = findViewById(R.id.login_username);
        mPassText = findViewById(R.id.login_password);
        mButton = findViewById(R.id.login_button);

        mButton.setOnClickListener(view -> checkLogin(view));

    }

    private void addUser() {
        AppDatabase db  = AppDatabase.getInstance(this.getApplicationContext());

        String input_user = "a";
        String input_pass = "a";

        String lang = "es"; // default language, cambiar
        boolean theme = false; // default

        if (!db.userDao().userExists(input_user)){
            User user = new User(input_user,input_pass,lang,theme);
            db.userDao().insertUser(user);
        }
    }

    private void checkLogin(View view) {
        String sUser = mUserText.getText().toString();
        String sPass = mPassText.getText().toString();

        System.out.println(sUser + " " + sPass);
        boolean basicLogin = sUser.equals("") && sPass.equals("");
        User u = login(sUser,sPass);

        if (basicLogin || (u != null)){ // de prueba
            // entrar
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("username",sUser);
            finish();
            startActivity(i);
        } else {
            //toast > incorrect login
            Toast.makeText(view.getContext(),R.string.login_incorrect_creds,Toast.LENGTH_SHORT).show();
        }

    }

    private User login(String sUser, String sPass) {
        AppDatabase db  = AppDatabase.getInstance(this.getApplicationContext());
        return db.userDao().getUser(sUser, sPass);
    }
}