package com.example.uniride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.uniride.fragments.AlertAddCarFragment;
import com.example.uniride.fragments.Chatfragment;
import com.example.uniride.fragments.CreateFragment;
import com.example.uniride.fragments.HomeFragment;
import com.example.uniride.fragments.ListaUsuariosFragment;
import com.example.uniride.fragments.PerfilFragment;
import com.example.uniride.fragments.TravelFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivityFragment extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    CreateFragment createFragment = new CreateFragment();
    TravelFragment travelFragment = new TravelFragment();
    PerfilFragment perfilFragment = new PerfilFragment();
    ListaUsuariosFragment listaUsuariosFragment = new ListaUsuariosFragment();

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
                case R.id.chat:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ListaUsuariosFragment()).commit();
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
                    Bundle args = new Bundle();
                    if (!task.getResult().isEmpty()) {
                        hasCar = true;
                    } else {
                        hasCar = false;
                    }
                    args.putBoolean("hasCar", hasCar);
                    perfilFragment.setArguments(args);
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