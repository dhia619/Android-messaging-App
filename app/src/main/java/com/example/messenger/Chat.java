package com.example.messenger;

public class Chat {
    private String sender;
    private String receiver;
    private String message;

    public Chat() {
    }
    public Chat(String sender, String receiver, String mmessage){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
