package com.assignment1.chatapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class SignInOut extends AppCompatActivity{

    EditText username, password, serverAddressInput;
    Button button_signin, button_signup, button_server_start, button_server_connect;
    private String userPassword;
    private static String userUsername; // change back to non static
    public static Server chatServer = null;
    private static String host = null;
    private Socket userSocket = null;
    private String connectToIPAddress = null;

    @SuppressLint("StaticFieldLeak")
    static AsyncTask<String, Void, String> getLocalHostAddress = new AsyncTask<String, Void, String>() {
        @Override
        protected String doInBackground(String... strings) {
            return host;
        }
    };




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        button_signin = findViewById(R.id.button_signin);
        button_signup = findViewById(R.id.button_signup);
        button_server_connect = findViewById(R.id.server_connect);
        button_server_start = findViewById(R.id.server_start);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        serverAddressInput = findViewById(R.id.serverAddressInput);

        button_signin.setOnClickListener((View v) -> {
            userPassword = password.getText().toString();
            userUsername = username.getText().toString();
            if (userUsername.equals("") || userPassword.equals("")) {
                Toast.makeText(this, "Fields cannot be left empty", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    if (chatServer.getWorker().handleLogin(userUsername, userPassword, this.getApplicationContext())){
                            Toast.makeText(this, "Welcome, " + userUsername, Toast.LENGTH_SHORT).show();

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

        button_server_start.setOnClickListener((View v) -> {
            if (chatServer == null) {
                chatServer = new Server(8818);
                chatServer.start();

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

                Toast.makeText(this, "Server started at " + ipAddress, Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Server already started", Toast.LENGTH_SHORT).show();

        });

        button_server_connect.setOnClickListener((View v) -> {
            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.popupwindows_server, null);
            serverAddressInput =dialogView.findViewById(R.id.serverAddressInput);
            serverAddressInput.setOnKeyListener((vv, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        connectToIPAddress = serverAddressInput.getText().toString();
                    try {
                        userSocket = new Socket(connectToIPAddress, 8818); //TODO async
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            });
            dialogBuilder.setView(dialogView);
            dialogBuilder.show();
        });
    }

    public static String getUserUsername() {
        return userUsername;
    }

    public Socket getUserSocket() {
        return userSocket;
    }
}

