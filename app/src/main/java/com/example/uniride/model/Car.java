package com.example.uniride.model;

public class Car {
    String make, model, color, year;

    public Car(){}

    public Car(String make, String model, String color, String year) {
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
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
}
