package com.example.uniride.functions;

import android.app.Activity;

import java.time.LocalDate;
import java.time.Period;

public class CalculateAge {

    int dia, mes, ano;
    String edad;

    public String calcularEdad(String fecha) {
        edad = "";
        String[] parts = fecha.split("/");
        dia = Integer.parseInt(parts[0]); // dia
        mes = Integer.parseInt(parts[1]); // mes
        ano = Integer.parseInt(parts[2]); // año

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            LocalDate birthdate = LocalDate.of(ano, mes, dia);
            Period p = Period.between(birthdate, today);
            edad = p.getYears() + " años";

        }

        return edad;
    }
}
