package com.youthfimodd.elenges.custom.chat_model;

import com.google.firebase.database.ServerValue;

public class Chat {
    //
    private String sender;
    private String receiver;
    private String message;
    private String type;
    private Object datatime;
    private boolean Issoon;
    //

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String type, Object datatime, boolean Issoon) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
        this.datatime = datatime;
        this.Issoon = Issoon;
    }
    //
    public Chat(String sender, String receiver, String message, String type, boolean Issoon) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
        this.datatime = ServerValue.TIMESTAMP;
        this.Issoon = Issoon;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDatatime() {
        return datatime;
    }

    public void setDatatime(Object datatime) {
        this.datatime = datatime;
    }

    public boolean isIssoon() {
        return Issoon;
    }

    public void setIssoon(boolean issoon) {
        Issoon = issoon;
    }
}
