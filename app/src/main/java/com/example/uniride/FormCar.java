package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button SaveCar;

    EditText etMarcaVehiculo, etModeloVehiculo, etNumeroPlaca, etAnioVehiculo;
    Spinner spTipoVehiculo, spColorVehiculo;
    Button btnSaveCar;

    // Referencia a Firestore
    FirebaseFirestore db;

    String [] opciones = {"-","Sedan","Suv","´PickUp","Compacto"};
    String [] opciones2 = {"-","Rojo","Verde","´Azul","Blanco","Negro","Plateado","Amarillo","Rosa","Morado","Gris","Café"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_car);
        spTipoVehiculo = (Spinner)findViewById(R.id.SpinerTipoVehiculo);
        spColorVehiculo = (Spinner)findViewById(R.id.SpinerColorVehiculo);
        SaveCar = (Button)findViewById(R.id.btn_image);
        etMarcaVehiculo = (EditText)findViewById(R.id.EditTextMarcaVehiculo);
        etModeloVehiculo = (EditText)findViewById(R.id.EditTextModeloVehiculo);
        etNumeroPlaca = (EditText)findViewById(R.id.EditTextNumeroPlaca);
        etAnioVehiculo = (EditText)findViewById(R.id.EditTextAñoVehiculo);
        db = FirebaseFirestore.getInstance();

        // Configurar spinner de tipo de vehículo
        ArrayAdapter<String> aa = new ArrayAdapter<String>(FormCar.this,
                R.layout.listviewresours, opciones);
        spTipoVehiculo.setAdapter(aa);
        spTipoVehiculo.setOnItemSelectedListener( this);

        // Configurar spinner de color de vehículo
        ArrayAdapter<String> ae = new ArrayAdapter<String>(FormCar.this,
                R.layout.listviewcolorcar, opciones2);
        spColorVehiculo.setAdapter(ae);
        spColorVehiculo.setOnItemSelectedListener(this);
        SaveCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCarro();
            }
        });
    }
    // Método para guardar el carro en Firestore
    private void guardarCarro() {
        String marca = etMarcaVehiculo.getText().toString();
        String modelo = etModeloVehiculo.getText().toString();
        String placa = etNumeroPlaca.getText().toString();
        String tipo = spTipoVehiculo.getSelectedItem().toString();
        String color = spColorVehiculo.getSelectedItem().toString();
        int anio = Integer.parseInt(etAnioVehiculo.getText().toString());

        // Crear un mapa con los datos del carro
        Map<String, Object> carro = new HashMap<>();
        carro.put("make", marca);
        carro.put("model", modelo);
        carro.put("plate number", placa);
        carro.put("type", tipo);
        carro.put("color", color);
        carro.put("year", anio);

        // Obtener la referencia del documento cars para el usuario actual
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference carsRef = db.collection("users").document(userId).collection("cars").document();
        carsRef.set(carro).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent i = new Intent(FormCar.this, MainActivityFragment.class);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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