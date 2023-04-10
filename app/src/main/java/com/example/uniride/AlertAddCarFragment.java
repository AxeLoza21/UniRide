package com.example.uniride;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AlertAddCarFragment extends Fragment {

    View vista;
    Button formcar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_alert_add_car, container, false);
        formcar = (Button)vista.findViewById(R.id.btnComenzar);

        formcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FormCar.class);
                startActivity(i);
            }
        });
        return vista;
    }
}