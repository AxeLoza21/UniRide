package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {

    CardView btn_changeLocation;
    Button details, formcar , alertCar ,fragments;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_changeLocation = (CardView)findViewById(R.id.btn_changeLocation);
        details = (Button)findViewById(R.id.irDetails);
        formcar = (Button)findViewById(R.id.botonformcar);
        alertCar = (Button)findViewById(R.id.botoncar);
        fragments = (Button)findViewById(R.id.botonfragment);

        btn_changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this, selectLocation.class);
                startActivity(i);

            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this, travelDetails2.class);
                startActivity(i);
            }
        });
        formcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this, FormCar.class);
                startActivity(i);
            }
        });
        alertCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this, AlertAddCar.class);
                startActivity(i);
            }
        });
        fragments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this, MainActivityFragment.class);
                startActivity(i);
            }
        });
    }
}