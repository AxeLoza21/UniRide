package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FormCar extends AppCompatActivity {
    Spinner SpinerTipoCar;
    Spinner SpinerColorCar;

    String [] opciones = {"-","Sedan","Suv","´PickUp","Compacto"};
    String [] opciones2 = {"-","Rojo","Verde","´Azul","Blanco","Negro","Plateado","Amarillo","Rosa","Morado","Gris","Café"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_car);
        SpinerTipoCar = (Spinner)findViewById(R.id.SpinerTipoVehiculo);
        SpinerColorCar = (Spinner)findViewById(R.id.SpinerColorVehiculo);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(FormCar.this,
                R.layout.listviewresours, opciones2);
        SpinerTipoCar.setAdapter(aa);
        SpinerTipoCar.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        ArrayAdapter<String> ae = new ArrayAdapter<String>(FormCar.this,
                R.layout.listviewcolorcar, opciones2);
        SpinerColorCar.setAdapter(ae);
        SpinerColorCar.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }
}