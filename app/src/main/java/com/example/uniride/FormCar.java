package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class FormCar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner SpinerTipoCar;
    Spinner SpinerColorCar;
    Button perfilGeneral;

    String [] opciones = {"-","Sedan","Suv","´PickUp","Compacto"};
    String [] opciones2 = {"-","Rojo","Verde","´Azul","Blanco","Negro","Plateado","Amarillo","Rosa","Morado","Gris","Café"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_car);
        SpinerTipoCar = (Spinner)findViewById(R.id.SpinerTipoVehiculo);
        SpinerColorCar = (Spinner)findViewById(R.id.SpinerColorVehiculo);
        perfilGeneral = (Button)findViewById(R.id.btn_image);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(FormCar.this,
                R.layout.listviewresours, opciones);
        SpinerTipoCar.setAdapter(aa);
        SpinerTipoCar.setOnItemSelectedListener( this);

        ArrayAdapter<String> ae = new ArrayAdapter<String>(FormCar.this,
                R.layout.listviewcolorcar, opciones2);
        SpinerColorCar.setAdapter(ae);
        SpinerColorCar.setOnItemSelectedListener(this);
        perfilGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FormCar.this, PerfilGeneral.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}