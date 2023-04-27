package com.example.uniride;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    View vista;
    List<raiteElement> elements;
    CardView btn_changeLocation;
    TextView location;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_home, container, false);
        btn_changeLocation = (CardView)vista.findViewById(R.id.btn_changeLocation);
        recyclerView = vista.findViewById(R.id.raitesRecyclerView);
        location = (TextView)vista.findViewById(R.id.location);

        SharedPreferences datosUsuario = vista.getContext().getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        location.setText(datosUsuario.getString("campus", "???Campus???"));
        init();

        btn_changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                Intent i = new Intent(getActivity(), selectLocation.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        return vista;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableButtons();
    }

    public void init() {
        elements = new ArrayList<>();
        elements.add(new raiteElement("Ernesto Manuel Jimenez Pineda", "LapizLazuli", "6:40 A.M", "3"));
        elements.add(new raiteElement("Oscar Axel ", "Santiago", "6:20 A.M", "2"));
        elements.add(new raiteElement("Andrei Trejo ", "Barrio 5", "6:30 A.M", "4"));
        elements.add(new raiteElement("Edson Manzano ", "Barrio 3", "6:50 A.M", "3"));
        elements.add(new raiteElement("Pancho ", "Colomos", "5:40 A.M", "1"));

        ListAdapter listAdapter = new ListAdapter(elements, getActivity(), new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(raiteElement item) {
                disableButtons();
                Intent i = new Intent(getActivity(), travelDetails2.class);
                startActivity(i);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);
    }

    private void disableButtons() {
        //Deshabilitar recycleview
        btn_changeLocation.setEnabled(false);
        recyclerView.setEnabled(false);

    }
    private void enableButtons() {
       //Habilitar recycleview
        btn_changeLocation.setEnabled(true);
        recyclerView.setEnabled(true);
    }
}