package com.assignment1.chatapplication;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class ServerWorker extends Thread implements Runnable{
    //private final Socket clientSocket;
    private final Server server;
    private String login = "placeHolder";
    private OutputStream outputStream;
    private InputStream inputStream;
    private HashSet<String> topicSet = new HashSet<>();
    private static ArrayList<String> userList = new ArrayList<>();
    private static ArrayList<String> passwordList = new ArrayList<>();

    public ServerWorker(Server server/*, Socket clientSocket*/){
        this.server = server;
       // this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
            System.out.println("Worker started");
            passwordList.add("admin");
            userList.add("admin");


//        try {
//            serverWorker_in = new ObjectInputStream(clientSocket.getInputStream());
//            serverWorker_out = new ObjectOutputStream(clientSocket.getOutputStream());
//            System.out.println("Worker listening...");
//            while (true) {
//
//                input = (String) serverWorker_in.readObject();
//
//                String[] token = StringUtils.split(input);
//                System.out.println("Worker received this..." + input);
//
//                if (token[0].equals("login")) {
//                    if (handleLogin(token[1], token[2])) {
//                        serverWorker_out.writeObject("true");
//                    } else serverWorker_out.writeObject("false");
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

/*
   private void handleClientSocket() throws IOException {
        this.inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line=reader.readLine())!=null){
            String[] token = StringUtils.split(line);
            if(token != null && token.length > 0){
                String cmd = token[0];
                if("logoff".equals(cmd)||"quit".equalsIgnoreCase(cmd)){
                    logoffHandle();
                    break;
                }
                else if ("login".equalsIgnoreCase(cmd)){
                    handleLogin(outputStream, token);
                }
                else if("msg".equalsIgnoreCase(cmd)){
                    String[] tokenMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokenMsg);
                }
                else if("join".equalsIgnoreCase(cmd)){
                    handleJoin(token);
                }
                else if("leave".equalsIgnoreCase(cmd)){
                    handleLeave(token);
                }
                else if("register".equalsIgnoreCase(cmd)){
                    handleRegister(token);
                }
                else if("print".equalsIgnoreCase(cmd)){
                    printInfo();
                }
                else {
                    String msg = "Error command\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        clientSocket.close();
    }
 */

    private void printInfo() throws IOException {
        System.out.println(userList + "\n");
        System.out.println(passwordList);
    }

    public boolean handleRegister(String username, String password, String rePassword, Context c) {
        if (!username.equals("") && !password.equals("") && !rePassword.equals("")) {
            if (!isRegistered(username)){
                if (password.equals(rePassword)){
                    userList.add(username);
                    passwordList.add(password);
                    Toast.makeText(c,"Register success!", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(c,"Password does not match.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(c,"User already exist.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else Toast.makeText(c,"No empty field.", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean isRegistered(String username){
        return userList.contains(username);
    }

    private void handleLeave(String[] token) {
        if(token.length > 1){
            String topic = token[1];
            topicSet.remove(topic);
        }
    }

    public boolean isMemberOfTopic(String topic){
        return topicSet.contains(topic);
    }

    private void handleJoin(String[] token) {
        if(token.length > 1){
            String topic = token[1];
            topicSet.add(topic);
        }
    }

    public void handleMessage(MessageAttr message) throws IOException {

        boolean isTopic = message.getSender().charAt(0) == '#';

        List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker worker : workerList){
            if(isTopic){
                if(worker.isMemberOfTopic(message.getSender())){
                    String outMsg = "from " + message.getSender() + " " + "by " + login + ":" + " " + message.getMsgContent() + "\n";
                    worker.send(outMsg);
                    MainActivity.getMsgList().add(message);
                }
            }
            if(message.getSender().equalsIgnoreCase(worker.getLogin())){
                String outMsg = "from " + login + ":" + " " + message.getMsgContent() + "\n";
                MainActivity.getMsgList().add(message);
                worker.send(outMsg);
            }
        }

    }

    public void logoffHandle() throws IOException {
        server.removeWorker(this);
        List<ServerWorker> workerList = server.getWorkerList();
        String offlmsg = "Offline " + login + "\n";
        for(ServerWorker worker : workerList){
            if(!login.equals(worker.getLogin()) && !login.contentEquals("placeHolder")) {
                worker.send(offlmsg);
            }
        }
        //clientSocket.close();
    }

    public String getLogin() {
        return login;
    }

    public boolean handleLogin(String username, String password) throws IOException {

        if (isRegistered(username)) {
            if (password.equals(passwordList.get(userList.indexOf(username)))) {
                List<ServerWorker> workerList = server.getWorkerList();
                for (ServerWorker worker : workerList) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin()) && login.contentEquals("placeHolder")) {
                            String msg_1 = worker.getLogin() + ": online" + "\n";
                            send(msg_1);
                            //Toast.makeText(c, "Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                //String onlmsg = login + " online" + "\n";
                for (ServerWorker worker : workerList) {
                    if (!login.equals(worker.getLogin()) && !login.contentEquals("placeHolder")) {
                            //print user online
                    }
                }
                return true;
            }
            else {
                //Toast.makeText(c, "Wrong credentials", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            //Toast.makeText(c, "User not registered", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void send(String msg) throws IOException { //delete this method
        if(login != null){
            outputStream.write(msg.getBytes());
        }
    }

}
