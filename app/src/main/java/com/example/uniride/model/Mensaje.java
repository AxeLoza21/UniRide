package com.example.uniride.model;

import com.google.firebase.Timestamp;

public class Mensaje {
    private String mensaje;
    private String nombre;
    private String type_mensaje;
    private Timestamp hora;
    private String recipientId;
    private String senderId;

    // Constructor sin argumentos
    public Mensaje() {
    }

    // Constructor completo
    public Mensaje(String mensaje, String nombre, String type_mensaje, Timestamp hora, String recipientId, String senderId) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.type_mensaje = type_mensaje;
        this.hora = hora;
        this.recipientId = recipientId;
        this.senderId = senderId;
    }

    // Getters y setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }

    public Timestamp getHora() {
        return hora;
    }

    public void setHora(Timestamp hora) {
        this.hora = hora;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
