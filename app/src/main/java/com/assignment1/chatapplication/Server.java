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
    public ArrayList<ServerWorker> workerlist = new ArrayList<>();
    public static Server instance;
    public ServerWorker worker;
    public ServerSocket serverSocket;
    public Socket clientSocket;


    public ServerWorker getWorker() { /** Based on username */
        return worker;
    }

    public static synchronized Server getInstance(){
        if(instance == null) {
            instance = new Server(8818);
            instance.start();
        }
        return instance;
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
            //serverSocket.setSoTimeout(10000);
            InetAddress host = InetAddress.getLocalHost();
            clientSocket = new Socket(host.getHostName(), 8818);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
                System.out.println("About to accept client connection ...");

            try {

                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Accept connection from: " + clientSocket);
                worker = new ServerWorker(this, clientSocket);
                workerlist.add(worker);
                worker.start();
        }
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerlist.remove(serverWorker);
    }

}
