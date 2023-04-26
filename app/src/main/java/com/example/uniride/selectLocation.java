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
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class selectLocation extends AppCompatActivity {

    List<locationElement> elements;
    ListAdapterLocation listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

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
                Intent i = new Intent(selectLocation.this, MainActivityFragment.class);
                startActivity(i);
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.locationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
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