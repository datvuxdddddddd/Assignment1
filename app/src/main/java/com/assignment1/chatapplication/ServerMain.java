package com.assignment1.chatapplication;

public class ServerMain {
    static Server server;
    public static boolean main(/*String[] args*/) {
        int port = 8818;
        server = new Server(port);
        server.start();
        return true;
    }
}
