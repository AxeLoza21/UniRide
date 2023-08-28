package com.example.uniride.model;

public class Car {
    String make, model, color, year, plateNumber;
    boolean predeterminado;

    public Car(){}

    public Car(String make, String model, String color, String year, String plateNumber, boolean predeterminado) {
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
        this.plateNumber = plateNumber;
        this.predeterminado = predeterminado;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }
    //
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    //
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    //
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public boolean isPredeterminado() {
        return predeterminado;
    }

    public void setPredeterminado(boolean predeterminado) {
        this.predeterminado = predeterminado;
    }
}
