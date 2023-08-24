package com.example.uniride;

import com.google.type.LatLng;

import java.util.List;

public class locationElement {
    private String nEscuela;
    private String nCampus;
    private String categorias;
    private double lat;
    private double lng;


    public locationElement(String nEscuela, String nCampus, String categorias, double lat, double lng) {
        this.nEscuela = nEscuela;
        this.nCampus = nCampus;
        this.categorias = categorias;
        this.lat = lat;
        this.lng = lng;
    }


    public String getnEscuela() {
        return nEscuela;
    }

    public void setnEscuela(String nEscuela) {
        this.nEscuela = nEscuela;
    }

    public String getnCampus() {
        return nCampus;
    }

    public void setnCampus(String nCampus) {
        this.nCampus = nCampus;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
