package com.example.uniride;

public class raiteElement {
    public String nameUser;
    public String direccion;
    public String salida;
    public String asientos;

    public raiteElement(String nameUser, String direccion, String salida, String asientos) {
        this.nameUser = nameUser;
        this.direccion = direccion;
        this.salida = salida;
        this.asientos = asientos;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getAsientos() {
        return asientos;
    }

    public void setAsientos(String asientos) {
        this.asientos = asientos;
    }
}
