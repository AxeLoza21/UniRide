package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsClient extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gmap;
    ImageView back;
    ImageButton btnCenterVehicle;

    HashMap<String, Object> datos = new HashMap<>();

    ArrayList<LatLng> routePoints = new ArrayList<>();
    Marker carMarker;
    Handler handler = new Handler();
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_client);

        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");

        back = findViewById(R.id.backArrowImageView);
        btnCenterVehicle = findViewById(R.id.btnCenterVehicle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        back.setOnClickListener(v -> onBackPressed());

        btnCenterVehicle.setOnClickListener(v -> {
            if (carMarker != null) {
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(carMarker.getPosition(), 15));
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

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 12));
        gmap.addMarker(markerDestination);

        // Creamos el marcador del carro en el origen
        carMarker = gmap.addMarker(new MarkerOptions()
                .position(latLngOrigin)
                .title("Vehículo")
                .flat(true)
                .anchor(0.5f, 0.5f)
                .icon(driver));

        // Trazar la ruta y animar el carro
        generateRoute(OriLat, OriLng, DesLat, DesLng);
    }

    private void generateRoute(double OriLat, double OriLng, double DesLat, double DesLng) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://api.openrouteservice.org/v2/directions/driving-car")
                .buildUpon()
                .appendQueryParameter("api_key", "5b3ce3597851110001cf6248fae2b5f6a9704b838bdb3940818fef72")
                .appendQueryParameter("start", OriLng + "," + OriLat)
                .appendQueryParameter("end", DesLng + "," + DesLat)
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

                    for (int i = 0; i < coordinates.length(); i++) {
                        JSONArray coordinate = coordinates.getJSONArray(i);
                        double lon = coordinate.getDouble(0);
                        double lat = coordinate.getDouble(1);
                        LatLng point = new LatLng(lat, lon);
                        routePoints.add(point);
                        polylineOptions.add(point);
                    }

                    gmap.addPolyline(polylineOptions);
                    animateCar();

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

    private void animateCar() {
        index = 0;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (index < routePoints.size() - 1) {
                    LatLng start = routePoints.get(index);
                    LatLng end = routePoints.get(index + 1);

                    carMarker.setRotation(getBearing(start, end));
                    carMarker.setPosition(end);

                    index++;
                    handler.postDelayed(this, 500); // cada 0.5 seg
                }
            }
        });
    }

    private float getBearing(LatLng start, LatLng end) {
        double lat = Math.abs(start.latitude - end.latitude);
        double lng = Math.abs(start.longitude - end.longitude);

        if (start.latitude < end.latitude && start.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));

        else if (start.latitude >= end.latitude && start.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);

        else if (start.latitude >= end.latitude && start.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);

        else if (start.latitude < end.latitude && start.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);

        return -1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
