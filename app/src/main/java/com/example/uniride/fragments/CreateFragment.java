package com.example.uniride.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uniride.MapsActivity;
import com.example.uniride.R;
import com.example.uniride.components.SnackBarElement;
import com.example.uniride.selectLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class CreateFragment extends Fragment {

    View vista;
    RelativeLayout locationActual, locationMapa;
    EditText et_InitLocation;
    HashMap<String, Object> datos = new HashMap<>();


    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private final static int SETTINGS_REQUEST_CODE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_create, container, false);
        locationMapa = (RelativeLayout) vista.findViewById(R.id.cLocationMapa);
        locationActual = (RelativeLayout) vista.findViewById(R.id.cLocationActual);
        et_InitLocation = (EditText) vista.findViewById(R.id.et_location);

        fusedClient = LocationServices.getFusedLocationProviderClient(getContext());


        /*formcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FormCar.class);
                startActivity(i);
            }
        });
        alertCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AlertAddCar.class);
                startActivity(i);
            }
        });*/
        locationActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsActived()) {
                    if (ActivityCompat.checkSelfPermission(
                            getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(
                            getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    Task<Location> task = fusedClient.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                currentLocation = location;
                                Bundle createTravel = new Bundle();
                                datos.put("OriLat", currentLocation.getLatitude());
                                datos.put("OriLng", currentLocation.getLongitude());
                                datos.put("direccionPartida", getDireccion());
                                createTravel.putSerializable("datos", datos);
                                Intent i = new Intent(getActivity(), selectLocation.class);
                                i.putExtra("create", true);
                                i.putExtras(createTravel);
                                startActivity(i);
                            }else{
                                new SnackBarElement(getActivity()).showSnackBar(getResources().getColor(R.color.red), "Error al encontrar tu Ubicacion. Intenta Reiniciar la APP");
                            }
                        }
                    });


                }else{
                    showAlertDialogGPS();
                }

            }

        });
        locationMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsActivity.class);
                startActivity(i);
            }
        });


       return vista;
    }

    private String getDireccion() {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String direccion = addressList.get(0).getAddressLine(0);
        return direccion;
    }

    private boolean gpsActived(){
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) vista.getContext().getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            isActive = true;
        }

        return isActive;
    }

    private void showAlertDialogGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Por favor. Activa tu ubicacion para continuar")
                .setPositiveButton("Configuracion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_REQUEST_CODE && gpsActived()){
            //El GPS ya esta activado
        }else{
            //El GPS aun no ha sido activado
            showAlertDialogGPS();


        }
    }
}