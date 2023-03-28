package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button pasar;
    FirebaseAuth fAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();
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
                startActivity(new Intent(getApplicationContext(),MainActivityFragment.class));
                finish();
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



