package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uniride.adapter.PublicationAdapter;
import com.example.uniride.adapter.RequestAdapter;
import com.example.uniride.model.Publications;
import com.example.uniride.model.Request;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MyTravelsCreated extends AppCompatActivity {

    ImageView btnExit;
    CardView circleState1, circleState2;
    RelativeLayout btnPAndF, btnActivo;
    LinearLayout btnVerPublicacion, btnEditarPublicacion, btnEliminarPublicacion, txtNoSolicitudes;

    RecyclerView rSolicitudes;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    RequestAdapter requestAdapter;

    Animation aCircleState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travels_created);

        //Recibir datos de patallas anteriores
        String statePublicacion = getIntent().getStringExtra("state");
        String idPublicacion = getIntent().getStringExtra("idItem");

        rSolicitudes = (RecyclerView)findViewById(R.id.rSolicitudes);
        rSolicitudes.setLayoutManager(new LinearLayoutManager(this));
        rSolicitudes.setHasFixedSize(true);

        btnExit = (ImageView)findViewById(R.id.btn_back);
        btnActivo = (RelativeLayout) findViewById(R.id.btnActivo);
        btnPAndF = (RelativeLayout)findViewById(R.id.btnPAndF);
        btnVerPublicacion = (LinearLayout)findViewById(R.id.btnVerPublicacion);
        aCircleState = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.circle_state);
        circleState1 = (CardView)findViewById(R.id.circleState);
        circleState2 = (CardView)findViewById(R.id.circleState2);
        txtNoSolicitudes = (LinearLayout)findViewById(R.id.cTxtNoSolicitudes);
        //btnEditarPublicacion = (CardView)findViewById(R.id.cVerPublicacion);


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

        Query query = fStore.collection("solicitud").whereEqualTo("IdPublication", idPublicacion);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size() == 0){
                    rSolicitudes.setVisibility(View.GONE);
                    txtNoSolicitudes.setVisibility(View.VISIBLE);
                }
            }
        });

        init(query);


        btnVerPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(MyTravelsCreated.this, travelDetails2.class);
                e.putExtra("idItem", idPublicacion);
                e.putExtra("originActivity", "MyTravelsCreated");
                startActivity(e);
            }
        });

        /*btnEditarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(getApplicationContext(), CreatePublicationDetails.class);
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



    private void init(Query query) {
        FirestoreRecyclerOptions<Request> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Request>().setQuery(query, Request.class).build();

        requestAdapter = new RequestAdapter(firestoreRecyclerOptions);
        requestAdapter.notifyDataSetChanged();
        rSolicitudes.setAdapter(requestAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestAdapter.stopListening();
    }
}