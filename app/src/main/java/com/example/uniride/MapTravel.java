package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapTravel extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gmap;
    //FirebaseFirestore mFirestore;
    //FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest mLocationRequest;

    ImageView back;
    HashMap<String, Object> datos = new HashMap<>();
    private Polyline currentPolyline;
    private List<LatLng> routePoints = new ArrayList<>();
    private static final float MIN_DISTANCE_TO_DESTINATION_METERS = 5; // constante 50 metros.

    private int getClosestPointIndex(LatLng currentLocation, List<LatLng> points) {
        int closestPointIndex = -1;
        double shortestDistance = Double.MAX_VALUE;

        for (int i = 0; i < points.size(); i++) {
            double distance = SphericalUtil.computeDistanceBetween(currentLocation, points.get(i));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestPointIndex = i;
            }
        }

        return closestPointIndex;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_travel);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapas);
        mapFragment.getMapAsync(this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new com.google.android.gms.location.LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);


        back = (ImageView) findViewById(R.id.backArrowImageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
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

        //double OriLat = 19.1254921;//todos son una prueba de la ubicacion
        //double OriLng = -104.3590741;
        double DesLat = 19.120063;
        double DesLng = -104.359311;
        //LatLng latLngOrigin = new LatLng(OriLat, OriLng);
        LatLng latLngDestination = new LatLng(DesLat, DesLng);
        BitmapDescriptor colege = BitmapDescriptorFactory.fromResource(R.drawable.colegio32);
        BitmapDescriptor driver = BitmapDescriptorFactory.fromResource(R.drawable.pointcar);
        MarkerOptions markerDestination = new MarkerOptions()
                .position(latLngDestination)
                .title("Escuela")
                .icon(colege);
        //MarkerOptions markerDriver = new MarkerOptions()
        //.position(latLngOrigin)
        //.title("Conductor")
        //.icon(driver);
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngDestination, 13));
        //gmap.addMarker(markerDriver);
        gmap.addMarker(markerDestination);
        //generateRoute(OriLat, OriLng, DesLat, DesLng);
        getCurrentLocation(DesLat, DesLng);

        // Luego, después de obtener la ubicación actual, genera la ruta inicial:
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    generateRoute(location.getLatitude(), location.getLongitude(), DesLat, DesLng);
                }
            }
        });

    }

    private void getCurrentLocation(double DesLat, double DesLng) {


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
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        gmap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));

                        int closestPointIndex = getClosestPointIndex(currentLatLng, routePoints);
                        if (closestPointIndex != -1 && closestPointIndex < routePoints.size() - 1) {
                            routePoints = routePoints.subList(closestPointIndex, routePoints.size());
                            updatePolyline(routePoints);
                        }
                        // Verifica si el usuario está cerca del destino
                        float[] results = new float[1];
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), DesLat, DesLng, results);
                        if (results[0] < MIN_DISTANCE_TO_DESTINATION_METERS) {
                            // Inicia la otra actividad
                            Intent intent = new Intent(MapTravel.this, MainActivityFragment.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        }, Looper.getMainLooper());
    }

    private void updatePolyline(List<LatLng> routePoints) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.purple_700));
        polylineOptions.width(15);
        polylineOptions.addAll(routePoints);
        currentPolyline = gmap.addPolyline(polylineOptions);
    }

    private void generateRoute(double currentLat, double currentLong, double DesLat, double DesLng) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://api.openrouteservice.org/v2/directions/driving-car")
                .buildUpon()
                .appendQueryParameter("api_key", "5b3ce3597851110001cf624870a1bdc82aea4b8aa2428a6d81c3cbea")
                .appendQueryParameter("start", currentLong+","+currentLat)
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
                    polylineOptions.width(15);
                    for(int i=0; i < coordinates.length(); i++){
                        JSONArray coordinate = coordinates.getJSONArray(i);
                        LatLng point = new LatLng(coordinate.getDouble(1), coordinate.getDouble(0));
                        routePoints.add(point);
                        polylineOptions.add(point);
                    }

                    if (currentPolyline != null) {
                        currentPolyline.remove(); // Elimina la línea anterior
                    }
                    currentPolyline = gmap.addPolyline(polylineOptions); // Dibuja la nueva línea y guarda la referencia

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapTravel.this, "Error del Servidor", Toast.LENGTH_SHORT).show();
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