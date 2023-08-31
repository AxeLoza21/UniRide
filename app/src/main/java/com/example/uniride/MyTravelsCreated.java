package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MyTravelsCreated extends AppCompatActivity {

    ImageView btnExit;
    //ecyclerView rMyTravels;
    CardView circleState1, circleState2, btnEditarPublicacion;
    RelativeLayout btnPAndF, btnActivo;

    Animation aCircleState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travels_created);

        btnExit = (ImageView)findViewById(R.id.btn_back);
        btnActivo = (RelativeLayout) findViewById(R.id.btnActivo);
        btnPAndF = (RelativeLayout)findViewById(R.id.btnPAndF);
        aCircleState = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.circle_state);
        circleState1 = (CardView)findViewById(R.id.circleState);
        circleState2 = (CardView)findViewById(R.id.circleState2);
        //btnEditarPublicacion = (CardView)findViewById(R.id.cVerPublicacion);

        String statePublicacion = getIntent().getStringExtra("state");
        if(statePublicacion.equals("Activo")){
            circleState1.setVisibility(View.VISIBLE);
            circleState2.setVisibility(View.GONE);
            btnPAndF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MyTravelsCreated.this, "Hola", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            circleState1.setVisibility(View.GONE);
            circleState2.setVisibility(View.VISIBLE);
            btnActivo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MyTravelsCreated.this, "Hola", Toast.LENGTH_SHORT).show();
                }
            });
        }



        /*btnEditarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(getApplicationContext(), CreateTravelDetails.class);
                e.putExtra("idItem", getIntent().getStringExtra("idItem"));
                startActivity(e);
            }
        });*/

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                circleState1.startAnimation(aCircleState);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}