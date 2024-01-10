package com.example.godee.ModelClass;

import com.google.firebase.Timestamp;

public class MessageModel {
    private String text; // Updated field name to match Firestore
    private String senderId;
    private Timestamp timestamp;

    public MessageModel() {
    }

    public MessageModel(String text, String senderId, Timestamp timestamp) {
        this.text = text;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getText() { // Updated method name
        return text;
    }

    public void setText(String text) { // Updated method name
        this.text = text;
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
