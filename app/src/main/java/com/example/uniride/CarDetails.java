package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

                    int position = colores.indexOf(color);
                    int position2 = tipos.indexOf(tipo);
                    spColorVehiculo.setSelection(position);
                    spTipoVehiculo.setSelection(position2);

                    // Asignar los valores a los TextView correspondientes
                    marcaTextView.setText(marca);
                    marcaTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Crear el diálogo personalizado
                            Dialog dialog = new Dialog(CarDetails.this);
                            dialog.setContentView(R.layout.dialog_edit_text);

                            // Obtener referencias a los elementos de la interfaz de usuario en el diálogo
                            EditText editText = dialog.findViewById(R.id.edit_text);
                            Button buttonOk = dialog.findViewById(R.id.button_ok);
                            Button buttonCancel = dialog.findViewById(R.id.button_cancel);

                            // Establecer el texto actual del TextView en el EditText
                            editText.setText(marcaTextView.getText().toString());

                            // Configurar los botones del diálogo
                            buttonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Obtener el nuevo texto del EditText
                                    String newText = editText.getText().toString();

                                    // Actualizar el TextView con el nuevo texto
                                    marcaTextView.setText(newText);

                                    // Cerrar el diálogo
                                    dialog.dismiss();
                                }
                            });

                            buttonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Cerrar el diálogo
                                    dialog.dismiss();
                                }
                            });

                            // Mostrar el diálogo
                            dialog.show();

                        }
                    });

                    modeloTextView.setText(modelo);
                    modeloTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Dialog dialog = new Dialog(CarDetails.this);
                            dialog.setContentView(R.layout.dialog_edit_text);

                            // Obtener referencias a los elementos de la interfaz de usuario en el diálogo
                            EditText editText = dialog.findViewById(R.id.edit_text);
                            Button buttonOk = dialog.findViewById(R.id.button_ok);
                            Button buttonCancel = dialog.findViewById(R.id.button_cancel);

                            // Establecer el texto actual del TextView en el EditText
                            editText.setText(modeloTextView.getText().toString());

                            // Configurar los botones del diálogo
                            buttonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Obtener el nuevo texto del EditText
                                    String newText = editText.getText().toString();

                                    // Actualizar el TextView con el nuevo texto
                                    modeloTextView.setText(newText);

                                    // Cerrar el diálogo
                                    dialog.dismiss();
                                }
                            });

                            buttonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Cerrar el diálogo
                                    dialog.dismiss();
                                }
                            });

                            // Mostrar el diálogo
                            dialog.show();
                        }
                    });

                    placaTextView.setText(placa);
                    placaTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Dialog dialog = new Dialog(CarDetails.this);
                            dialog.setContentView(R.layout.dialog_edit_text);

                            // Obtener referencias a los elementos de la interfaz de usuario en el diálogo
                            EditText editText = dialog.findViewById(R.id.edit_text);
                            Button buttonOk = dialog.findViewById(R.id.button_ok);
                            Button buttonCancel = dialog.findViewById(R.id.button_cancel);

                            // Establecer el texto actual del TextView en el EditText
                            editText.setText(placaTextView.getText().toString());

                            // Configurar los botones del diálogo
                            buttonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Obtener el nuevo texto del EditText
                                    String newText = editText.getText().toString();

                                    // Actualizar el TextView con el nuevo texto
                                    placaTextView.setText(newText);

                                    // Cerrar el diálogo
                                    dialog.dismiss();
                                }
                            });

                            buttonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Cerrar el diálogo
                                    dialog.dismiss();
                                }
                            });

                            // Mostrar el diálogo
                            dialog.show();
                        }
                    });

                    añoTextView.setText(anioStr); // establecer en TextView
                    añoTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Dialog dialog = new Dialog(CarDetails.this);
                            dialog.setContentView(R.layout.dialog_edit_text);

                            // Obtener referencias a los elementos de la interfaz de usuario en el diálogo
                            EditText editText = dialog.findViewById(R.id.edit_text);
                            Button buttonOk = dialog.findViewById(R.id.button_ok);
                            Button buttonCancel = dialog.findViewById(R.id.button_cancel);

                            // Establecer el texto actual del TextView en el EditText
                            editText.setText(añoTextView.getText().toString());

                            // Configurar los botones del diálogo
                            buttonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Obtener el nuevo texto del EditText
                                    String newText = editText.getText().toString();

                                    // Actualizar el TextView con el nuevo texto
                                    añoTextView.setText(newText);

                                    // Cerrar el diálogo
                                    dialog.dismiss();
                                }
                            });

                            buttonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Cerrar el diálogo
                                    dialog.dismiss();
                                }
                            });

                            // Mostrar el diálogo
                            dialog.show();
                        }
                    });

                }
            }
        });
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        // Mostrar un mensaje de error
                        Toast.makeText(CarDetails.this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}