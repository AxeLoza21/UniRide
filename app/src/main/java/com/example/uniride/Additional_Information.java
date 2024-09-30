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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uniride.functions.UploadPhoto;
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
    AutoCompleteTextView Cargo;
    String [] opciones = {"Trabajador institucional","Estudiante"};

    private Uri image_url;
    private static final int COD_SEL_IMAGE = 300;
    UploadPhoto foto;

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

        foto = new UploadPhoto(this);

        etNacimiento = (EditText)findViewById(R.id.et_fechaNacimiento);
        etNacimiento.setFocusable(false);
        etEscuela = (EditText)findViewById(R.id.et_Asistes);
        Cargo = (AutoCompleteTextView) findViewById(R.id.SpinerCargo);
        etEscuela.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        abrirCalendario = (ImageView)findViewById(R.id.btnAbrirCalendario);
        btnFoto = (CardView)findViewById(R.id.btnUploadPhoto);
        imgUser = (ImageView)findViewById(R.id.imgUser);
        btnContinue = (Button)findViewById(R.id.btnContinue);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(Additional_Information.this,R.layout.listviewresours, opciones);
        Cargo.setAdapter(aa);
        Cargo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                estadoInpust();
            }
        });


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
                foto.uploadPhoto();
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
        String cargo = Cargo.getText().toString();

        if(!etNacimiento.getText().toString().isEmpty() && etEscuela.length() > 5  && (cargo.equals("Estudiante") || cargo.equals("Trabajador institucional"))){
            btnContinue.setEnabled(true);
        }else{
            btnContinue.setEnabled(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if(requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                imgUser.setImageURI(image_url);
                foto.subirPhoto(image_url);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}