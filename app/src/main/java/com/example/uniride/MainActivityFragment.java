package com.example.uniride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivityFragment extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    CreateFragment createFragment = new CreateFragment();
    TravelFragment travelFragment = new TravelFragment();
    PerfilFragment perfilFragment = new PerfilFragment();

    AlertAddCarFragment alertAddCarFragment = new AlertAddCarFragment();
    boolean hasCar = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                    break;
                case R.id.create:
                    if (hasCar) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, createFragment).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, alertAddCarFragment).commit();
                    }
                    break;
                case R.id.travel:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,travelFragment).commit();
                    break;
                case R.id.perfil:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,perfilFragment).commit();
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfUserHasCar();
    }

    private void checkIfUserHasCar() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference carsCollectionRef = db.collection("users").document(userId).collection("cars");

        carsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        hasCar = true;
                    } else {
                        hasCar = false;
                    }
                } else {
                    Log.d(TAG, "Error obteniendo documentos: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }
}