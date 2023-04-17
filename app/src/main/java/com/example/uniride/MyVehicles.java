package com.example.uniride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uniride.adapter.CarAdapter;
import com.example.uniride.model.Car;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MyVehicles extends AppCompatActivity {
    RecyclerView mRecycler;
    CarAdapter mAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ImageView btnAddCar,btnExit;
    TextView limite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);
        mAuth = FirebaseAuth.getInstance();
        btnAddCar = (ImageView) findViewById(R.id.btn_add);
        limite = (TextView) findViewById(R.id.txtLimite);
        btnExit = (ImageView)findViewById(R.id.btnexit);


        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Esperar
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Desbloquear la pantalla
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        // Navegar hacia la actividad FormCar.class con el string
                        String myString = "MyVehicles";
                        Intent intent = new Intent(getApplicationContext(), FormCar.class);
                        intent.putExtra("Vehicles", myString);
                        startActivity(intent);
                        finish();

                    }
                }, 0);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // Esperar
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Desbloquear la pantalla
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        onBackPressed();

                    }
                }, 0);
            }
        });

        db = FirebaseFirestore.getInstance();

        db.collection("users").document(mAuth.getUid()).collection("cars")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = task.getResult().size();
                            if (count >= 3) {
                                // Deshabilitar el botón y cambiar su apariencia
                                //cambiar el icono
                                btnAddCar.setImageDrawable(getResources().getDrawable(R.drawable.car4));
                                btnAddCar.setEnabled(false);
                                btnAddCar.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                                limite.setVisibility(View.VISIBLE);

                            } else {
                                // Permitir agregar un nuevo auto
                                btnAddCar.setEnabled(true);
                                btnAddCar.clearColorFilter();
                            }
                        } else {
                            Log.d(TAG, "Error obteniendo el documento: ", task.getException());
                        }
                    }
                });
        mRecycler = findViewById(R.id.recyclerViewSingle);
        //mRecycler.setLayoutManager(new LinearLayoutManager(this));
        //arreglar problema del recyclerview
        mRecycler.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = db.collection("users").document(userId).collection("cars");

        FirestoreRecyclerOptions<Car> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Car>().setQuery(query, Car.class).build();
        mAdapter = new CarAdapter(firestoreRecyclerOptions, this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
    //arreglar problema del recyclerview
    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String refresh = "MyVehicles";
        Intent intent = new Intent(getApplicationContext(), MainActivityFragment.class);
        intent.putExtra("Vehicles", refresh);
        startActivity(intent);
        finish();
        //Intent intent = new Intent(getApplicationContext(), FormCar.class);
        //intent.putExtra("Vehicles", myString);
        //startActivity(intent);
        //finish();
    }
}