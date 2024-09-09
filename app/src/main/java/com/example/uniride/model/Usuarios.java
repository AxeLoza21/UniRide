package com.example.uniride.model;

public class Usuarios {
    private String userId;
    private String nombre;
    private String fotoPerfilUrl;
    private boolean online;

    public Usuarios() {
    }

    public Usuarios(String userId, String nombre, String fotoPerfilUrl, boolean online) {
        this.userId = userId;
        this.nombre = nombre;
        this.fotoPerfilUrl = fotoPerfilUrl;
        this.online = online;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
