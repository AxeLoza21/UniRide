package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class
login extends AppCompatActivity {
    TextView registrarse, forgotpassword;
    Button login;
    TextInputEditText  ed_email, ed_password;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    int REQUEST_CODE = 200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        registrarse = (TextView)findViewById(R.id.btnRegistro);
        login = (Button)findViewById(R.id.btn_login);
        ed_email = (TextInputEditText)findViewById(R.id.et_usarname);
        ed_password = (TextInputEditText)findViewById(R.id.et_password);
        forgotpassword = (TextView)findViewById(R.id.Forgotpassword);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                String email = ed_email.getText().toString().trim();
                String password = ed_password.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    ed_email.setError("Correo requerido");
                    ed_email.requestFocus();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return;
                } else if (!isValidEmail(email)) {
                    // Validar formato del correo
                    ed_email.setError("Correo inválido. El correo debe tener el formato 'ejemplo@ucol.mx' y contener solo letras y números.");
                    ed_email.requestFocus();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return;
                }

                // Validar el campo de contraseña
                if (TextUtils.isEmpty(password)) {
                    ed_password.setError("Contraseña requerida", null);
                    ed_password.requestFocus();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return;
                } else if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$")) {
                    ed_password.setError("La contraseña debe tener al menos una letra mayúscula, dos números, un carácter especial y ser mayor a 8 caracteres", null);
                    ed_password.requestFocus();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    return;
                }
                //Se cumplio las acciones
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            fAuth = FirebaseAuth.getInstance();
                            user = fAuth.getCurrentUser();
                            //int permiso_location_precisa = ContextCompat.checkSelfPermission(login.this, Manifest.permission.ACCESS_FINE_LOCATION);
                            //if (permiso_location_precisa == PackageManager.PERMISSION_GRANTED) {
                                // Permiso concedido, continuar con la lógica después de la autenticación
                                // ...
                            if (!user.isEmailVerified()) {
                                Toast.makeText(login.this, "Debes de verificar el correo", Toast.LENGTH_SHORT).show();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                fAuth.signOut();

                            } else if (user.isEmailVerified()) {
                                DocumentReference documentReference = fStore.collection("users").document(user.getUid());
                                documentReference.addSnapshotListener(com.example.uniride.login.this, new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        String school = value.getString("school");
                                        String birthDay = value.getString("birthDay");
                                        String location = value.getString("destinationLocation");

                                        if(school.isEmpty() || birthDay.isEmpty()){
                                            startActivity(new Intent(getApplicationContext(),Additional_Information.class));
                                            finish();
                                        }else if (location.isEmpty()){
                                            startActivity(new Intent(getApplicationContext(),selectLocation.class));
                                            finish();
                                        }else{
                                            startActivity(new Intent(getApplicationContext(),MainActivityFragment.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this, "Error al iniciar sesión. Verifica tu correo y contraseña", Toast.LENGTH_SHORT).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResetPasswordDialog();
            }
        });
    }
    // Función para validar el formato del correo
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@ucol\\.mx$";
        return email.matches(emailRegex);
    }
    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_password_dialog, null);
        builder.setView(dialogView);

        final EditText emailEditText = dialogView.findViewById(R.id.emailEditText);

        builder.setPositiveButton(R.string.reset_password, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailAddress = emailEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(emailAddress)) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // El correo electrónico de restablecimiento de contraseña se ha enviado correctamente
                                        Toast.makeText(login.this, "El correo electrónico de restablecimiento de contraseña se ha enviado correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Error al enviar el correo electrónico de restablecimiento de contraseña
                                        Toast.makeText(login.this, "Error al enviar el correo electrónico de restablecimiento de contraseña", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // El campo de entrada de correo electrónico está vacío, muestra un mensaje de error
                    Toast.makeText(login.this, "Ingrese una dirección de correo electrónico válida", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario canceló la operación de restablecimiento de contraseña
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}