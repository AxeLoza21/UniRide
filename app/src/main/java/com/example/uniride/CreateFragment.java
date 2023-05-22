package com.example.uniride;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


public class CreateFragment extends Fragment {

    View vista;
    RelativeLayout locationActual, locationMapa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       vista = inflater.inflate(R.layout.fragment_create, container, false);
       locationMapa = (RelativeLayout)vista.findViewById(R.id.cLocationMapa);
       locationActual = (RelativeLayout)vista.findViewById(R.id.cLocationActual);

        /*formcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FormCar.class);
                startActivity(i);
            }
        });
        alertCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AlertAddCar.class);
                startActivity(i);
            }
        });*/
        locationActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle createTravel = new Bundle();
                createTravel.putBoolean("create", true);
                Intent i = new Intent(getActivity(), selectLocation.class);
                i.putExtras(createTravel);
                startActivity(i);
            }

        });
        locationMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle createTravel = new Bundle();
                createTravel.putBoolean("create", true);
                Intent i = new Intent(getActivity(), selectLocation.class);
                i.putExtras(createTravel);
                startActivity(i);
            }
        });


       return vista;
    }
}