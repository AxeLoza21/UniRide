package com.example.uniride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class sign_in extends AppCompatActivity {

    ImageView btnBack;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button btn_register;
    TextInputEditText ed_user , ed_email, ed_password , ed_telef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnBack = (ImageView)findViewById(R.id.btnBackLoogin);
        btn_register = (Button)findViewById(R.id.btn_interested);
        ed_email = (TextInputEditText)findViewById(R.id.et_email);
        ed_password = (TextInputEditText)findViewById(R.id.et_password);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterProgress();
            }
        });
    }

    private void RegisterProgress() {
        final String emailREQUEST = ed_email.getText().toString().trim();
        final String email = ed_email.getText().toString().trim() + "@ucol.mx";
        String password = ed_password.getText().toString().trim();
        if (TextUtils.isEmpty(emailREQUEST)) {
            ed_email.setError("Correo obligatorio");
            ed_email.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            ed_password.setError("Contraseña obligatoria", null);
            ed_password.requestFocus();
            return;
        } else if (password.length() < 8) {
            ed_password.setError("La contraseña debe ser mayor a 8 caracteres", null);
            ed_password.requestFocus();
            return;
        }
        else if (password.length() < 8) {
            ed_password.setError("La contraseña debe ser mayor a 8 caracteres", null);
            ed_password.requestFocus();
            return;
        } else if (!TextUtils.isEmpty(emailREQUEST)){
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser fuser = fAuth.getCurrentUser();
                        fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(sign_in.this, "Se ha enviado una Verificacion a tu correo, aceptalo para poder ingresar", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(), login.class));
                        finish();
                    }else {
                        Toast.makeText(sign_in.this, "Error ! El Correo ya esta registrado", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}