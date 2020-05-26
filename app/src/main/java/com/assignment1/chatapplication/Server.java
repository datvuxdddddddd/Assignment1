package com.assignment1.chatapplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    public final int serverPort;
    public ArrayList<ServerWorker> workerlist = new ArrayList<>();
    public static Server instance = null;
    public ServerWorker worker;

    public ServerWorker getWorker() { /** Based on username */
        return worker;
    }

    public static synchronized Server getInstance(){
        if(instance == null) {
            instance = new Server(8188);
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
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                System.out.println("About to accept client connection ...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accept connection from: " + clientSocket);
                worker = new ServerWorker(this, clientSocket);
                workerlist.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerlist.remove(serverWorker);
    }
}
