package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uniride.components.SnackBarElement;
import com.example.uniride.functions.CalculateAge;
import com.example.uniride.publicationCreation.ConfirmRouteMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapClientLocation extends AppCompatActivity implements OnMapReadyCallback{

    ImageView imgUser, btnBack;
    TextView nameUser, ageUser, schoolUser;
    CardView btnAccept, btnDecline;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    GoogleMap mMap;
    String idRequest;

    double UserLat, UserLng, OriLat, OriLng, DesLat, DesLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client_location);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        imgUser = (ImageView)findViewById(R.id.imgUser);
        nameUser = (TextView)findViewById(R.id.nameUser);
        ageUser = (TextView)findViewById(R.id.ageUser);
        schoolUser = (TextView)findViewById(R.id.schoolUser);
        btnBack = (ImageView)findViewById(R.id.btn_back);
        btnAccept = (CardView) findViewById(R.id.btnAccept);
        btnDecline = (CardView) findViewById(R.id.btnDecline);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync((OnMapReadyCallback) MapClientLocation.this);

        idRequest = getIntent().getStringExtra("idItem");
        //Log.e("Codigo: ", idSolicitud);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                fStore.collection("solicitud").document(idRequest).update("State", "Aceptado").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        new SnackBarElement(MapClientLocation.this).showSnackBar(getResources().getColor(R.color.green),"Pasajero Aceptado");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), MyTravelsCreated.class));
                                finish();
                                // Desbloquear la pantalla
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        },2000);
                    }
                });
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                fStore.collection("solicitud").document(idRequest).update("State", "Rechazado").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        new SnackBarElement(MapClientLocation.this).showSnackBar(getResources().getColor(R.color.red),"Pasajero Rechazado");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), MyTravelsCreated.class));
                                finish();
                                // Desbloquear la pantalla
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        },2000);
                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        fStore.collection("solicitud").document(idRequest).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserLat = task.getResult().getDouble("PoinLat");
                UserLng = task.getResult().getDouble("PointLng");
                fStore.collection("publications").document(task.getResult().getString("IdPublication")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        OriLat = task.getResult().getDouble("OriLat");
                        OriLng = task.getResult().getDouble("OriLng");
                        DesLat = task.getResult().getDouble("DesLat");
                        DesLng = task.getResult().getDouble("DesLng");

                        LatLng latLngOrigin = new LatLng(OriLat, OriLng);
                        LatLng latLngDestination = new LatLng(DesLat, DesLng);
                        LatLng latLngPasajero = new LatLng(UserLat, UserLng);
                        BitmapDescriptor colege = BitmapDescriptorFactory.fromResource(R.drawable.colegio32);
                        BitmapDescriptor driver = BitmapDescriptorFactory.fromResource(R.drawable.pointcar);
                        BitmapDescriptor pasajero = BitmapDescriptorFactory.fromResource(R.drawable.marcador_de_posicion_pasajero);
                        MarkerOptions markerDestination = new MarkerOptions()
                                .position(latLngDestination)
                                .title("Escuela")
                                .icon(colege);
                        MarkerOptions markerOrigin = new MarkerOptions()
                                .position(latLngOrigin)
                                .title("Tu Ubicacion")
                                .icon(driver);
                        MarkerOptions markerPasajero = new MarkerOptions()
                                .position(latLngPasajero)
                                .title("Ubicacion Pasajero")
                                .icon(pasajero);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 12));
                        mMap.addMarker(markerOrigin);
                        mMap.addMarker(markerDestination);
                        mMap.addMarker(markerPasajero);

                        generateRoute(OriLat, OriLng, DesLat, DesLng);
                    }
                });

                fStore.collection("users").document(task.getResult().getString("IdUser")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        nameUser.setText(task.getResult().getString("username"));
                        ageUser.setText(new CalculateAge().calcularEdad(task.getResult().getString("birthDay")));
                        schoolUser.setText(task.getResult().getString("school"));
                        if(!task.getResult().getString("photo").equals("")){
                            Picasso.get().load(task.getResult().getString("photo")).into(imgUser);
                        }
                    }
                });
            }
        });

    }

    public void generateRoute(double OriLat, double OriLng, double DesLat, double DesLng) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://api.openrouteservice.org/v2/directions/driving-car")
                .buildUpon()
                .appendQueryParameter("api_key", "AIzaSyBGEINzWzOYMKjHG0Sp0oaRzVf0WInRAas")
                .appendQueryParameter("start", OriLng+","+OriLat)
                .appendQueryParameter("end", DesLng+","+DesLat)
                .toString();
        Log.e("URL", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray features = response.getJSONArray("features");
                    JSONObject featuresContent = features.getJSONObject(0);
                    JSONObject geometry = featuresContent.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");

                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.color(getResources().getColor(R.color.purple_700));
                    polylineOptions.width(10);
                    for(int i=0; i < coordinates.length(); i++){
                        JSONArray coordinate = coordinates.getJSONArray(i);
                        polylineOptions.add(new LatLng(coordinate.getDouble(1), coordinate.getDouble(0)));
                    }
                    mMap.addPolyline(polylineOptions);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapClientLocation.this, "Error del Servidor", Toast.LENGTH_SHORT).show();
                Log.e("Error Servidor", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}