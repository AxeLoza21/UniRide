package com.example.uniride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PerfilFragment extends Fragment {
    View vista;
    RelativeLayout opcion1, opcion2, opcion3, opcion4, logout;
    TextView nameUser, ageUser, schoolUser;
    ImageView imgUser;

     FirebaseAuth mAuth;
     FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        opcion2 = (RelativeLayout)vista.findViewById(R.id.cOpcion2);
        opcion3 = (RelativeLayout)vista.findViewById(R.id.cOpcion3);
        logout = (RelativeLayout)vista.findViewById(R.id.logout);
        nameUser = (TextView)vista.findViewById(R.id.nameUser);
        ageUser = (TextView)vista.findViewById(R.id.ageUser);
        schoolUser = (TextView)vista.findViewById(R.id.schoolUser);
        imgUser = (ImageView)vista.findViewById(R.id.profileIMG);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(getActivity(), login.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        opcion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyPerfil.class);
                startActivity(i);
            }
        });

        opcion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyVehicles.class);

                startActivity(i);
            }
        });

        setInformation();

        return vista;
    }



    private void setInformation() {
        if(mAuth.getCurrentUser() != null){
            fStore.collection("users").document(mAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null) {
                        nameUser.setText(value.getString("username"));
                        schoolUser.setText(value.getString("school"));
                        ageUser.setText(calcularEdad(value.getString("birthDay")));
                        if(value.getString("photo").isEmpty()){
                            Picasso.get().load(R.drawable.person_2).into(imgUser);
                        }else{
                            Picasso.get().load(value.getString("photo")).into(imgUser);
                        }
                    } else {
                        // El documento no existe
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "Sesion Cerrada", Toast.LENGTH_SHORT).show();
        }

    }

    private String calcularEdad(String fecha) {
        String edad = "";
        String[] parts = fecha.split("/");
        int dia = Integer.parseInt(parts[0]); // dia
        int mes = Integer.parseInt(parts[1]); // mes
        int ano = Integer.parseInt(parts[2]); // año

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            LocalDate birthdate = LocalDate.of(ano, mes, dia);
            Period p = Period.between(birthdate, today);
            edad = p.getYears() + " años";

        }
        return edad;
    }
}