package com.example.uniride.model;

public class Request {
    String IdUser, direccionPoint;
    double PoinLat, PointLng;

    public Request(){}

    public Request(String IdUser, String direccionPoint, double PoinLat, double PointLng){
        this.IdUser = IdUser;
        this.direccionPoint = direccionPoint;
        this.PoinLat = PoinLat;
        this.PointLng = PointLng;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
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
}
