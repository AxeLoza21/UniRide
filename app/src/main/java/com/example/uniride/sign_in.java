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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class sign_in extends AppCompatActivity {

    ImageView btnBack;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button btn_register;
    TextInputEditText ed_user , ed_email, ed_password , ed_repitpassword,  ed_telef;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnBack = (ImageView)findViewById(R.id.btnBackLoogin);
        btn_register = (Button)findViewById(R.id.btn_interested);
        ed_email = (TextInputEditText)findViewById(R.id.et_email);
        ed_password = (TextInputEditText)findViewById(R.id.et_password);
        ed_user = (TextInputEditText)findViewById(R.id.et_usarname);
        ed_repitpassword = (TextInputEditText)findViewById(R.id.et_repPassword);
        ed_telef = (TextInputEditText)findViewById(R.id.et_phone);


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
        final String username = ed_user.getText().toString().trim();
        String password = ed_password.getText().toString().trim();
        String repitpassword = ed_repitpassword.getText().toString().trim();
        String phone = ed_telef.getText().toString().trim();

        // Validar el campo de usuario
        if (TextUtils.isEmpty(username)) {
            ed_user.setError("Nombre de usuario requerido");
            ed_user.requestFocus();
            return;
        } else if (username.contains("@")) {
            ed_user.setError("El nombre de usuario no puede contener el símbolo '@'");
            ed_user.requestFocus();
            return;
        } else if (!username.matches("[A-Za-z0-9]+")) {
            ed_user.setError("El nombre de usuario solo puede contener letras y números, sin espacios");
            ed_user.requestFocus();
            return;
        }
        // Validar el campo de correo electrónico
        if (TextUtils.isEmpty(emailREQUEST)) {
            ed_email.setError("Correo electrónico requerido");
            ed_email.requestFocus();
            return;
        } else if (!emailREQUEST.matches("[A-Za-z0-9]+")) {
            ed_email.setError("El correo electrónico solo puede contener letras y números");
            ed_email.requestFocus();
            return;
        }

        // Validar el campo de contraseña
        if (TextUtils.isEmpty(password)) {
            ed_password.setError("Contraseña requerida", null);
            ed_password.requestFocus();
            return;
        } else if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$")) {
            ed_password.setError("La contraseña debe tener al menos una letra mayúscula, dos números, un carácter especial y ser mayor a 8 caracteres", null);
            ed_password.requestFocus();
            return;
        }

        // Validar el campo de repetir contraseña
        if (TextUtils.isEmpty(repitpassword)) {
            ed_repitpassword.setError("Repetir contraseña requerido", null);
            ed_repitpassword.requestFocus();
            return;
        } else if (!repitpassword.equals(password)) {
            ed_repitpassword.setError("Las contraseñas no coinciden", null);
            ed_repitpassword.requestFocus();
            return;
        }
        // Validar el campo de teléfono
        if (TextUtils.isEmpty(phone)) {
            ed_telef.setError("Teléfono requerido");
            ed_telef.requestFocus();
            return;
        } else if (!phone.matches("[0-9]+") || phone.length() != 10) {
            ed_telef.setError("El teléfono debe contener 10 números");
            ed_telef.requestFocus();
            return;
        }
        //Se cumplio las acciones
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
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Id", userID);
                    user.put("username", username);
                    user.put("email", email);
                    user.put("phone", phone);
                    user.put("school", "");
                    user.put("birthDay", "");
                    user.put("photo", "");
                    user.put("destinationLocation", "");
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "El perfil se creo con el id" + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), login.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sign_in.this, "Error ! El Correo ya esta registrado", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}