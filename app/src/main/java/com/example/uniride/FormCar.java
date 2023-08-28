package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uniride.components.SnackBarElement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import android.graphics.Color;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormCar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button SaveCar;
    ImageView btnSalir;
    SnackBarElement snakBar;

    EditText etMarcaVehiculo, etModeloVehiculo, etNumeroPlaca, etAnioVehiculo;
    Spinner spTipoVehiculo, spColorVehiculo;

    // Referencia a Firestore
    FirebaseFirestore db;

    String [] opciones = {"-","Sedan","Suv","PickUp","Compacto"};
    String [] opciones2 = {"-","Rojo","Verde","Azul","Blanco","Negro","Plateado","Amarillo","Rosa","Morado","Gris","Café"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_car);
        spTipoVehiculo = (Spinner)findViewById(R.id.SpinerTipoVehiculo);
        spColorVehiculo = (Spinner)findViewById(R.id.SpinerColorVehiculo);
        SaveCar = (Button)findViewById(R.id.btn_image);
        btnSalir = (ImageView)findViewById(R.id.salir);
        etMarcaVehiculo = (EditText)findViewById(R.id.EditTextMarcaVehiculo);
        etModeloVehiculo = (EditText)findViewById(R.id.EditTextModeloVehiculo);
        etNumeroPlaca = (EditText)findViewById(R.id.EditTextNumeroPlaca);
        etAnioVehiculo = (EditText)findViewById(R.id.EditTextAñoVehiculo);
        db = FirebaseFirestore.getInstance();
        snakBar = new SnackBarElement(this);

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
                SaveCar.setEnabled(false);
                guardarCarro();


            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        String anio = etAnioVehiculo.getText().toString();



        if (marca.isEmpty()) {
            etMarcaVehiculo.requestFocus();
            etMarcaVehiculo.setError("Este campo es requerido");
            SaveCar.setEnabled(true);
            return;
        }else if (!marca.matches("^[a-zA-Z]{1,12}$")) {
            etMarcaVehiculo.requestFocus();
            etMarcaVehiculo.setError("La marca debe tener máximo 12 caracteres y no debe contener números, espacios ni caracteres especiales");
            SaveCar.setEnabled(true);
            return;
        }

        if (modelo.isEmpty()) {
            etModeloVehiculo.requestFocus();
            etModeloVehiculo.setError("Este campo es requerido");
            SaveCar.setEnabled(true);
            return;
        }if (modelo.length() > 10 || !modelo.matches("[a-zA-Z0-9 ]+")) {
            etModeloVehiculo.requestFocus();
            if (modelo.length() > 10) {
                etModeloVehiculo.setError("El modelo no puede tener más de 10 caracteres");
                SaveCar.setEnabled(true);
            } else if (!modelo.matches("[a-zA-Z0-9 ]+")) {
                etModeloVehiculo.setError("El modelo solo puede contener letras, números y espacios");
                SaveCar.setEnabled(true);
            }
            return;
        }

        if (placa.isEmpty()) {
            etNumeroPlaca.requestFocus();
            etNumeroPlaca.setError("Este campo es requerido");
            SaveCar.setEnabled(true);
            return;
        }else if (placa.length() > 7) {
            etNumeroPlaca.requestFocus();
            etNumeroPlaca.setError("La placa no puede tener más de 7 caracteres");
            SaveCar.setEnabled(true);
            return;
        }else if (!placa.matches("[a-zA-Z0-9\\-]+")) {
            etNumeroPlaca.requestFocus();
            etNumeroPlaca.setError("La placa solo puede contener letras, números y guiones (-)");
            SaveCar.setEnabled(true);
            return;
        }

        if (tipo.equals("-")) {
            ((TextView) spTipoVehiculo.getSelectedView()).setError("Este campo es requerido");
            SaveCar.setEnabled(true);
            return;
        }

        if (color.equals("-")) {
            ((TextView) spColorVehiculo.getSelectedView()).setError("Este campo es requerido");
            SaveCar.setEnabled(true);
            return;
        }

        if (anio.isEmpty()) {
            etAnioVehiculo.requestFocus();
            etAnioVehiculo.setError("Este campo es requerido");
            SaveCar.setEnabled(true);
            return;
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        // Validar el año del vehículo
        int anioVehiculo = Integer.parseInt(etAnioVehiculo.getText().toString());
        if (anioVehiculo < 1900 || anioVehiculo > year + 1) {
            etAnioVehiculo.requestFocus();
            etAnioVehiculo.setError("El año debe ser entre 1900 y " + (year + 1));
            SaveCar.setEnabled(true);
            return;
        } else if (anio.length() != 4) {
            etAnioVehiculo.requestFocus();
            etAnioVehiculo.setError("El año debe de tener 4 dígitos");
            SaveCar.setEnabled(true);
            return;
        }


        // Crear un mapa con los datos del carro
        Map<String, Object> carro = new HashMap<>();
        carro.put("make", marca);
        carro.put("model", modelo);
        carro.put("plateNumber", placa);
        carro.put("type", tipo);
        carro.put("color", color);
        carro.put("year", anio);

        // Obtener la referencia del documento cars para el usuario actual
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).collection("cars").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size() >= 1){
                    carro.put("predeterminado", false);
                }else{
                    carro.put("predeterminado", true);
                }

                DocumentReference carsRef = db.collection("users").document(userId).collection("cars").document();
                carsRef.set(carro).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        snakBar.showSnackBar(getResources().getColor(R.color.green),"Se agrego un vehículo");

                        // Esperar 3 segundos antes de desbloquear la pantalla y volver a la actividad principal
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Desbloquear la pantalla
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Intent intent = getIntent();
                                String vehicles = intent.getStringExtra("Vehicles");
                                if (vehicles != null && vehicles.equals("MyVehicles")) {
                                    // Devolver a MyVehicles.class
                                    Intent i = new Intent(FormCar.this, MyVehicles.class);
                                    startActivity(i);
                                    SaveCar.setEnabled(true);
                                    finish();

                                } else {
                                    // Devolver al MainActivityFragment.class
                                    Intent i = new Intent(FormCar.this, MainActivityFragment.class);
                                    startActivity(i);
                                    SaveCar.setEnabled(true);
                                    finish();
                                }

                            }
                        }, 3000);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        SaveCar.setEnabled(true);
                        Toast.makeText(FormCar.this, "No se pudo guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}