package com.example.uniride.model;

public class Publications {

    String IdCreator, timePublication, travelSeating, direccionPartida;
    public Publications(){}

    public Publications(String idCreator, String timePublication, String travelSeating, String direccionPartida) {
        this.IdCreator = idCreator;
        this.timePublication = timePublication;
        this.travelSeating = travelSeating;
        this.direccionPartida = direccionPartida;
    }

    public String getIdCreator() {
        return IdCreator;
    }

    public void setIdCreator(String idCreator) {
        IdCreator = idCreator;
    }

    public String getTimePublication() {
        return timePublication;
    }

    public void setTimePublication(String timePublication) {
        this.timePublication = timePublication;
    }

    public String getTravelSeating() {
        return travelSeating;
    }

    public void setTravelSeating(String travelSeating) {
        this.travelSeating = travelSeating;
    }

    public String getDireccionPartida() {
        return direccionPartida;
    }

    public void setDireccionPartida(String direccionPartida) {
        this.direccionPartida = direccionPartida;
    }
}
