package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uniride.components.DialogElement;
import com.example.uniride.functions.SplitDirection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Route;

public class RouteMap extends AppCompatActivity implements OnMapReadyCallback {

    TextView tv_Origen, tv_Destino;
    Button btnConfirmar;
    RelativeLayout btnEditarPPartida, btnEditarPDestino;

    HashMap<String, Object> datos = new HashMap<>();

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        tv_Origen = (TextView)findViewById(R.id.OriDireccion);
        tv_Destino = (TextView)findViewById(R.id.OriDestino);
        btnConfirmar = (Button)findViewById(R.id.btnConfirmar);
        btnEditarPPartida = (RelativeLayout) findViewById(R.id.cPulsaEditar);
        btnEditarPDestino = (RelativeLayout)findViewById(R.id.cPulsaEditar2);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(RouteMap.this);


        //----Recibir los datos anteriores----
        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");
        String direccion = datos.get("direccionPartida").toString();


        tv_Origen.setText(new SplitDirection().getDirection(direccion));
        tv_Destino.setText(datos.get("campusDestination").toString());


        btnEditarPPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent i = new Intent(RouteMap.this, MapsActivity.class);
                i.putExtra("editar", true);
                i.putExtra("activity","routeMap");
                i.putExtras(cDatos);
                startActivity(i);
            }
        });

        btnEditarPDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent i = new Intent(RouteMap.this, selectLocation.class);
                i.putExtra("editar", true);
                i.putExtra("create", true);
                i.putExtra("activity","routeMap");
                i.putExtras(cDatos);
                startActivity(i);
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                cDatos.putSerializable("datos", datos);

                Intent d = new Intent(RouteMap.this, SelectDate.class);
                d.putExtras(cDatos);
                startActivity(d);
            }
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        double OriLat = Double.parseDouble(datos.get("OriLat").toString());
        double OriLng = Double.parseDouble(datos.get("OriLng").toString());
        double DesLat = Double.parseDouble(datos.get("DesLat").toString());
        double DesLng = Double.parseDouble(datos.get("DesLng").toString());

        LatLng latLngOrigin = new LatLng(OriLat, OriLng);
        LatLng latLngDestination = new LatLng(DesLat, DesLng);
        MarkerOptions markerOrigin = new MarkerOptions().position(latLngOrigin).title("Punto de Inicio");
        MarkerOptions markerDestination = new MarkerOptions().position(latLngDestination).title("Punto de Destino");

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 12));
        mMap.addMarker(markerOrigin);
        mMap.addMarker(markerDestination);

        generateRoute(OriLat, OriLng, DesLat, DesLng);
    }

    public void generateRoute(double OriLat, double OriLng, double DesLat, double DesLng) {
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
                    mMap.addPolyline(polylineOptions);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RouteMap.this, "Error del Servidor", Toast.LENGTH_SHORT).show();
                Log.e("Error Servidor", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        new DialogElement(this).showDialogWarningExit();
    }
}