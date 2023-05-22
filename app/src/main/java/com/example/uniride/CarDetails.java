package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class CarDetails extends AppCompatActivity {
    Spinner spColorVehiculo, spTipoVehiculo;
    Button btnguardar;
    FirebaseFirestore db;
    Dialog d_edit;
    ImageView btnExit;
    FirebaseAuth mAuth;
    TextView marcaTextView, modeloTextView, placaTextView, añoTextView;
    String marca, modelo, placa, anioStr, color, tipo, newmarca, newmodelo, newplaca, newañoStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        spColorVehiculo = (Spinner)findViewById(R.id.SpinerColorVehiculo);
        spTipoVehiculo = (Spinner)findViewById(R.id.SpinerTipoVehiculo);
        btnguardar = (Button)findViewById(R.id.btn_image);
        marcaTextView = (TextView)findViewById(R.id.EditTextMarcaVehiculo);
        modeloTextView = (TextView)findViewById(R.id.EditTextModeloVehiculo);
        placaTextView = (TextView)findViewById(R.id.EditTextNumeroPlaca);
        añoTextView = (TextView)findViewById(R.id.EditTextAñoVehiculo);
        btnExit = (ImageView)findViewById(R.id.Salir);
        d_edit = new Dialog(this);
        String idCar = getIntent().getStringExtra("id_car");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DocumentReference carRef = db.collection("users").document(mAuth.getUid()).collection("cars").document(idCar);
        carRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    marca = documentSnapshot.getString("make");
                    modelo = documentSnapshot.getString("model");
                    placa = documentSnapshot.getString("plate number");
                    anioStr = documentSnapshot.getString("year");
                    //anioStr = Long.toString(anio); // convertir a String
                    color = documentSnapshot.getString("color");
                    tipo = documentSnapshot.getString("type");

                    ArrayList<String> colores = new ArrayList<>(Arrays.asList("Rojo","Verde","Azul","Blanco","Negro","Plateado","Amarillo","Rosa","Morado","Gris","Café"));
                    ColorSpinnerAdapter colorAdapter = new ColorSpinnerAdapter(CarDetails.this, colores);
                    spColorVehiculo.setAdapter(colorAdapter);

                    ArrayList<String> tipos = new ArrayList<>(Arrays.asList("Sedan","Suv","PickUp","Compacto"));
                    TypeSpinnerAdapter typeAdapter = new TypeSpinnerAdapter(CarDetails.this, tipos);
                    spTipoVehiculo.setAdapter(typeAdapter);

                    //----------Asignar los valores a los TextView correspondientes----------
                    int position = colores.indexOf(color);
                    int position2 = tipos.indexOf(tipo);
                    spColorVehiculo.setSelection(position);
                    spTipoVehiculo.setSelection(position2);

                    marcaTextView.setText(marca);
                    marcaTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog("Marca");
                        }
                    });

                    modeloTextView.setText(modelo);
                    modeloTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog("Modelo");
                        }
                    });

                    placaTextView.setText(placa);
                    placaTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog("Placa");
                        }
                    });

                    añoTextView.setText(anioStr); // establecer en TextView
                    añoTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDialog("Ano");
                        }
                    });

                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnExit.setEnabled(false);
                btnguardar.setEnabled(false);
                onBackPressed();
            }
        });
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnguardar.setEnabled(false);
                btnExit.setEnabled(false);

                // Leer los nuevos valores
                newmarca = marcaTextView.getText().toString();
                newmodelo = modeloTextView.getText().toString();
                newplaca = placaTextView.getText().toString();
                newañoStr = añoTextView.getText().toString();
                //long newaño = Long.parseLong(newañoStr); // convertir a long
                String colorSeleccionado = spColorVehiculo.getSelectedItem().toString();
                String tipoSeleccionado = spTipoVehiculo.getSelectedItem().toString();


                // Actualizar los datos en Firebase
                DocumentReference carRef = db.collection("users").document(mAuth.getUid()).collection("cars").document(idCar);
                carRef.update(
                        "color", colorSeleccionado,
                        "make", newmarca,
                        "model", newmodelo,
                        "plate number", newplaca,
                        "type", tipoSeleccionado,
                        "year", newañoStr
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Mostrar un mensaje de éxito
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Los datos se modificaron correctamente", Snackbar.LENGTH_LONG);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.green)); // Establecer el color de fondo
                        snackbar.show();

                        // Esperar 3 segundos antes de desbloquear la pantalla y volver a la actividad principal
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Desbloquear la pantalla
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                //Intent i = new Intent(CarDetails.this, PerfilFragment.class);
                                //startActivity(i);
                                //finish();
                            }
                        }, 3000);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnguardar.setEnabled(true);
                        btnExit.setEnabled(true);
                        // Mostrar un mensaje de error
                        Toast.makeText(CarDetails.this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    //-----------Metodo para mostrar el Dialog---------
    private void openDialog(String tDato){
        // Crear el diálogo personalizado
        d_edit.setContentView(R.layout.edit_user_dialog);
        d_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_edit.setCanceledOnTouchOutside(false);
        d_edit.show();

        //Obtener referencias a los elementos de la interfaz de usuario en el diálogo
        TextView txtEditar = d_edit.findViewById(R.id.datoEdit);
        EditText editText = d_edit.findViewById(R.id.et_dato);
        Button btnGuardar = d_edit.findViewById(R.id.btnGuardar);
        ImageView btnClose = d_edit.findViewById(R.id.btn_close);

        switch(tDato){
            case "Marca":
                //Texto que muestra el valor del que se esta actualizando.
                txtEditar.setText("Marca del Vehiculo");

                // Establecer el texto actual del TextView en el EditText
                editText.setText(marcaTextView.getText().toString());

                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obtener el nuevo texto del EditText
                        String newText = editText.getText().toString();

                        // Actualizar el TextView con el nuevo texto
                        marcaTextView.setText(newText);

                        // Cerrar el diálogo
                        d_edit.dismiss();
                    }
                });
                break;
            case "Modelo":
                //Texto que muestra el valor del que se esta actualizando.
                txtEditar.setText("Modelo del Vehiculo");

                // Establecer el texto actual del TextView en el EditText
                editText.setText(modeloTextView.getText().toString());

                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obtener el nuevo texto del EditText
                        String newText = editText.getText().toString();

                        // Actualizar el TextView con el nuevo texto
                        modeloTextView.setText(newText);

                        // Cerrar el diálogo
                        d_edit.dismiss();
                    }
                });
                break;
            case "Placa":
                //Texto que muestra el valor del que se esta actualizando.
                txtEditar.setText("Numero de Placa");

                // Establecer el texto actual del TextView en el EditText
                editText.setText(placaTextView.getText().toString());

                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obtener el nuevo texto del EditText
                        String newText = editText.getText().toString();

                        // Actualizar el TextView con el nuevo texto
                        placaTextView.setText(newText);

                        // Cerrar el diálogo
                        d_edit.dismiss();
                    }
                });
                break;
            case "Ano":
                //Texto que muestra el valor del que se esta actualizando.
                txtEditar.setText("Año del Vehiculo");

                // Establecer el texto actual del TextView en el EditText
                editText.setText(añoTextView.getText().toString());

                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obtener el nuevo texto del EditText
                        String newText = editText.getText().toString();

                        // Actualizar el TextView con el nuevo texto
                        añoTextView.setText(newText);

                        // Cerrar el diálogo
                        d_edit.dismiss();
                    }
                });
                break;
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el diálogo
                d_edit.dismiss();
            }
        });
    }
}