package com.example.godee.ModelClass;

import com.google.firebase.Timestamp;

public class MessageModel {
    private String text;
    private String senderId;
    private Timestamp timestamp;

    public MessageModel() {
    }

    public MessageModel(String message, String senderId, Timestamp timestamp) {
        this.text = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return text;
    }

    public void setMessage(String message) {
        this.text = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}