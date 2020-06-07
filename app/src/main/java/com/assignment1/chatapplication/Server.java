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
import java.io.Reader;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements Runnable {

    public final int serverPort;
    private ArrayList<ServerWorker> workerlist = new ArrayList<>();
    private ServerWorker worker = new ServerWorker(this);
    private ServerSocket serverSocket = null;
    private static Socket serverCommSocket = null;

    private ObjectInputStream server_in;
    private ObjectOutputStream server_out;





    public ServerWorker getWorker() {
        return worker;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Server(int serverPort){
        this.serverPort = serverPort;
    }

    public List<ServerWorker> getWorkerList(){
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
            if (getServerSocket() == null) { serverSocket = new ServerSocket(serverPort);}
            if (getServerCommSocket() == null) {
                serverCommSocket = new Socket(SignInOut.getIPAddress(true), 8818);

                server_out = new ObjectOutputStream(getServerCommSocket().getOutputStream());
                System.out.println(server_out);
                //server_out.flush();

//                server_in = new ObjectInputStream(getServerCommSocket().getInputStream());
//                System.out.println(server_in);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (getServerCommSocket() != null){ //RECEIVE DATA HERE
//            try {
//                server_in = new ObjectInputStream(getServerCommSocket().getInputStream());
//                System.out.println((String) server_in.readObject());
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
        worker.start();

        while (true) {
            try{
                System.out.println("Accept connection from: " + serverSocket.accept());
                System.out.println("Looking for connect request ...");

                server_out.writeObject("Your connection is accepted");



            } catch (IOException e) {
                e.printStackTrace();
            }

//            worker = new ServerWorker(this, clientSocket);
//            workerlist.add(worker);
//            System.out.println(workerlist);

        }
    }
}
