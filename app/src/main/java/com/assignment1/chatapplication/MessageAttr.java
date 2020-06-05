package com.assignment1.chatapplication;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.*;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MessageAttr {
    public static final int VIEW_TYPE_MESSAGE_SENT = 1;
    public static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    // Message content.
    private String msgContent;
    // Message type.
    private int msgType;
    //sender
    private String sender;
    //Time
    private String timeSent;
    //Set time format
    DateTimeFormatter hhMM = DateTimeFormatter.ofPattern("HH:mm");

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MessageAttr(int msgType, String msgContent, String sender) {
        this.msgType = msgType;
        this.msgContent = msgContent;
        this.sender = sender;
        this.timeSent = LocalTime.now().format(hhMM);
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getSender() {
        return sender;
    }

    public String getTime(){
        return timeSent;
    }
}
