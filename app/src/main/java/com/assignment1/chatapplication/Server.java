package com.assignment1.chatapplication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    ObjectInputStream server_in;
    ObjectOutputStream server_out;





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

    public void removeWorker(ServerWorker serverWorker) {
        workerlist.remove(serverWorker);
    }

    @Override
    public void run() {
        try {
            if (getServerSocket() == null) { serverSocket = new ServerSocket(serverPort);}
            if (getClientSocket() == null) { clientSocket = new Socket(SignInOut.getConnectToServerIPAddress(), 8818);}
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        if (getClientSocket() != null){ //RECEIVE DATA HERE
//            try {
//                server_in = new ObjectInputStream(getClientSocket().getInputStream());
//                System.out.println((String) server_in.readObject());
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }

        while (true) {
            try{
                System.out.println("Looking for connect request ...");
                clientSocket = serverSocket.accept();   //block and wait

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Accept connection from: " + clientSocket);

            try{
                server_out = new ObjectOutputStream(getClientSocket().getOutputStream());
                server_out.writeObject("Your connection is accepted");
            } catch (IOException e) {
                e.printStackTrace();
            }
            worker = new ServerWorker(this, clientSocket);
            workerlist.add(worker);
            System.out.println(workerlist);
            worker.run();
        }
    }

}
