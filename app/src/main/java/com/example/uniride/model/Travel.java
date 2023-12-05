package com.example.uniride.model;

public class  Travel {
    String campusDestination,datePublication,timePublication, State, direccionPartida, IdSolicitud;

    public Travel(){}

    public Travel(String campusDestination, String datePublication, String timePublication, String State, String direccionPartida, String IdSolicitud) {
        this.campusDestination = campusDestination;
        this.datePublication = datePublication;
        this.timePublication = timePublication;
        this.State = State;
        this.direccionPartida = direccionPartida;
        this.IdSolicitud = IdSolicitud;
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


    public String getDireccionPartida() {
        return direccionPartida;
    }

    public void setDireccionPartida(String direccionPartida) {
        this.direccionPartida = direccionPartida;
    }

    public String getIdSolicitud() {
        return IdSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        IdSolicitud = idSolicitud;
    }
}
