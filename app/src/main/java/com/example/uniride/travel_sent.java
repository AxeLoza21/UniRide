package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class travel_sent extends AppCompatActivity {

    Button btnVolverHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_sent);

        btnVolverHome = (Button)findViewById(R.id.btnVolverHome);

        btnVolverHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(travel_sent.this, MainActivityFragment.class);
                startActivity(i);
                finish();
            }
        });
    }
}