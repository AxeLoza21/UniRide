package com.example.uniride;

public class locationElement {
    private String nEscuela;
    private String nCampus;


    public locationElement(String nEscuela, String nCampus) {
        this.nEscuela = nEscuela;
        this.nCampus = nCampus;
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
}
