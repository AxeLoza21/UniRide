package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uniride.components.SnackBarElement;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapClientSelectLocation extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gmap;
    ImageView back;
    Button btnContinue;
    LatLng pOriginLatLng;
    SnackBarElement snackBar;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String direccionPointRecolection, IdSolicitud;
    HashMap<String, Object> datos = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client_select_location);
        Bundle c = getIntent().getExtras();
        //firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        datos = (HashMap<String, Object>) c.getSerializable("datos");
        //snackbar
        snackBar = new SnackBarElement(this);
        back = (ImageView)findViewById(R.id.Exit);
        btnContinue = (Button)findViewById(R.id.btnContinue);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //al continuar manda  los datos del punto a firebase para crear la solicitud
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Map<String, Object> solicitud = new HashMap<>();
                solicitud.put("IdUser", fAuth.getUid());
                solicitud.put("IdPublication", datos.get("IdPublication"));
                solicitud.put("PoinLat", Double.parseDouble(pOriginLatLng.latitude+""));
                solicitud.put("PointLng", Double.parseDouble(pOriginLatLng.longitude+""));
                solicitud.put("State", "Pendiente");
                solicitud.put("direccionPoint", direccionPointRecolection);

                fStore.collection("solicitud").add(solicitud).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        IdSolicitud = task.getResult().getId();

                        fStore.collection("publications").document(datos.get("IdPublication").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Map<String, Object> docPublicationRequested = task.getResult().getData();
                                docPublicationRequested.put("IdSolicitud", IdSolicitud);
                                fStore.collection("users").document(fAuth.getUid()).collection("myRequestTo").document(datos.get("IdPublication").toString()).set(docPublicationRequested).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(getApplicationContext(), travel_sent.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                                finish();
                                            }
                                        },1200);

                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackBar.showSnackBar(getResources().getColor(R.color.red),"Error al mandar la solicitud");
                    }
                });
            }
        });



    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        double OriLat = Double.parseDouble(datos.get("OriLat").toString());
        double OriLng = Double.parseDouble(datos.get("OriLng").toString());
        double DesLat = Double.parseDouble(datos.get("DesLat").toString());
        double DesLng = Double.parseDouble(datos.get("DesLng").toString());

        LatLng latLngOrigin = new LatLng(OriLat, OriLng);
        LatLng latLngDestination = new LatLng(DesLat, DesLng);
        BitmapDescriptor colege = BitmapDescriptorFactory.fromResource(R.drawable.colegio32);
        BitmapDescriptor driver = BitmapDescriptorFactory.fromResource(R.drawable.pointcar);
        MarkerOptions markerDestination = new MarkerOptions()
                .position(latLngDestination)
                .title("Escuela")
                .icon(colege);
        MarkerOptions markerDriver = new MarkerOptions()
                .position(latLngOrigin)
                .title("Conductor")
                .icon(driver);

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14));
        gmap.addMarker(markerDriver);
        gmap.addMarker(markerDestination);
        generateRoute(OriLat, OriLng, DesLat, DesLng);
        //obtener direccion de puntp
        gmap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    Geocoder geocoder = new Geocoder(MapClientSelectLocation.this);
                    pOriginLatLng = gmap.getCameraPosition().target;
                    List<Address> addressList = geocoder.getFromLocation(pOriginLatLng.latitude, pOriginLatLng.longitude, 1);
                    direccionPointRecolection = addressList.get(0).getAddressLine(0);
                }catch (Exception e){
                    Log.d("Error:", e.getMessage()+"");
                }
            }
        });

    }

    private void generateRoute(double OriLat, double OriLng, double DesLat, double DesLng) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://api.openrouteservice.org/v2/directions/driving-car")
                .buildUpon()
                .appendQueryParameter("api_key", "5b3ce3597851110001cf6248fae2b5f6a9704b838bdb3940818fef72")
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
                    gmap.addPolyline(polylineOptions);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapClientSelectLocation.this, "Error del Servidor", Toast.LENGTH_SHORT).show();
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