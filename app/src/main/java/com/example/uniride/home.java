package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {

    CardView btn_changeLocation;
    Button details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_changeLocation = (CardView)findViewById(R.id.btn_changeLocation);
        details = (Button)findViewById(R.id.irDetails);

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
    }
}