package com.example.uniride;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uniride.functions.CalculateAge;
import com.example.uniride.functions.FormatDateName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class travelDetails2 extends AppCompatActivity {

    TextView dateAndTime, startingLocation, destinationLocation, nameCreator, ageCreator, description, time, price, seating;
    ImageView btnBack, imgCreator;
    Button btnPedirRaite;
    LinearLayout cTxtYourPublication;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    CalculateAge cE;
    FormatDateName cf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_details2);

        cE = new CalculateAge();
        cf = new FormatDateName();


        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        cTxtYourPublication = (LinearLayout)findViewById(R.id.cTexto);
        dateAndTime = (TextView)findViewById(R.id.fechaYhoraSalida);
        startingLocation = (TextView)findViewById(R.id.startingLocation);
        destinationLocation = (TextView)findViewById(R.id.destinationLocation);
        nameCreator = (TextView)findViewById(R.id.nameUser);
        ageCreator = (TextView)findViewById(R.id.ageUser);
        description = (TextView)findViewById(R.id.description);
        time = (TextView)findViewById(R.id.salida);
        price = (TextView)findViewById(R.id.cuota);
        seating = (TextView)findViewById(R.id.seats);
        imgCreator = (ImageView) findViewById(R.id.imgUser);
        btnBack = (ImageView)findViewById(R.id.btn_back);
        btnPedirRaite = (Button)findViewById(R.id.btnPedirRaite);
        final String originActivity = getIntent().getStringExtra("originActivity");
        if ("TravelAdapter".equals(originActivity)) {
            btnPedirRaite.setVisibility(View.GONE);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnPedirRaite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(travelDetails2.this, travel_sent.class);
                startActivity(i);
            }
        });

        final String idPublication = getIntent().getStringExtra("idItem");
        setInformation(idPublication);
    }

    private void setInformation(String idPublication) {

        fStore.collection("publications").document(idPublication).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //-----Obtener Datos del creador del viaje Mediante su ID-----
                fStore.collection("users").document(value.getString("IdCreator")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        nameCreator.setText(value.getString("username"));
                        ageCreator.setText(cE.calcularEdad(value.getString("birthDay")));
                        if (value.getString("photo").isEmpty()) {
                            Picasso.get().load(R.drawable.person_2).into(imgCreator);
                        } else {
                            Picasso.get().load(value.getString("photo")).into(imgCreator);
                        }
                    }
                });
                //------------------------------------------------------------


                //----------Setear la informacion restante del Viaje----------
                String date = value.getString("datePublication");
                String timee = value.getString("timePublication");
                dateAndTime.setText(cf.getDiaSemana(date)+" "+date.substring(0,2)+", "+cf.getNombreMes(date)+" - "+timee);
                destinationLocation.setText(value.getString("campusDestination"));
                description.setText(value.getString("travelDescription"));
                time.setText(timee);
                price.setText("$"+value.getString("travelPrice"));
                seating.setText(value.getString("travelSeating"));

                //----------------La publicacion es tuya----------------
                if(value.getString("IdCreator").equals(mAuth.getUid())){
                    btnPedirRaite.setVisibility(View.GONE);
                    cTxtYourPublication.setVisibility(View.VISIBLE);

                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}