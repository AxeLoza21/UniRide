package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AlertAddCar extends AppCompatActivity {

    ImageView btnRegresar;
    Button btnComenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_add_car);

        btnRegresar = (ImageView)findViewById(R.id.regresar);
        btnComenzar = (Button)findViewById(R.id.btnComenzar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myString = "MyVehicles";
                Intent intent = new Intent(getApplicationContext(), FormCar.class);
                intent.putExtra("Vehicles", myString);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            finish();
    }
}