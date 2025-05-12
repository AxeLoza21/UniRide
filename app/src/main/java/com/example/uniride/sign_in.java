package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uniride.fragments.ChatFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class sign_in extends AppCompatActivity {

    ImageView btnBack;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatabaseReference dbReference;
    Button btn_register;
    TextInputEditText ed_user, ed_email, ed_password, ed_repitpassword, ed_telef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnBack = findViewById(R.id.btnBackLoogin);
        btn_register = findViewById(R.id.btn_interested);
        ed_email = findViewById(R.id.et_email);
        ed_password = findViewById(R.id.et_password);
        ed_user = findViewById(R.id.et_usarname);
        ed_repitpassword = findViewById(R.id.et_repPassword);
        ed_telef = findViewById(R.id.et_phone);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://uniride-ec144-default-rtdb.firebaseio.com/");

        btnBack.setOnClickListener(v -> onBackPressed());

        btn_register.setOnClickListener(v -> RegisterProgress());
    }

    private void RegisterProgress() {
        final String email = ed_email.getText().toString().trim() + "@ucol.mx";
        final String username = ed_user.getText().toString().trim();
        String password = ed_password.getText().toString().trim();
        String phone = ed_telef.getText().toString().trim();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userID = fAuth.getCurrentUser().getUid();
                Map<String, Object> user = new HashMap<>();
                user.put("Id", userID);
                user.put("username", username);
                user.put("email", email);
                user.put("phone", phone);
                user.put("profile_pic", "");

                dbReference.child("users").child(userID).setValue(user);

                goToChatFragment(userID, username, email, phone, "");
            } else {
                Toast.makeText(sign_in.this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Esta función está diseñada para navegar desde la actividad actual hacia ChatFragment
    private void goToChatFragment(String userID, String username, String email, String phone, String profilePicUrl) {
        ChatFragment chatFragment = new ChatFragment();
        // Función para ir al Fragment de chat.
        Bundle args = new Bundle();
        // Crea un Bundle para pasar datos.
        args.putString("userID", userID);
        args.putString("username", username);
        args.putString("email", email);
        args.putString("phone", phone);
        args.putString("profile_pic", profilePicUrl);
// Coloca los datos del usuario en el Bundle.
        chatFragment.setArguments(args);
        // Asigna el Bundle al ChatFragment.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, chatFragment)
                .addToBackStack(null)
                .commit();
    }
}
