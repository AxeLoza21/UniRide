package com.example.uniride;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CreateFragment extends Fragment {

    View vista;
    Button formcar, alertCar, selectStudent, timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       vista = inflater.inflate(R.layout.fragment_create, container, false);
       formcar = (Button)vista.findViewById(R.id.botonformcar);
       alertCar = (Button)vista.findViewById(R.id.botoncar);
       selectStudent = (Button)vista.findViewById(R.id.botonselectstudents);
       timer = (Button)vista.findViewById(R.id.time);

        formcar.setOnClickListener(new View.OnClickListener() {
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
        });
        selectStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SelectStudents.class);
                startActivity(i);
            }
        });
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SelectTime.class);
                startActivity(i);
            }
        });


       return vista;
    }
}