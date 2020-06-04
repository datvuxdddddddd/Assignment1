package com.assignment1.chatapplication;

import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements Runnable{

    public final int serverPort;
    private ArrayList<ServerWorker> workerlist = new ArrayList<>();
    //public static Server instance;
    private ServerWorker worker;
    private ServerSocket serverSocket;
    public Socket clientSocket;

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

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            //InetAddress host = InetAddress.getLocalHost();
            //System.out.println(host);
            clientSocket = new Socket("10.0.2.16", 8818);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
                System.out.println("Looking for connect request ...");
            try {
                clientSocket = serverSocket.accept();
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
