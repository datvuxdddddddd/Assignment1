package com.assignment1.chatapplication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements Runnable{

    public final int serverPort;
    private ArrayList<ServerWorker> workerlist = new ArrayList<>();
    private ServerWorker worker;
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    DataInputStream in;




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

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        if (getClientSocket() != null){ //RECEIVE DATA HERE

        }

        try {
            if (getServerSocket() == null) {
                serverSocket = new ServerSocket(serverPort);
            }
            if (getClientSocket() == null) {
                clientSocket = new Socket(SignInOut.getConnectToServerIPAddress(), 8818);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try{
                System.out.println("Looking for connect request ...");
                clientSocket = serverSocket.accept();   //block and wait
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Accept connection from: " + clientSocket);
            worker = new ServerWorker(this, clientSocket);
            workerlist.add(worker);
            worker.run();
        }
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerlist.remove(serverWorker);
    }
}
