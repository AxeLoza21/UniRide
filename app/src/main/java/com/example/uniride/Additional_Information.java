package com.example.uniride;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Additional_Information extends AppCompatActivity {

    Button btnContinue;
    ImageView abrirCalendario, imgUser;
    EditText etNacimiento, etEscuela;
    CardView btnFoto;

    private Uri image_url;
    private static final int COD_SEL_IMAGE = 300;
    private static final int COD_SEL_STORAGE = 200;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_information);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        etNacimiento = (EditText)findViewById(R.id.et_fechaNacimiento);
        etNacimiento.setFocusable(false);
        etEscuela = (EditText)findViewById(R.id.et_Asistes);
        etEscuela.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        abrirCalendario = (ImageView)findViewById(R.id.btnAbrirCalendario);
        btnFoto = (CardView)findViewById(R.id.btnUploadPhoto);
        imgUser = (ImageView)findViewById(R.id.imgUser);
        btnContinue = (Button)findViewById(R.id.btnContinue);


        estadoInpust();

        abrirCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario();
            }
        });

        etEscuela.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    estadoInpust();
            }
        });


        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("users").document(fAuth.getUid());
                Map<String, Object> user = new HashMap<>();
                user.put("school", etEscuela.getText().toString());
                user.put("birthDay", etNacimiento.getText().toString().replace(" ",""));
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), selectLocation.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
            }
        });

    }



    private void abrirCalendario() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(Additional_Information.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etNacimiento.setText(dayOfMonth+" / "+(month+1)+" / "+year);
                estadoInpust();
            }
        }
        ,2000,1,1);
        datePickerDialog.show();

    }

    private void estadoInpust() {
        if(!etNacimiento.getText().toString().isEmpty() && etEscuela.length() > 5){
            btnContinue.setEnabled(true);
        }else{
            btnContinue.setEnabled(false);
        }
    }


    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if(requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                imgUser.setImageURI(image_url);
                subirPhoto(image_url);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void subirPhoto(Uri image_url) {
        String rute_storage_photo = "photoUserProfile/" + fAuth.getUid();
        storageReference.child(rute_storage_photo).putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                    if(uriTask.isSuccessful()){
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url_photo = uri.toString();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("photo", url_photo);
                                fStore.collection("users").document(fAuth.getUid()).update(map);
                                Toast.makeText(Additional_Information.this, "Foto cargada Correctamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Additional_Information.this, "Error al cargar la Foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

}