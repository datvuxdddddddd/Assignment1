package com.assignment1.chatapplication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.assignment1.chatapplication.SignInOut.getUserSocket;

public class Server extends Thread implements Runnable{

    public final int serverPort;
    private ArrayList<ServerWorker> workerlist = new ArrayList<>();
    private ServerWorker worker = new ServerWorker(this);
    private ServerSocket serverSocket = null;
    private Socket serverCommSocket = null;

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

    public Socket getServerCommSocket() {
        return serverCommSocket;
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerlist.remove(serverWorker);
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
            if (getUserSocket() != null) {


                    new Thread() {
                        public void run() {
                            try {
                                while (true) {
                                    System.out.println("creating input stream...");
                                    in = new ObjectInputStream(getServerCommSocket().getInputStream());
                                    System.out.println("from server:" + in.readObject());
                                    //in.close();
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();


            }
            try{
                System.out.println("Looking for connect request ...");
                clientSocket = serverSocket.accept();   //block and wait

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
