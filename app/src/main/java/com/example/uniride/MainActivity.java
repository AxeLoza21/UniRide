package com.example.uniride;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button pasar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        pasar = (Button)findViewById(R.id.nose);

        pasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, login.class);
                startActivity(i);
            }
        });

        if(fAuth.getCurrentUser() != null){
            user = fAuth.getCurrentUser();
            if(!user.isEmailVerified()){
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
                DocumentReference documentReference = fStore.collection("users").document(user.getUid());
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String school = value.getString("school");
                        String birthDay = value.getString("birthDay");

                        if(school.isEmpty() || birthDay.isEmpty()){
                            startActivity(new Intent(getApplicationContext(),Additional_Information.class));
                            finish();
                        }else{
                            startActivity(new Intent(getApplicationContext(),MainActivityFragment.class));
                            finish();
                        }
                    }
                });
            }
        }else{
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
        }

        /*
        ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j = new Intent(Principal.this, login.class);
                startActivity(j);
            }
        });*/

    }


}



