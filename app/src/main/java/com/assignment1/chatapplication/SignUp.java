package com.assignment1.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    EditText username, password, rePassword;
    Button button_signup_confirm, button_signup_return;
    private String newPassword, newUsername, newRePassword; // to save credentials
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        button_signup_return = findViewById(R.id.button_signup_return);
        button_signup_confirm = findViewById(R.id.button_signup_confirm);
        username = findViewById(R.id.newUsername);
        password = findViewById(R.id.newPassword);
        rePassword = findViewById(R.id.newRePassword);

        button_signup_confirm.setOnClickListener((View v) -> {
            newUsername = username.getText().toString();
            newPassword = password.getText().toString();
            newRePassword = rePassword.getText().toString();
           if(Server.getInstance().getWorker() == null){
//                    .handleRegister(newUsername, newPassword, newRePassword, this.getApplicationContext())){
               Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
           } else finish();

        });

        button_signup_return.setOnClickListener((View v) -> finish());

    }
}
