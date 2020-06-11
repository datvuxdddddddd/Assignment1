package com.assignment1.chatapplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.assignment1.chatapplication.SignInOut.getUserSocket;

public class Server extends Thread implements Runnable {

    public final int serverPort;
    private ArrayList<ServerWorker> workerlist = new ArrayList<>();
    private ServerWorker worker = new ServerWorker(this);
    private static ServerSocket serverSocket = null;
    private static Socket serverCommSocket = null;

    private ObjectInputStream in;
    private ObjectOutputStream out;


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
            if (serverSocket == null) {
                System.out.println("Creating new chat server...");
                serverSocket = new ServerSocket(serverPort);
            }
            if (serverCommSocket == null) {
                System.out.println("Creating new serverCommSocket...");
                serverCommSocket = new Socket(SignInOut.getIPAddress(true), 8818);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true) {
                new Thread() {
                    public void run() {
                        try {
                            while (true) {
                                in = new ObjectInputStream(serverCommSocket.getInputStream());
                                String input = (String) in.readObject();
                                System.out.println("from client:" +  input);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();



            try{

                System.out.println("Looking for connect request ...");
                Socket client = serverSocket.accept();
                System.out.println("Accept connection from: " + client);   //block and wait

                out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject("Your connection is accepted");
                out.flush();



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
