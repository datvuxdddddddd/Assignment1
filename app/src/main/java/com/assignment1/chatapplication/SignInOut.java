package com.assignment1.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInOut extends AppCompatActivity {

    EditText username, password;
    Button button_signin, button_signup;
    private String userPassword, userUsername; // to save credentials

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        button_signin = findViewById(R.id.button_signin);
        button_signup = findViewById(R.id.button_signup);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);

        button_signin.setOnClickListener((View v) -> {
            userPassword = password.getText().toString();
            userUsername = username.getText().toString();
            if (userUsername.equals("") || userPassword.equals("")) {
                Toast.makeText(this, "Fields cannot be left empty", Toast.LENGTH_SHORT).show();
            }
            else {
                if (true /* check credential here */) {
                    Toast.makeText(this, "Welcome " /*+ user name*/, Toast.LENGTH_SHORT).show();

                    /* optionally, clear all text fields */
                    username.getText().clear();
                    password.getText().clear();

                    Intent mainUI = new Intent(this, MainActivity.class);
                    startActivity(mainUI);
                }
                else Toast.makeText(this, "Wrong username and/or password", Toast.LENGTH_SHORT).show();
            }
        });

        button_signup.setOnClickListener((View v) -> {
            Intent signUpMenu = new Intent(this, SignUp.class);
            startActivity(signUpMenu);
            /* optionally, clear all text fields */
                username.getText().clear();
                password.getText().clear();
        });
    }
}

