package com.example.uniride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.uniride.adapter.SchoolsLocationAdapter;
import com.example.uniride.model.SchoolsLocation;
import com.example.uniride.publicationCreation.ConfirmRouteMap;
import com.example.uniride.publicationCreation.CreatePublicationDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class selectLocation extends AppCompatActivity {

    HashMap<String, Object> datos = new HashMap<>();
    List<SchoolsLocation> elements;
    SchoolsLocationAdapter listAdapter;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        init();
        setupSearchView();

    }
    //esta actividad permite al usuario seleccionar una ubicación de una lista predefinida de escuelas y guardar esa selección para su uso posterior en la aplicación.
//Declaraciones e inicializaciones: Se importan las clases necesarias y se declaran e inicializan las variables y objetos necesarios, como
// FirebaseAuth y FirebaseFirestore para la autenticación y acceso a la base de datos, respectivamente. También se inicializan la lista de
// ubicaciones de escuelas y el adaptador para la vista de reciclaje.

//Método onCreate(): Este método se llama cuando se crea la actividad. Aquí se configura el diseño de la actividad y se inicializan los elementos
// y métodos necesarios.

//Método init(): Este método inicializa la lista de ubicaciones de las escuelas y configura el adaptador para la vista de reciclaje. También maneja
// la lógica cuando se hace clic en un elemento de la lista.

//Método setupSearchView(): Este método configura el EditText de búsqueda para filtrar la lista de ubicaciones de las escuelas a medida que el usuario
// escribe en él.

//Método guardarDestino(): Este método guarda la selección de ubicación del usuario en SharedPreferences para que pueda ser recuperada más tarde en
// otras partes de la aplicación.

//Método onStart(): Este método se llama cuando la actividad se vuelve visible para el usuario. Aquí se puede realizar cualquier inicialización adicional
// o actualización de la interfaz de usuario.
    public void init() {
        elements = new ArrayList<>();
        elements.add(new SchoolsLocation("Manzanillo, Colima", "Campus el Naranjo", "FIE FCAM FACIMAR facultad ingenieria software mecanica electromecanica electrica mecatronica contabilidad administracion oceanico ingeniero licenciatura ingeniero software",19.1226787, -104.4032666));
        elements.add(new SchoolsLocation("Manzanillo, Colima", "Campus Barrio 3", "CUBAM gastronomia comercio exterior aduanas licenciatura ", 19.1060035, -104.311148));
        elements.add(new SchoolsLocation("Manzanillo, Colima", "Campus San Pedrito", "Bach 8 Bach 9 Bach 10 bachillerato 8 bachillerato 9 bachillerato 10", 19.0546213, -104.3074771));

        listAdapter = new SchoolsLocationAdapter(elements, this, new SchoolsLocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SchoolsLocation item) {
                boolean crear = getIntent().getBooleanExtra("create", false);
                if(!crear){
                    guardarDestino(item.getnCampus());
                    String campus = item.getnCampus();
                    DocumentReference documentReference = fStore.collection("users").document(fAuth.getUid());
                    Map<String, Object> user = new HashMap<>();
                    user.put("destinationLocation", campus);
                    documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent i = new Intent(selectLocation.this, MainActivityFragment.class);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });

                }else{

                    //----Recibir los datos anteriores----
                    Bundle c = getIntent().getExtras();
                    datos = (HashMap<String, Object>) c.getSerializable("datos");

                    if(getIntent().getBooleanExtra("editar", false)){
                        Bundle cDatos = new Bundle();
                        datos.put("campusDestination", item.getnCampus());
                        datos.put("DesLat", item.getLat());
                        datos.put("DesLng", item.getLng());
                        cDatos.putSerializable("datos",datos);

                        if(getIntent().getStringExtra("activity").equals("routeMap")){
                            Intent d = new Intent(selectLocation.this, ConfirmRouteMap.class);
                            d.putExtras(cDatos);
                            startActivity(d);
                            finish();
                        }else if(getIntent().getStringExtra("activity").equals("createTravelDetails")){
                            Intent d = new Intent(selectLocation.this, CreatePublicationDetails.class);
                            d.putExtras(cDatos);
                            startActivity(d);
                            finish();
                        }

                    }else{
                        Bundle cDatos = new Bundle();
                        datos.put("campusDestination", item.getnCampus());
                        datos.put("DesLat", item.getLat());
                        datos.put("DesLng", item.getLng());
                        cDatos.putSerializable("datos",datos);

                        Intent d = new Intent(selectLocation.this, ConfirmRouteMap.class);
                        d.putExtras(cDatos);
                        startActivity(d);
                        finish();
                    }


                }

            }
        });
        RecyclerView recyclerView = findViewById(R.id.locationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }


    private void setupSearchView() {
        EditText searchView = findViewById(R.id.searchView);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable newText) {
                listAdapter.getFilter().filter(newText);
            }
        });
    }


    private void guardarDestino(String campus) {
        SharedPreferences datosUsuario = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=datosUsuario.edit();
        editor.putString("campus",campus);
        editor.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}