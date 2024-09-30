package com.example.uniride.model;

import com.google.firebase.Timestamp;

public class Chat {
    private String chatId;
    private String senderId;
    private String recipientId;
    private Timestamp timestamp;

    // Constructor vacío necesario para Firestore
    public Chat() {}

    // Constructor completo
    public Chat(String chatId, String senderId, String recipientId, Timestamp timestamp) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.timestamp = timestamp;
    }

    // Getters y Setters
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
