package com.example.uniride.model;

public class Publications {

    String IdCreator, timePublication, travelSeating, direccionPartida;
    Float rating; // Nueva variable para la calificación

    public Publications(){}

    public Publications(String idCreator, String timePublication, String travelSeating, String direccionPartida, Float rating) {
        this.IdCreator = idCreator;
        this.timePublication = timePublication;
        this.travelSeating = travelSeating;
        this.direccionPartida = direccionPartida;
        this.rating = rating;
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
