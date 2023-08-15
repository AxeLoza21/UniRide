package com.example.uniride.model;

public class Travel {
    String campusDestination,datePublication,timePublication,latInitation, State;

    public Travel(){}

    public Travel(String campusDestination, String datePublication, String timePublication, String latInitation, String State) {
        this.campusDestination = campusDestination;
        this.datePublication = datePublication;
        this.timePublication = timePublication;
        this.latInitation = latInitation;
        this.State = State;
    }
    public String getCampusDestination() {
        return campusDestination;
    }

    public void setCampusDestination(String campusDestination) {
        this.campusDestination = campusDestination;
    }
    public String getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String datePublication) {
        this.datePublication = datePublication;
    }

    public String getTimePublication() {
        return timePublication;
    }

    public void setTimePublication(String timePublication) {
        this.timePublication = timePublication;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getLatInitation() {
        return latInitation;
    }

    public void setLatInitation(String latInitation) {
        this.latInitation = latInitation;
    }
}
