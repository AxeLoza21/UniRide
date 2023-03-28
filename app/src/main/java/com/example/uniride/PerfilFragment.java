package com.example.uniride;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PerfilFragment extends Fragment {
    View vista;
    Button salir;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        salir = (Button) vista.findViewById(R.id.btnsalir);
        mAuth = FirebaseAuth.getInstance();
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(getActivity(), login.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return vista;

    }
}