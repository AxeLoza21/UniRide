package com.example.uniride;

import java.util.List;

public class locationElement {
    private String nEscuela;
    private String nCampus;
    private String categorias;


    public locationElement(String nEscuela, String nCampus, String categorias) {
        this.nEscuela = nEscuela;
        this.nCampus = nCampus;
        this.categorias = categorias;
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
}
