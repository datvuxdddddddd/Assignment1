package com.assignment1.chatapplication;

public class ServerMain {
    public static boolean main(/*String[] args*/) {
        int port = 8818;
        Server server = new Server(port);
        server.start();
        return true;
    }
}
