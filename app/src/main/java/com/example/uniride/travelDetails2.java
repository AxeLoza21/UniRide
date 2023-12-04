package com.example.uniride;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uniride.functions.CalculateAge;
import com.example.uniride.functions.FormatDateName;
import com.example.uniride.functions.SplitDirection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class travelDetails2 extends AppCompatActivity {

    TextView dateAndTime, startingLocation, destinationLocation, nameCreator, ageCreator, description, time, price, seating, yearCar, colorCar, brandCar;
    ImageView btnBack, imgCreator;
    Button btnPedirRaite;
    LinearLayout cTxtYourPublication, cTxtAlreadyRequested;
    RelativeLayout seemap;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    double deslat, deslng ,orilat, orilng;
    String statePublication;

    CalculateAge cE;
    FormatDateName cf;
    HashMap<String, Object> datos = new HashMap<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_details2);

        cE = new CalculateAge();
        cf = new FormatDateName();


        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        cTxtAlreadyRequested = (LinearLayout)findViewById(R.id.cTexto2);
        cTxtYourPublication = (LinearLayout)findViewById(R.id.cTexto);
        dateAndTime = (TextView)findViewById(R.id.fechaYhoraSalida);
        startingLocation = (TextView)findViewById(R.id.startingLocation);
        destinationLocation = (TextView)findViewById(R.id.destinationLocation);
        nameCreator = (TextView)findViewById(R.id.nameUser);
        brandCar = (TextView)findViewById(R.id.brandCar);
        yearCar = (TextView)findViewById(R.id.yearCar);
        colorCar = (TextView)findViewById(R.id.colorCar);
        ageCreator = (TextView)findViewById(R.id.ageUser);
        description = (TextView)findViewById(R.id.description);
        seemap = (RelativeLayout)findViewById(R.id.seemap);
        time = (TextView)findViewById(R.id.salida);
        price = (TextView)findViewById(R.id.cuota);
        seating = (TextView)findViewById(R.id.seats);
        imgCreator = (ImageView) findViewById(R.id.imgUser);
        btnBack = (ImageView)findViewById(R.id.btn_back);
        btnPedirRaite = (Button)findViewById(R.id.btnPedirRaite);
        final String originActivity = getIntent().getStringExtra("originActivity");
        final String idPublication = getIntent().getStringExtra("idItem");

        setInformation(idPublication);


        seemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle createTravel = new Bundle();
                datos.put("OriLat", orilat);
                datos.put("OriLng", orilng);
                datos.put("DesLat", deslat);
                datos.put("DesLng", deslng);
                createTravel.putSerializable("datos", datos);
                Intent i = new Intent(travelDetails2.this, MapsClient.class);
                i.putExtra("create", true);
                i.putExtras(createTravel);
                startActivity(i);
            }
        });

        btnPedirRaite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle createTravel = new Bundle();
                datos.put("OriLat", orilat);
                datos.put("OriLng", orilng);
                datos.put("DesLat", deslat);
                datos.put("DesLng", deslng);
                datos.put("IdPublication", idPublication);
                createTravel.putSerializable("datos", datos);
                Intent i = new Intent(travelDetails2.this, MapClientSelectLocation.class);//antes travel_sent
                i.putExtra("create", true);
                i.putExtras(createTravel);
                startActivity(i);
            }
        });

        cTxtYourPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (originActivity.equals("HomeFragment")){
                    Intent i = new Intent(travelDetails2.this, MyTravelsCreated.class);
                    i.putExtra("state",statePublication);
                    startActivity(i);
                }else if(originActivity.equals("MyTravelsCreated")){
                    onBackPressed();
                }

            }
        });

        cTxtAlreadyRequested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mandar al Fragment de Viajes
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setInformation(String idPublication) {

        fStore.collection("publications").document(idPublication).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //-----Obtener Datos del creador del viaje Mediante su ID-----
                String userId = value.getString("IdCreator");
                statePublication = value.getString("State");
                fStore.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null){
                            nameCreator.setText(value.getString("username"));
                            ageCreator.setText(cE.calcularEdad(value.getString("birthDay")));
                            if (value.getString("photo").isEmpty()) {
                                Picasso.get().load(R.drawable.person_2).into(imgCreator);
                            } else {
                                Picasso.get().load(value.getString("photo")).into(imgCreator);
                            }
                            CollectionReference carsRef = fStore.collection("users")
                                    .document(userId)
                                    .collection("cars");
                            carsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot carDocument : task.getResult()) {
                                            // documento (car) en la subcolección "cars"
                                            String make = carDocument.getString("make");
                                            String carModel = carDocument.getString("model");
                                            String color = carDocument.getString("color");
                                            String year = carDocument.getString("year");
                                            String combinedText = make + " " + carModel;
                                            yearCar.setText(year);
                                            colorCar.setText(color);
                                            brandCar.setText(combinedText);
                                            // ... y así sucesivamente para los demás campos del auto
                                        }
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });
                        }

                    }
                });
                //------------------------------------------------------------


                //----------Setear la informacion restante del Viaje----------
                String date = value.getString("datePublication");
                String timee = value.getString("timePublication");
                deslat = value.getDouble("DesLat");
                deslng = value.getDouble("DesLng");
                orilat = value.getDouble("OriLat");
                orilng = value.getDouble("OriLng");
                String direccion = value.getString("direccionPartida");
                startingLocation.setText(new SplitDirection().getDirection(direccion));
                dateAndTime.setText(cf.getDiaSemana(date)+" "+date.substring(0,2)+", "+cf.getNombreMes(date)+" - "+timee);
                destinationLocation.setText(value.getString("campusDestination"));
                description.setText(value.getString("travelDescription"));
                time.setText(timee);
                price.setText("$"+value.getString("travelPrice"));
                seating.setText(value.getString("travelSeating"));

                //----------------La publicacion es tuya----------------
                if(value.getString("IdCreator").equals(mAuth.getUid())){
                    btnPedirRaite.setVisibility(View.GONE);
                    cTxtAlreadyRequested.setVisibility(View.GONE);
                    cTxtYourPublication.setVisibility(View.VISIBLE);

                }

                //----------------Ya solicitaste este viaje----------------
                fStore.collection("users").document(mAuth.getUid()).collection("myRequestTo").whereEqualTo("IdPublication", idPublication).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().getDocuments().size() == 1){
                            Log.e("Error", "Ya has solicitado este viaje");
                            btnPedirRaite.setVisibility(View.GONE);
                            cTxtYourPublication.setVisibility(View.GONE);
                            cTxtAlreadyRequested.setVisibility(View.VISIBLE);
                        }else{
                            Log.e("Error", "Todavia no solicitas es viaje");
                        }
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
}