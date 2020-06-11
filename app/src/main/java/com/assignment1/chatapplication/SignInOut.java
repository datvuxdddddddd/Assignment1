package com.assignment1.chatapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class SignInOut extends AppCompatActivity {

    EditText username, password, serverAddressInput;
    Button button_signin, button_signup, button_server_start, button_server_connect;

    private String userPassword;
    private static String userUsername;
    private static Server chatServer = null;
    public static Socket userSocket = null;
    private static String serverAddress = null;

    ObjectOutputStream DOS;
    ObjectInputStream DIS;



    public static boolean validateIPPattern(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ip.matches(PATTERN);
    }

    private class createNewUserSocket extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                userSocket = new Socket(getServerAddress(), 8818);
                DIS = new ObjectInputStream(userSocket.getInputStream());

                while (true) {
                    System.out.println("server says: " + DIS.readObject());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public static Server getChatServer() {
        return chatServer;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':')<0;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public static String getUserUsername() {
        return userUsername;
    }

    public static Socket getUserSocket() {
        return userSocket;
    }

    public static String getServerAddress() {
        return serverAddress;
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
                if (getChatServer() != null && getUserSocket() != null) {
                    // TODO write to server.
                    //TODO then startActivity
                    new Thread(){
                        public void run(){
                            try {
                                DOS = new ObjectOutputStream(getUserSocket().getOutputStream());
                                DOS.writeObject("loginnnnnn....");
                                System.out.println("sent a message to server");
                                DOS.flush();
//                                while (true) {
//                                    //DIS = new ObjectInputStream(getUserSocket().getInputStream());
//                                    String input = "";
//                                    if (input.equals("true")) {
//                                        runOnUiThread(() -> Toast.makeText(SignInOut.this, "Welcome, " + userUsername, Toast.LENGTH_SHORT).show());
//                                        username.getText().clear();
//                                        password.getText().clear();
//                                        Intent mainUI = new Intent(SignInOut.this, MainActivity.class);
//                                        startActivity(mainUI);
//                                    } else
//                                        runOnUiThread(() -> Toast.makeText(SignInOut.this, "Wrong credentials", Toast.LENGTH_SHORT).show());
//                                }
                            }catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
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
                new Thread(){public void run(){chatServer.start();}}.start();


                Toast.makeText(this, "New server at " + getIPAddress(true), Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, getIPAddress(true), Toast.LENGTH_SHORT).show();
        });

        button_server_connect.setOnClickListener((View v) -> {

            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.popupwindows_server, null);
            serverAddressInput =dialogView.findViewById(R.id.serverAddressInput);

            serverAddressInput.setOnKeyListener((vv, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    serverAddress = serverAddressInput.getText().toString();
                    if (validateIPPattern(serverAddress)){
                        serverAddressInput.getText().clear();
                        dialogBuilder.dismiss();

                        new createNewUserSocket().execute();                        //create a socket to server regardless of server location.


                        if (!serverAddress.equals(getIPAddress(true))){  //if attempts to connect to another server,
                            chatServer = null;                                      // disable local server.
                        }


                        return true;
                    }
                    else {
                        serverAddressInput.getText().clear();
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



