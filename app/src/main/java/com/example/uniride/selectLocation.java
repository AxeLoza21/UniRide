package com.example.uniride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class selectLocation extends AppCompatActivity {

    List<locationElement> elements;
    ListAdapterLocation listAdapter;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        init();
        setupSearchView();

    }

    public void init() {
        elements = new ArrayList<>();
        elements.add(new locationElement("Manzanillo, Colima", "Campus el Naranjo", "FIE FCAM FACIMAR facultad ingenieria software mecanica electromecanica electrica mecatronica contabilidad administracion oceanico ingeniero licenciatura ingeniero software"));
        elements.add(new locationElement("Manzanillo, Colima", "Campus Barrio 3", "CUBAM gastronomia comercio exterior aduanas licenciatura "));
        elements.add(new locationElement("Manzanillo, Colima", "Campus San Pedrito", "Bach 8 Bach 9 Bach 10 bachillerato 8 bachillerato 9 bachillerato 10"));

        listAdapter = new ListAdapterLocation(elements, this, new ListAdapterLocation.OnItemClickListener() {
            @Override
            public void onItemClick(locationElement item) {
                guardarDestino(item.getnCampus());
                String campus = item.getnCampus();
                DocumentReference documentReference = fStore.collection("users").document(fAuth.getUid());
                Map<String, Object> user = new HashMap<>();
                user.put("destinationLocation", campus);
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(selectLocation.this, MainActivityFragment.class);
                        startActivity(i);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
            }
        });
        RecyclerView recyclerView = findViewById(R.id.locationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }


    private void setupSearchView() {
        EditText searchView = findViewById(R.id.searchView);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable newText) {
                listAdapter.getFilter().filter(newText);
            }
        });
    }


    private void guardarDestino(String campus) {
        SharedPreferences datosUsuario = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=datosUsuario.edit();
        editor.putString("campus",campus);
        editor.commit();

    }

}