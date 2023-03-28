package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class selectLocation extends AppCompatActivity {

    List<locationElement> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        init();

    }

    public void init() {
        elements = new ArrayList<>();
        elements.add(new locationElement("Facultad de Ingenieria Electromecan", "Campus el Naranjo"));
        elements.add(new locationElement("Facultad de Administracion y Contabilidad", "Campus el Naranjo"));
        elements.add(new locationElement("Bachillerato 8", "Campus San Pedrito"));


        ListAdapterLocation listAdapter = new ListAdapterLocation(elements, this, new ListAdapterLocation.OnItemClickListener() {
            @Override
            public void onItemClick(locationElement item) {
                guardarDestino(item.getnCampus());
                Intent i = new Intent(selectLocation.this, MainActivityFragment.class);
                startActivity(i);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.locationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    private void guardarDestino(String campus) {
        SharedPreferences datosUsuario = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=datosUsuario.edit();
        editor.putString("campus",campus);
        editor.commit();
    }
}