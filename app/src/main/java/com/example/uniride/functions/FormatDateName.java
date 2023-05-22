package com.example.uniride.functions;

import android.app.Activity;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormatDateName {

    Calendar calendar = Calendar.getInstance();


    public String getDiaSemana(String fecha){

        Date inputDate = null;
        try {
            inputDate = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(inputDate);
        String diaSemana = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toUpperCase(Locale.ROOT);
        String nombreDia="";


        switch(diaSemana){
            case "MONDAY":
                nombreDia = "Lunes";
                break;
            case "TUESDAY":
                nombreDia = "Martes";
                break;
            case "WEDNESDAY":
                nombreDia = "Miercoles";
                break;
            case "THURSDAY":
                nombreDia = "Jueves";
                break;
            case "FRIDAY":
                nombreDia = "Viernes";
                break;
            case "SATURDAY":
                nombreDia = "Sabado";
                break;
            case "SUNDAY":
                nombreDia = "Domingo";
                break;
        }
        return nombreDia;
    }


    public String getNombreMes(String fecha){
        String nMes = fecha.substring(3,5);
        String nombreMes="";

        switch(nMes){
            case "01":
                nombreMes = "Enero";
                break;
            case "02":
                nombreMes = "Febrero";
                break;
            case "03":
                nombreMes = "Marzo";
                break;
            case "04":
                nombreMes = "Abril";
                break;
            case "05":
                nombreMes = "Mayo";
                break;
            case "06":
                nombreMes = "Junio";
                break;
            case "07":
                nombreMes = "Julio";
                break;
            case "08":
                nombreMes = "Agosto";
                break;
            case "09":
                nombreMes = "Septiembre";
                break;
            case "10":
                nombreMes = "Octubre";
                break;
            case "11":
                nombreMes = "Noviembre";
                break;
            case "12":
                nombreMes = "Diciembre";
                break;
        }
        return nombreMes;
    }
}
