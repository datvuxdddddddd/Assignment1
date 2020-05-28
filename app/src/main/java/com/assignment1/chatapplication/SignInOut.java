package com.assignment1.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import static com.assignment1.chatapplication.Server.getInstance;
import static com.assignment1.chatapplication.Server.instance;

public class SignInOut extends AppCompatActivity {

    EditText username, password;
    Button button_signin, button_signup;
    private String userPassword, userUsername; // to save credentials

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        instance = getInstance();


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
                try {
                    if (instance.getWorker().handleLogin(userUsername, userPassword, this.getApplicationContext())){

                        /* optionally, clear all text fields */
                        username.getText().clear();
                        password.getText().clear();

                        Intent mainUI = new Intent(this, MainActivity.class);
                        startActivity(mainUI);
                    }
                    else Toast.makeText(this, "Wrong username and/or password", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

