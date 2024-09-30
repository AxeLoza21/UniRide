package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
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

public class login extends AppCompatActivity {
    TextView registrarse, forgotpassword;
    Button login;
    TextInputEditText ed_email, ed_password;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialización de los elementos
        registrarse = findViewById(R.id.btnRegistro);
        login = findViewById(R.id.btn_login);
        ed_email = findViewById(R.id.et_usarname);
        ed_password = findViewById(R.id.et_password);
        forgotpassword = findViewById(R.id.Forgotpassword);

        // Inicialización de Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Evento para registrarse
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, sign_in.class));
            }
        });

        // Evento de inicio de sesión
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableTouch();
                String email = ed_email.getText().toString().trim();
                String password = ed_password.getText().toString().trim();

                // Validación de los campos
                if (!validateInputs(email, password)) {
                    enableTouch();
                    return;
                }

                // Iniciar sesión con Firebase
                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                user = fAuth.getCurrentUser();
                                if (user != null) {
                                    if (!user.isEmailVerified()) {
                                        showToast("Debes verificar el correo");
                                        fAuth.signOut();
                                        enableTouch();
                                    } else {
                                        checkUserDetails();
                                    }
                                }
                            } else {
                                showToast("Error al iniciar sesión. Verifica tu correo y contraseña");
                                enableTouch();
                            }
                        });
            }
        });

        // Evento de restablecimiento de contraseña
        forgotpassword.setOnClickListener(view -> showResetPasswordDialog());
    }

    // Validar el formato del correo y la contraseña
    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            ed_email.setError("Correo requerido");
            ed_email.requestFocus();
            return false;
        } else if (!isValidEmail(email)) {
            ed_email.setError("Correo inválido. Debe ser 'ejemplo@ucol.mx'.");
            ed_email.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            ed_password.setError("Contraseña requerida");
            ed_password.requestFocus();
            return false;
        } else if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$")) {
            ed_password.setError("Contraseña debe tener mayúscula, número, carácter especial y más de 8 caracteres");
            ed_password.requestFocus();
            return false;
        }

        return true;
    }

    // Validación del formato de correo
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@ucol\\.mx$";
        return email.matches(emailRegex);
    }

    // Checar detalles del usuario tras iniciar sesión
    private void checkUserDetails() {
        DocumentReference documentReference = fStore.collection("users").document(user.getUid());
        documentReference.addSnapshotListener(this, (value, error) -> {
            if (value != null && value.exists()) {
                String school = value.getString("school");
                String birthDay = value.getString("birthDay");
                String location = value.getString("destinationLocation");

                // Verificar si falta información adicional
                if (TextUtils.isEmpty(school) || TextUtils.isEmpty(birthDay)) {
                    navigateToActivity(Additional_Information.class);
                } else if (TextUtils.isEmpty(location)) {
                    navigateToActivity(selectLocation.class);
                } else {
                    navigateToActivity(MainActivityFragment.class);
                }
            } else {
                showToast("Error al recuperar detalles del usuario");
                enableTouch();
            }
        });
    }

    // Deshabilitar interacción mientras espera
    private void disableTouch() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // Habilitar interacción
    private void enableTouch() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // Mostrar mensaje
    private void showToast(String message) {
        Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
    }

    // Navegar a otra actividad
    private void navigateToActivity(Class<?> activityClass) {
        startActivity(new Intent(getApplicationContext(), activityClass));
        finish();
    }

    // Mostrar diálogo para restablecer contraseña
    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.reset_password_dialog, null);
        builder.setView(dialogView);

        final EditText emailEditText = dialogView.findViewById(R.id.emailEditText);

        builder.setPositiveButton(R.string.reset_password, (dialog, which) -> {
            String emailAddress = emailEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(emailAddress)) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Correo de restablecimiento enviado");
                    } else {
                        showToast("Error al enviar el correo de restablecimiento");
                    }
                });
            } else {
                showToast("Ingrese un correo válido");
            }
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
