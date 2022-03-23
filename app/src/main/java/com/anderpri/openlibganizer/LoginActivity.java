package com.anderpri.openlibganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText mUserText;
    EditText mPassText;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserText = findViewById(R.id.login_username);
        mPassText = findViewById(R.id.login_password);
        mButton = findViewById(R.id.login_button);

        mButton.setOnClickListener(view -> comprobarLogin(view));

    }

    private void comprobarLogin(View view) {
        String sUser = mUserText.getText().toString();
        String sPass = mPassText.getText().toString();

        System.out.println(sUser + " " + sPass);

        if (sUser.equals("") && sPass.equals("")){ // de prueba
            // entrar
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        } else {
            //toast > incorrect login
            Toast.makeText(view.getContext(),R.string.login_incorrect_creds,Toast.LENGTH_SHORT).show();
        }

    }
}