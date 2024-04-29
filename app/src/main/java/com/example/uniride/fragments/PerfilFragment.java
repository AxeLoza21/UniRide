package com.example.uniride.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.example.uniride.AlertAddCar;
import com.example.uniride.MyHistoryTravels;
import com.example.uniride.MyPerfil;
import com.example.uniride.MyVehicles;
import com.example.uniride.R;
import com.example.uniride.functions.CalculateAge;
import com.example.uniride.login;
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
    RelativeLayout opcion2, opcion3, opcion4, logout;
    TextView nameUser, ageUser, schoolUser;
    ImageView imgUser;
    Dialog d_photo;

    CalculateAge edad;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    boolean hasCar;

    String URL_PHOTO;

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
        opcion4 = (RelativeLayout)vista.findViewById(R.id.cOpcion4);

        d_photo = new Dialog(getActivity());
        edad = new CalculateAge();

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                openDialogPhoto();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                mAuth.signOut();
                Intent i = new Intent(getActivity(), login.class);
                startActivity(i);
                getActivity().finish();
            }
        });



        opcion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                Intent i = new Intent(getActivity(), MyPerfil.class);
                startActivity(i);
            }
        });

        opcion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                if(getArguments() != null){
                    hasCar = getArguments().getBoolean("hasCar");
                    if(hasCar){
                        Intent i = new Intent(getActivity(), MyVehicles.class);
                        startActivity(i);
                        //getActivity().finish();
                    }else {
                        Intent i = new Intent(getActivity(), AlertAddCar.class);
                        startActivity(i);
                        //getActivity().finish();
                    }
                }
            }
        });
        opcion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtons();
                Intent i = new Intent(getActivity(), MyHistoryTravels.class);
                startActivity(i);

            }
        });


        setInformation();

        return vista;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableButtons();
    }

    private void setInformation() {
        if(mAuth.getCurrentUser() != null){
            fStore.collection("users").document(mAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null) {
                        nameUser.setText(value.getString("username"));
                        schoolUser.setText(value.getString("school"));
                        ageUser.setText(edad.calcularEdad(value.getString("birthDay")));

                        URL_PHOTO = value.getString("photo");

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


    private void disableButtons() {
        //opcion1.setEnabled(false);//Deshabilitar recycleview
        imgUser.setEnabled(false);
        opcion2.setEnabled(false);
        opcion3.setEnabled(false);
        opcion4.setEnabled(false);
        logout.setEnabled(false);
    }
    private void enableButtons() {
        //opcion1.setEnabled(true);//Habilitar recycleview
        imgUser.setEnabled(true);
        opcion2.setEnabled(true);
        opcion3.setEnabled(true);
        opcion4.setEnabled(true);
        logout.setEnabled(true);
    }

    private void openDialogPhoto(){
        d_photo.setContentView(R.layout.image_user_dialog);
        d_photo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_photo.setCanceledOnTouchOutside(true);
        d_photo.show();

        ImageView photoUser = d_photo.findViewById(R.id.imgUserComplete);
        if(URL_PHOTO.isEmpty()){
            Picasso.get().load(R.drawable.person_2).into(photoUser);
        }else{
            Picasso.get().load(URL_PHOTO).into(photoUser);
        }

        enableButtons();

    }
    public class PerfilFragment extends Fragment {

        private boolean modoOscuro = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_perfil, container, false);
            return view;
        }

        public void toggleModoOscuro(View view) {
            modoOscuro = !modoOscuro;
            int tema = modoOscuro ? R.style.Theme_UniRide_Dark : R.style.Theme_UniRide;
            requireActivity().setTheme(tema);
            requireActivity().recreate(); // Reinicia la actividad para aplicar el nuevo tema
        }


}