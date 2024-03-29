package com.example.uniride.publicationCreation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uniride.MainActivityFragment;
import com.example.uniride.MapsActivity;
import com.example.uniride.MapsClient;
import com.example.uniride.MyTravelsCreated;
import com.example.uniride.R;
import com.example.uniride.components.DialogElement;
import com.example.uniride.components.SnackBarElement;
import com.example.uniride.functions.FormatDateName;
import com.example.uniride.functions.SplitDirection;
import com.example.uniride.publicationCreation.DescriptionPublication;
import com.example.uniride.publicationCreation.SelectCuota;
import com.example.uniride.publicationCreation.SelectDate;
import com.example.uniride.publicationCreation.SelectStudents;
import com.example.uniride.publicationCreation.SelectTime;
import com.example.uniride.selectLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class CreatePublicationDetails extends AppCompatActivity {

    TextView fechaSalida, horaSalida, puntoInicio, campusDestino, asientos, cuota, descripcion;
    Button btnCrearRaite;
    ImageView btnSalir;
    RelativeLayout btnEditarFecha, btnEditarHora, btnPuntoInicio, btnEditarCampusDestino, btnEditarAsientos, btnEditarCuota, btnEditarDescripcion, btnVerRutaMapa;
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
        puntoInicio = (TextView)findViewById(R.id.puntoInicio);
        campusDestino = (TextView)findViewById(R.id.campusDestino);
        asientos = (TextView)findViewById(R.id.asientos);
        cuota = (TextView)findViewById(R.id.cuota);
        descripcion = (TextView)findViewById(R.id.descripcion);
        btnCrearRaite = (Button)findViewById(R.id.btnCrearRaite);
        btnSalir = (ImageView)findViewById(R.id.Salir);
        btnEditarFecha = (RelativeLayout)findViewById(R.id.cPulsaEditar);
        btnEditarHora = (RelativeLayout)findViewById(R.id.cPulsaEditar2);
        btnPuntoInicio = (RelativeLayout)findViewById(R.id.cPulsaEditar31);
        btnEditarCampusDestino = (RelativeLayout)findViewById(R.id.cPulsaEditar32);
        btnEditarAsientos = (RelativeLayout)findViewById(R.id.cPulsaEditar4);
        btnEditarCuota = (RelativeLayout)findViewById(R.id.cPulsaEditar5);
        btnEditarDescripcion = (RelativeLayout)findViewById(R.id.cPulsaEditar6);
        btnVerRutaMapa = (RelativeLayout)findViewById(R.id.btnVerRutaMapa);

        SharedPreferences preferences = getSharedPreferences("editarTodaLaPublicacion", Context.MODE_PRIVATE);
        boolean editarFirst = getIntent().getBooleanExtra("editar", false);
        if(editarFirst){
            btnCrearRaite.setText("Guardar Cambios");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("IdPublicacionAEditar", getIntent().getStringExtra("idItem"));
            editor.putBoolean("editarToda",true);
            editor.commit();
        }

        if(preferences.getBoolean("editarToda", false)){
            btnCrearRaite.setText("Guardar Cambios");
        }

        //-----------Setear la informacion---------
        if(getIntent().getStringExtra("idItem") != null){
            //Recibir la informacion de FireStore
            fStore.collection("publications").document(getIntent().getStringExtra("idItem")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    datos = (HashMap<String, Object>) value.getData();
                    String fecha = datos.get("datePublication").toString();
                    String direccion = datos.get("direccionPartida").toString();

                    fechaSalida.setText(cf.getDiaSemana(fecha)+" "+fecha.substring(0,2)+" de "+cf.getNombreMes(fecha)+" del "+fecha.substring(6));
                    horaSalida.setText(datos.get("timePublication").toString());
                    puntoInicio.setText(new SplitDirection().getDirection(direccion));
                    campusDestino.setText(datos.get("campusDestination").toString());
                    asientos.setText(datos.get("travelSeating").toString());
                    cuota.setText("$"+datos.get("travelPrice").toString());
                    descripcion.setText(datos.get("travelDescription").toString());
                }
            });
        }else{
            //Recibir la informacion de las vistas anteriores
            Bundle c = getIntent().getExtras();
            datos = (HashMap<String, Object>) c.getSerializable("datos");

            String fecha = datos.get("datePublication").toString();
            String direccion = datos.get("direccionPartida").toString();

            fechaSalida.setText(cf.getDiaSemana(fecha)+" "+fecha.substring(0,2)+" de "+cf.getNombreMes(fecha)+" del "+fecha.substring(6));
            horaSalida.setText(datos.get("timePublication").toString());
            puntoInicio.setText(new SplitDirection().getDirection(direccion));
            campusDestino.setText(datos.get("campusDestination").toString());
            asientos.setText(datos.get("travelSeating").toString());
            cuota.setText("$"+datos.get("travelPrice").toString());
            descripcion.setText(datos.get("travelDescription").toString());
        }
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

        btnPuntoInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent d = new Intent(getApplicationContext(), MapsActivity.class);
                d.putExtra("editar", true);
                d.putExtra("activity","createTravelDetails");
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
                d.putExtra("activity","createTravelDetails");
                d.putExtras(cDatos);
                startActivity(d);
            }
        });

        btnVerRutaMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ir al activity que va a hacer Ernesto con unicamente el mapa con los dos puntos y la ruta
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);
                Intent d = new Intent(getApplicationContext(), MapsClient.class);
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

                Intent d = new Intent(getApplicationContext(), DescriptionPublication.class);
                d.putExtra("editar", true);
                d.putExtras(cDatos);
                startActivity(d);

            }
        });

        btnCrearRaite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(preferences.getBoolean("editarToda", false)){
                    fStore.collection("publications").document(preferences.getString("IdPublicacionAEditar", "")).update(datos).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear().apply();
                            snackBar.showSnackBar(getResources().getColor(R.color.purple_500),"Cambios Realizados Correctamente");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(), MyTravelsCreated.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                    finish();
                                }
                            },1200);
                        }
                    });
                }else{
                    Map<String, Object> publication = datos;
                    publication.put("IdCreator", fAuth.getUid());
                    publication.put("State", "Activo");
                    publication.put("IdPublication", "");


                    fStore.collection("publications").add(publication).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            fStore.collection("publications").document(documentReference.getId()).update("IdPublication", documentReference.getId());
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