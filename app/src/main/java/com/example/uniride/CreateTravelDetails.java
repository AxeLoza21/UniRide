package com.example.uniride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uniride.components.DialogElement;
import com.example.uniride.components.SnackBarElement;
import com.example.uniride.functions.FormatDateName;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateTravelDetails extends AppCompatActivity {

    TextView fechaSalida, horaSalida, campusDestino, asientos, cuota, descripcion;
    Button btnCrearRaite;
    ImageView btnSalir;
    RelativeLayout btnEditarFecha, btnEditarHora, btnEditarCampusDestino, btnEditarAsientos, btnEditarCuota, btnEditarDescripcion;
    HashMap<String, Object> datos = new HashMap<>();

    DialogElement dialog;
    FormatDateName cf;
    SnackBarElement snackBar;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_travel_details);

        dialog = new DialogElement(this);
        cf = new FormatDateName();
        snackBar = new SnackBarElement(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fechaSalida = (TextView)findViewById(R.id.fechaSalida);
        horaSalida = (TextView)findViewById(R.id.horaSalida);
        campusDestino = (TextView)findViewById(R.id.campusDestino);
        asientos = (TextView)findViewById(R.id.asientos);
        cuota = (TextView)findViewById(R.id.cuota);
        descripcion = (TextView)findViewById(R.id.descripcion);
        btnCrearRaite = (Button)findViewById(R.id.btnCrearRaite);
        btnSalir = (ImageView)findViewById(R.id.Salir);
        btnEditarFecha = (RelativeLayout)findViewById(R.id.cPulsaEditar);
        btnEditarHora = (RelativeLayout)findViewById(R.id.cPulsaEditar2);
        btnEditarCampusDestino = (RelativeLayout)findViewById(R.id.cPulsaEditar3);
        btnEditarAsientos = (RelativeLayout)findViewById(R.id.cPulsaEditar4);
        btnEditarCuota = (RelativeLayout)findViewById(R.id.cPulsaEditar5);
        btnEditarDescripcion = (RelativeLayout)findViewById(R.id.cPulsaEditar6);

        //-----------Setear la informacion---------

        //Recibir la informacion de las vistas anteriores
        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");
        String fecha = datos.get("datePublication").toString();

        fechaSalida.setText(cf.getDiaSemana(fecha)+" "+fecha.substring(0,2)+" de "+cf.getNombreMes(fecha)+" del "+fecha.substring(6));
        horaSalida.setText(datos.get("timePublication").toString());
        campusDestino.setText(datos.get("campusDestination").toString());
        asientos.setText(datos.get("travelSeating").toString());
        cuota.setText("$"+datos.get("travelPrice").toString());
        descripcion.setText(datos.get("travelDescription").toString());

        //-------------------------------------------

        btnEditarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent d = new Intent(getApplicationContext(), SelectDate.class);
                d.putExtra("editar", true);
                d.putExtras(cDatos);
                startActivity(d);

            }
        });

        btnEditarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent d = new Intent(getApplicationContext(), SelectTime.class);
                d.putExtra("editar", true);
                d.putExtras(cDatos);
                startActivity(d);

            }
        });

        btnEditarCampusDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent d = new Intent(getApplicationContext(), selectLocation.class);
                d.putExtra("editar", true);
                d.putExtra("create", true);
                d.putExtras(cDatos);
                startActivity(d);
            }
        });

        btnEditarAsientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent d = new Intent(getApplicationContext(), SelectStudents.class);
                d.putExtra("editar", true);
                d.putExtras(cDatos);
                startActivity(d);

            }
        });

        btnEditarCuota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent d = new Intent(getApplicationContext(), SelectCuota.class);
                d.putExtra("editar", true);
                d.putExtras(cDatos);
                startActivity(d);
            }
        });

        btnEditarDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent d = new Intent(getApplicationContext(), DescriptionTravel.class);
                d.putExtra("editar", true);
                d.putExtras(cDatos);
                startActivity(d);

            }
        });

        btnCrearRaite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                DocumentReference documentReference = fStore.collection("publications").document();
                Map<String, Object> publication = datos;
                publication.put("IdCreator", fAuth.getUid());
                publication.put("latCampus", "");
                publication.put("longCampus", "");
                publication.put("latInitation", "");
                publication.put("longInitation", "");

                documentReference.set(publication).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        snackBar.showSnackBar(getResources().getColor(R.color.green),"Publicacion creada Correctamente");
                        new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              startActivity(new Intent(getApplicationContext(), MainActivityFragment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                              finish();
                          }
                        },1200);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackBar.showSnackBar(getResources().getColor(R.color.red),"Error al crear la Publicacion");
                    }
                });

            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        dialog.showDialogWarningExit();

    }

}