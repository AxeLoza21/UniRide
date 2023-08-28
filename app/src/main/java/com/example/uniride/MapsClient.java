package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsClient extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gmap;
    //FirebaseFirestore mFirestore;
    //FirebaseAuth mAuth;
    ImageView back;
    int newWidth = 40;  // Ancho deseado en píxeles
    int newHeight = 40; // Alto deseado en píxeles

    HashMap<String, Object> datos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_client);
        //mAuth = FirebaseAuth.getInstance();
        //mFirestore = FirebaseFirestore.getInstance();
        //----Recibir los datos anteriores----
        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");
        back = (ImageView)findViewById(R.id.backArrowImageView);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //direction();


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

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 12));
        gmap.addMarker(markerDriver);
        gmap.addMarker(markerDestination);
        generateRoute(OriLat, OriLng, DesLat, DesLng);

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
                Toast.makeText(MapsClient.this, "Error del Servidor", Toast.LENGTH_SHORT).show();
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



    /*public void direction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://api.openrouteservice.org/v2/directions/driving-car")
                .buildUpon()
                .appendQueryParameter("api_key", "5b3ce3597851110001cf6248fae2b5f6a9704b838bdb3940818fef72")
                .appendQueryParameter("start", "-104.3590974,19.1256515")
                .appendQueryParameter("end", "-104.4093514,19.1256791")
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
                    polylineOptions.color(getResources().getColor(R.color.purple_500));
                    polylineOptions.width(7);
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
                Toast.makeText(MapsClient.this, "Error del Servidor", Toast.LENGTH_SHORT).show();
                Log.e("Error Servidor", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }*/



    /*@Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setTrafficEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gmap.setMyLocationEnabled(true);
        getLastLocation();
        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> direccion = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    String mostradireccion = direccion.get(0).getAddressLine(0);
                    markerOptions.title(mostradireccion);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gmap.clear();
                gmap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            }
        });
        LocationManager locationManager = (LocationManager) MapsClient.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    }*/

    /*private void getLastLocation() {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(MapsClient.this);
        try {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                if (gmap != null) {

                                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
                                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }*/
}