package com.example.uniride.model;

public class Request {
    String IdUser, IdPublication, direccionPoint, State;
    double PoinLat, PointLng;

    public Request(){}

    public Request(String IdUser, String IdPublication ,String direccionPoint, double PoinLat, double PointLng, String State){
        this.IdUser = IdUser;
        this.IdPublication = IdPublication;
        this.direccionPoint = direccionPoint;
        this.PoinLat = PoinLat;
        this.PointLng = PointLng;
        this.State = State;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getIdPublication() {
        return IdPublication;
    }

    public void setIdPublication(String idPublication) {
        IdPublication = idPublication;
    }

    public String getDireccionPoint() {
        return direccionPoint;
    }

    public void setDireccionPoint(String direccionPoint) {
        this.direccionPoint = direccionPoint;
    }

    public double getPoinLat() {
        return PoinLat;
    }

    public void setPoinLat(double poinLat) {
        PoinLat = poinLat;
    }

    public double getPointLng() {
        return PointLng;
    }

    public void setPointLng(double pointLng) {
        PointLng = pointLng;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
