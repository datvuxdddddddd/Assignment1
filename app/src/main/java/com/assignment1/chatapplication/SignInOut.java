package com.assignment1.chatapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SignInOut extends AppCompatActivity{

    EditText username, password, serverAddressInput;
    Button button_signin, button_signup, button_server_start, button_server_connect;

    private String userPassword;
    private static String userUsername;
    private static Server chatServer = null;
    private static Socket userSocket = null;
    private static String connectToServerIPAddress = null;

    ObjectInputStream signInOut_in;
    ObjectOutputStream signInOut_out;



    public static boolean validateIPPattern(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ip.matches(PATTERN);
    }

    private class createNewUserSocket extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                userSocket = new Socket(getConnectToServerIPAddress(), 8818);
            } catch (IOException e) {
                        e.printStackTrace();
                    }
            return null;
        }
    }

    private class sendCredentials extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                signInOut_out = new ObjectOutputStream(getUserSocket().getOutputStream());
                signInOut_out.writeObject("Incoming connection from " + getWiFiIPAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class checkConnectionResult extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            if (getUserSocket() != null) {
                try {
                    signInOut_in = new ObjectInputStream(getUserSocket().getInputStream());
                    String backgroundResult = (String) signInOut_in.readObject();
                    System.out.println(backgroundResult);
                    return backgroundResult;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String backgroundResult) {
            super.onPostExecute(backgroundResult);
            Toast.makeText(SignInOut.this,"SignInOut says: " + backgroundResult, Toast.LENGTH_SHORT).show();
        }
    }

    public static Server getChatServer() {
        return chatServer;
    }

    public String getWiFiIPAddress(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    public static String getUserUsername() {
        return userUsername;
    }

    public static Socket getUserSocket() {
        return userSocket;
    }

    public static String getConnectToServerIPAddress() {
        return connectToServerIPAddress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        button_signin = findViewById(R.id.button_signin);
        button_signup = findViewById(R.id.button_signup);
        button_server_connect = findViewById(R.id.server_connect);
        button_server_start = findViewById(R.id.server_start);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        serverAddressInput = findViewById(R.id.serverAddressInput);
        ////////////////////////////////////////////////




///////////////////////////////////////////////////////////////////////////////////////////////////////

        button_signin.setOnClickListener((View v) -> {
            userPassword = password.getText().toString();
            userUsername = username.getText().toString();
            if (userUsername.equals("") || userPassword.equals("")) {
                Toast.makeText(this, "Fields cannot be left empty", Toast.LENGTH_SHORT).show();
            }
            else {
                if (getChatServer() == null) {
                   // new writeData().execute();
                    // TODO write to server.
                    //TODO then startActivity
                    new Thread(){
                        public void run(){
                            try {
                                signInOut_out = new ObjectOutputStream(getUserSocket().getOutputStream());
                                signInOut_out.writeObject("login " + userUsername + " " + userPassword);

                                signInOut_in = new ObjectInputStream(getUserSocket().getInputStream());
                                if (signInOut_in.readObject().equals("true")){
                                    Intent mainUI = new Intent(SignInOut.this, MainActivity.class);
                                    startActivity(mainUI);
                                }
                                else Toast.makeText(SignInOut.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                }
              else{
                    try {
                        if (getChatServer().getWorker().handleLogin(userUsername, userPassword, this.getApplicationContext())) {
                            Toast.makeText(this, "Welcome, " + userUsername, Toast.LENGTH_SHORT).show();

                            /* optionally, clear all text fields */
                            username.getText().clear();
                            password.getText().clear();

                            Intent mainUI = new Intent(this, MainActivity.class);
                            startActivity(mainUI);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            if (chatServer == null){
                userSocket = null;      //the device is the server, destroy socket to other servers
                chatServer = new Server(8818);
                chatServer.start();
                Toast.makeText(this, "New server at " + getWiFiIPAddress(), Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Server already started " + getWiFiIPAddress(), Toast.LENGTH_SHORT).show();
        });

        button_server_connect.setOnClickListener((View v) -> {

            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.popupwindows_server, null);

            serverAddressInput =dialogView.findViewById(R.id.serverAddressInput);

            serverAddressInput.setOnKeyListener((vv, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    connectToServerIPAddress = serverAddressInput.getText().toString();
                    if (validateIPPattern(connectToServerIPAddress)){
                        serverAddressInput.getText().clear();
                        dialogBuilder.dismiss();

                        new createNewUserSocket().execute();        //create a socket to server
                                                                    // regardless of server location.
                        if (!connectToServerIPAddress.equals(getWiFiIPAddress())){ //if attempts to connect to another server,
                            chatServer = null;                               // disable local server.
                        }
                        new checkConnectionResult().execute();

                        return true;
                    }
                    else {
                        serverAddressInput.clearComposingText();
                        Toast.makeText(this, "Invalid IPv4 Address!", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            });
            dialogBuilder.setView(dialogView);
            dialogBuilder.show();
        });
    }


}



