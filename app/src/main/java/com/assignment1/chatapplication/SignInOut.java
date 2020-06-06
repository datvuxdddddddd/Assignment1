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

    DataOutputStream DOS;
    DataInputStream DIS;

    public static boolean validateIPPattern(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ip.matches(PATTERN);
    }

    @SuppressLint("StaticFieldLeak")
    private static class createNewUserSocket extends AsyncTask<Void, Void, Void>{
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

    @SuppressLint("StaticFieldLeak")
    private static class writeData extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            System.out.println("Writing data");
                if (getUserSocket() != null) {
                    DataOutputStream dOut;
                    try {
                        dOut = new DataOutputStream(getUserSocket().getOutputStream());
                        dOut.writeUTF("This is the first type of message.");
                        dOut.flush(); // Send off the data
                        dOut.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            return null;
        }
    }

    public static Server getChatServer() {
        return chatServer;
    }

    public String getWiFiIPAddress(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
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

///////////////////////////////////////////////////////////////////////////////////////////////////////
        if (getUserSocket() != null) {
            try {
                DIS = new DataInputStream(getUserSocket().getInputStream());
                while (!DIS.readUTF().equals("")) {
                    System.out.println(DIS.readUTF());
                    DIS.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        button_signin.setOnClickListener((View v) -> {
            userPassword = password.getText().toString();
            userUsername = username.getText().toString();
            if (userUsername.equals("") || userPassword.equals("")) {
                Toast.makeText(this, "Fields cannot be left empty", Toast.LENGTH_SHORT).show();
            }
            else {
                if (getChatServer() == null) {
                    // TODO write to server.
                    //TODO then startActivity
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
                        new createNewUserSocket().execute();
                        serverAddressInput.getText().clear();
                        dialogBuilder.dismiss();
                        chatServer = null;
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

    public static String getUserUsername() {
        return userUsername;
    }

    public static Socket getUserSocket() {
        return userSocket;
    }

    public static String getConnectToServerIPAddress() {
        return connectToServerIPAddress;
    }
}



