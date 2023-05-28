package com.example.uniride.model;

public class Publications {

    String IdCreator, timePublication, travelSeating;
    public Publications(){}

    public Publications(String idCreator, String timePublication, String travelSeating) {
        this.IdCreator = idCreator;
        this.timePublication = timePublication;
        this.travelSeating = travelSeating;
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
}
