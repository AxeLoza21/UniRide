package com.example.uniride.model;

public class Travel {
    String campusDestination,datePublication,latInitation;

    public Travel(){}

    public Travel(String campusDestination, String datePublication, String latInitation) {
        this.campusDestination = campusDestination;
        this.datePublication = datePublication;
        this.latInitation = latInitation;
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
    public String getLatInitation() {
        return latInitation;
    }

    public void setLatInitation(String latInitation) {
        this.latInitation = latInitation;
    }
}
