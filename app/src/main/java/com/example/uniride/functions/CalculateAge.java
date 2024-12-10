package com.example.uniride.functions;

import android.app.Activity;

import java.time.LocalDate;
import java.time.Period;

public class CalculateAge {

    int dia, mes, ano;
    String edad;

    public String calcularEdad(String fecha) {
        // Verificar si la fecha es nula o vacía
        if (fecha == null || fecha.isEmpty()) {
            return "Fecha no válida";
        }

        String[] parts = fecha.split("/");
        if (parts.length != 3) { // Verificar si la fecha tiene el formato esperado
            return "Formato de fecha inválido";
        }

        int dia, mes, ano;
        try {
            dia = Integer.parseInt(parts[0]); // día
            mes = Integer.parseInt(parts[1]); // mes
            ano = Integer.parseInt(parts[2]); // año
        } catch (NumberFormatException e) {
            return "Fecha contiene caracteres no numéricos";
        }

        // Verificar si la versión de Android es compatible con LocalDate
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            LocalDate birthdate;
            try {
                birthdate = LocalDate.of(ano, mes, dia);
            } catch (Exception e) {
                return "Fecha fuera de rango";
            }

            Period p = Period.between(birthdate, today);
            return p.getYears() + " años";
        } else {
            return "Versión de Android no compatible";
        }
    }
}
