package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    TextView registrarse;
    Button login;
    TextInputEditText  ed_email, ed_password;
    FirebaseAuth fAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registrarse = (TextView)findViewById(R.id.btnRegistro);
        login = (Button)findViewById(R.id.btn_login);
        ed_email = (TextInputEditText)findViewById(R.id.et_usarname);
        ed_password = (TextInputEditText)findViewById(R.id.et_password);
        fAuth = FirebaseAuth.getInstance();


        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, sign_in.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed_email.getText().toString().trim();
                String password = ed_password.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    ed_email.setError("Correo requerido");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ed_password.setError("Contraseña requerida", null);
                    ed_email.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    ed_password.setError("la Contraseña no es correcta ingresa 8 digitos", null);
                    ed_password.requestFocus();
                    return;
                } else if (!TextUtils.isEmpty(email)) {
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                fAuth = FirebaseAuth.getInstance();
                                user = fAuth.getCurrentUser();


                                if (!user.isEmailVerified()) {
                                    Toast.makeText(login.this, "Debes de verificar el correo", Toast.LENGTH_SHORT).show();
                                    fAuth.signOut();

                                } else if (user.isEmailVerified()) {
                                    Intent i = new Intent(login.this, selectLocation.class);
                                    startActivity(i);
                                }
                            }

                        }
                    });
                }

            }
        });



    }
}