package com.assignment1.chatapplication;

import android.widget.Adapter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements Runnable {

    public final int serverPort;
    private ArrayList<ServerWorker> workerlist = new ArrayList<>();
    private ServerWorker worker = new ServerWorker(this);
    private static ServerSocket serverSocket = null;
    private static Socket serverCommSocket = null;

    private DataOutputStream dataOutputStream;


    public ServerWorker getWorker() {
        return worker;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerWorker> getWorkerList() {
        return workerlist;
    }

    public static Socket getServerCommSocket() {
        return serverCommSocket;
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerlist.remove(serverWorker);
    }

    @Override
    public void run() {
        try {
            if (getServerSocket() == null) {
                System.out.println("Creating new Chat Server...");

                serverSocket = new ServerSocket(serverPort);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (getServerCommSocket() == null) {
                System.out.println("Creating new serverCommSocket...");
                serverCommSocket = new Socket(SignInOut.getIPAddress(true), 8818);
                dataOutputStream = new DataOutputStream(serverCommSocket.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {


            try {
                System.out.println("Accept connection from: " + serverSocket.accept());
                System.out.println("Looking for connect request ...");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                dataOutputStream.writeUTF("Hello from the other side!");
                dataOutputStream.flush(); // send the message
                //dataOutputStream.close(); // close the output stream when we're done.
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


//            worker = new ServerWorker(this, clientSocket);
//            workerlist.add(worker);
//            System.out.println(workerlist);


    }
}
