package com.example.uniride;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uniride.adapter.PublicationAdapter;
import com.example.uniride.model.Publications;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    View vista;
    List<Publications> elements;
    CardView btn_changeLocation;
    LinearLayout cTexto;
    TextView location;
    RecyclerView recyclerView;
    PublicationAdapter publicationAdapter;
    SharedPreferences datosUsuario;

    FirebaseFirestore fStore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_home, container, false);
        btn_changeLocation = (CardView)vista.findViewById(R.id.btn_changeLocation);
        cTexto = vista.findViewById(R.id.cTexto);
        recyclerView = vista.findViewById(R.id.raitesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        location = (TextView)vista.findViewById(R.id.location);

        fStore = FirebaseFirestore.getInstance();

        datosUsuario = vista.getContext().getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        location.setText(datosUsuario.getString("campus", "???Campus???"));

        btn_changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                Intent i = new Intent(getActivity(), selectLocation.class);
                startActivity(i);
                getActivity().finish();

            }
        });

        //--------Obtener la cantidad de elementos que va haber en el RecyclerView-------
        fStore.collection("publications").whereEqualTo("campusDestination", datosUsuario.getString("campus", "???Campus???")).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    if (value.size() > 0){
                        cTexto.setVisibility(View.INVISIBLE);
                    }else{
                        cTexto.setVisibility(View.VISIBLE);
                    }
                }else {
                    //el documnegrshd
                }

            }
        });
        //---------------------------------------------------------------------------------

        init();
        return vista;
    }

    private void init() {
        Query query = fStore.collection("publications").whereEqualTo("campusDestination", datosUsuario.getString("campus", "???Campus???"));

        FirestoreRecyclerOptions<Publications> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Publications>().setQuery(query, Publications.class).build();

        publicationAdapter = new PublicationAdapter(firestoreRecyclerOptions, getActivity());
        publicationAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(publicationAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableButtons();
        publicationAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        publicationAdapter.startListening();
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




    //RecyclerView Anterior
    /*public void init() {
        elements = new ArrayList<>();
        elements.add(new Publications("Ernesto Manuel Jimenez Pineda", "LapizLazuli", "6:40 A.M", "3"));
        elements.add(new Publications("Oscar Axel ", "Santiago", "6:20 A.M", "2"));
        elements.add(new Publications("Andrei Trejo ", "Barrio 5", "6:30 A.M", "4"));
        elements.add(new Publications("Edson Manzano ", "Barrio 3", "6:50 A.M", "3"));
        elements.add(new Publications("Pancho ", "Colomos", "5:40 A.M", "1"));

        PublicationAdapter publicationAdapter = new PublicationAdapter(elements, getActivity(), new PublicationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Publications item) {
                disableButtons();
                Intent i = new Intent(getActivity(), travelDetails2.class);
                startActivity(i);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(publicationAdapter);
    }*/
}