package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    View pasar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    private static final int LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        pasar = (View)findViewById(R.id.nose);

        //Abrir la ventana para solicitar los permisos de Ubicacion
        ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);


    }

    private void checkLocationPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Entro aqui", Toast.LENGTH_SHORT).show();
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "Pero aqui no", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permiso de Ubicación");
                builder.setMessage("Esta aplicación necesita acceso a la ubicación para brindarte una mejor experiencia. ¿Conceder permiso ahora?");

                builder.setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Solicitar permisos nuevamente
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    //Los permisos de ubicacion se consedieron
                    if(fAuth.getCurrentUser() != null){
                        //Entra aqui, si, hay una cuenta activa
                        user = fAuth.getCurrentUser();
                        if(!user.isEmailVerified()){
                            //Entra aqui si la cuenta activa actual no esta verificada
                            Toast.makeText(this, "Necesitar verificar tu correo", Toast.LENGTH_SHORT).show();

                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(MainActivity.this, login.class);
                                    startActivity(i);
                                    finish();
                                }
                            };
                            Timer time = new Timer();
                            time.schedule(task, 2500);


                        }else if (user.isEmailVerified()){
                            //Entra aqui si la cuenta activa actual ya esta verificada
                            DocumentReference documentReference = fStore.collection("users").document(user.getUid());
                            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    String school = value.getString("school");
                                    String birthDay = value.getString("birthDay");
                                    String location = value.getString("destinationLocation");
                                    String cargo = value.getString("Rol");

                                    if(school.equals("") || birthDay.equals("") || cargo.equals("")){
                                        startActivity(new Intent(getApplicationContext(),Additional_Information.class));
                                        finish();
                                    }else if (location.isEmpty()){
                                        startActivity(new Intent(getApplicationContext(),selectLocation.class));
                                        finish();
                                    }
                                    else{
                                        startActivity(new Intent(getApplicationContext(),MainActivityFragment.class));
                                        finish();
                                    }
                                }
                            });
                        }
                    }else{
                        //Entra aqui si no hay ninguna cuenta activa
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Intent i = new Intent(MainActivity.this, login.class);//modificar esto para que te mande al iniciar, predeterminado: login
                                startActivity(i);
                                finish();
                            }
                        };
                        Timer time = new Timer();
                        time.schedule(task, 2500);
                    }
                }
            }else{
                //No se han consedido los permisos de Ubicacion
                Toast.makeText(this, "No consedidos", Toast.LENGTH_SHORT).show();
                checkLocationPermissions();
            }
        }
    }

}



