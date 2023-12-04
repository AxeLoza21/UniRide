package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.uniride.publicationCreation.ConfirmRouteMap;
import com.example.uniride.publicationCreation.CreatePublicationDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Button btnContinue;
    String direccion;
    HashMap<String, Object> datos = new HashMap<>();

    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int  REQUEST_CODE = 101;

    GoogleMap mMap;
    LatLng pOriginLatLng;

    private PlacesClient mPlaces;
    private AutocompleteSupportFragment mAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(MapsActivity.this);

        btnContinue = (Button)findViewById(R.id.btnContinue);


        //mAutoComplete = (AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.placeAutoComplete);

        //if(!Places.isInitialized()){
        //    Places.initialize(getApplicationContext(), "AIzaSyBcK9PzTSxKBLYj-33v6LfGjBb83nFyXeE");
        //}
        //mPlaces = Places.createClient(this);
        //mAutoComplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        /*mAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status){}
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.e("Name", place.getName());
                Log.e("LAtLong", place.getLatLng().toString());
            }
        });*/
        fusedClient = LocationServices.getFusedLocationProviderClient(this);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle createTravel = new Bundle();
                datos.put("OriLat", Double.parseDouble(pOriginLatLng.latitude+""));
                datos.put("OriLng", Double.parseDouble(pOriginLatLng.longitude+""));
                datos.put("direccionPartida", direccion);
                createTravel.putSerializable("datos", datos);

                if(getIntent().getBooleanExtra("editar", false)){
                    if(getIntent().getStringExtra("activity").equals("routeMap")){
                        Intent i = new Intent(MapsActivity.this, ConfirmRouteMap.class);
                        i.putExtras(createTravel);
                        startActivity(i);
                        finish();
                    }else if(getIntent().getStringExtra("activity").equals("createTravelDetails")){
                        Intent i = new Intent(MapsActivity.this, CreatePublicationDetails.class);
                        i.putExtras(createTravel);
                        startActivity(i);
                        finish();
                    }

                }else{
                    Intent i = new Intent(MapsActivity.this, selectLocation.class);
                    i.putExtra("create", true);
                    i.putExtras(createTravel);
                    startActivity(i);
                }

            }
        });


    }

    /*private void getLocation(){
        //Comprobar si tienes Activados los permisos de Ubicacion. Si no los tienes se ejecuta lo de adentro del IF.
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //Ventana para pedir los permisos de ubicacion
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }*/

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if(getIntent().getBooleanExtra("editar", false)){
            //Recibir la informacion de las vistas anteriores
            Bundle c = getIntent().getExtras();
            datos = (HashMap<String, Object>) c.getSerializable("datos");

            double oriLat = Double.parseDouble(datos.get("OriLat").toString());
            double oriLng = Double.parseDouble(datos.get("OriLng").toString());

            LatLng latLngAnterior = new LatLng(oriLat, oriLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngAnterior, 18));

        }else{
            LatLng latLngColima = new LatLng(19.1510171, -103.9152456);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngColima, 8));

        }

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    pOriginLatLng = mMap.getCameraPosition().target;
                    List<Address> addressList = geocoder.getFromLocation(pOriginLatLng.latitude, pOriginLatLng.longitude, 1);
                    direccion = addressList.get(0).getAddressLine(0);
                }catch (Exception e){
                    Log.d("Error:", e.getMessage()+"");
                }
            }
        });



        //LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        //Log.e("Latitude", String.valueOf(currentLocation.getLatitude()));
        //Log.e("Longitud", String.valueOf(currentLocation.getLongitude()));
        //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Mi ubicacion actual");
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngColima));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngColima, 10));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngColima));
        //googleMap.addMarker(markerOptions);

    }

}