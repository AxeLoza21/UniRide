package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uniride.components.DialogElement;
import com.example.uniride.components.SnackBarElement;
import com.example.uniride.functions.CalculateAge;
import com.example.uniride.functions.UploadPhoto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;

public class MyPerfil extends AppCompatActivity {

    ImageView btnAtras, imgUser, btnEditAge, btnEditSchool, btnEditPhone, btnEditPassword;
    TextView uName, uAge, uSchool, uPhone, uPassword;
    CardView btnFoto;
    String uBirthDay;

    DialogElement d_edit;
    CalculateAge edad;
    UploadPhoto foto;

    private Uri image_url;
    private static final int COD_SEL_IMAGE = 300;
    private static final int COD_SEL_STORAGE = 200;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_perfil);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        d_edit = new DialogElement(this);
        foto = new UploadPhoto(this);
        edad = new CalculateAge();

        uName = (TextView)findViewById(R.id.nombre);
        uAge = (TextView)findViewById(R.id.edad);
        uSchool = (TextView)findViewById(R.id.nEscuela);
        uPhone = (TextView)findViewById(R.id.telefono);
        uPassword = (TextView)findViewById(R.id.contrasena);

        imgUser = (ImageView)findViewById(R.id.imgUser);

        btnEditAge = (ImageView)findViewById(R.id.editAge);
        btnEditSchool = (ImageView)findViewById(R.id.editSchool);
        btnEditPhone = (ImageView)findViewById(R.id.editPhone);
        btnEditPassword = (ImageView)findViewById(R.id.editPassword);
        btnFoto = (CardView)findViewById(R.id.btnUploadPhoto);
        btnAtras = (ImageView)findViewById(R.id.btn_back);

        //Colocar la informacion del usuario de la base de datos en sus respectivos campos.
        setInformation();

        //--------------------Botones de Editar-----------------------
        btnEditAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                d_edit.showDialogAge(uBirthDay);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enableButtons();
                    }
                }, 500);
            }
        });

        btnEditSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                d_edit.showDialogSchoolYPhone("Facultad a la que Asistes", 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enableButtons();
                    }
                }, 500);
            }
        });

        btnEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                d_edit.showDialogSchoolYPhone("Telefono", 2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enableButtons();
                    }
                }, 500);


            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disableButtons();
                //openDialogContasena();
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                foto.uploadPhoto();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enableButtons();
                    }
                }, 500);

            }
        });
        //---------------------------------------------------------------

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableButtons();
    }

    private void setInformation() {
        fStore.collection("users").document(mAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    uName.setText(value.getString("username"));
                    uAge.setText(edad.calcularEdad(value.getString("birthDay")));
                    uBirthDay = value.getString("birthDay").replace("/", " / ");
                    uSchool.setText(value.getString("school"));
                    uPhone.setText(value.getString("phone"));
                    if(value.getString("photo").isEmpty()){
                        Picasso.get().load(R.drawable.person_2).into(imgUser);
                    }else{
                        Picasso.get().load(value.getString("photo")).into(imgUser);
                    }
                }else{
                    // El documento no existe
                }

            }
        });
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


    /*
    private void openDialogContasena(){
        d_edit.setContentView(R.layout.edit_user_dialog3);
        d_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_edit.setCanceledOnTouchOutside(false);
        d_edit.show();

        Button btnGuardar = d_edit.findViewById(R.id.btnGuardar);
        TextInputEditText et_actPass, et_newPass, et_rNewPass;
        //et_actPass = d_edit.findViewById(R.id.et_passAct);
        et_newPass = d_edit.findViewById(R.id.et_passNew);
        et_rNewPass = d_edit.findViewById(R.id.et_rpassNew);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_newPass.getText().toString().trim();
                String rpwd = et_rNewPass.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    et_newPass.setError("Contraseña requerida", null);
                    et_newPass.requestFocus();
                    return;
                } else if(!pwd.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$")) {
                    et_newPass.setError("La contraseña debe tener al menos una letra mayúscula, dos números, un carácter especial y ser mayor a 8 caracteres", null);
                    et_newPass.requestFocus();
                    return;
                }

                // Validar el campo de repetir contraseña
                if (TextUtils.isEmpty(rpwd)) {
                    et_rNewPass.setError("Repetir contraseña requerido", null);
                    et_rNewPass.requestFocus();
                    return;
                } else if (!rpwd.equals(pwd)) {
                    et_rNewPass.setError("Las contraseñas no coinciden", null);
                    et_rNewPass.requestFocus();
                    return;
                }

                //Se cumplieron las acciones
                mAuth.getCurrentUser().updatePassword(pwd);
                Toast.makeText(MyPerfil.this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                d_edit.dismiss();
                enableButtons();

            }
        });

        ImageView btnClose = d_edit.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableButtons();
                d_edit.dismiss();
            }
        });

    }*/

    public void disableButtons() {
        btnFoto.setEnabled(false);//Deshabilitar recycleview
        btnAtras.setEnabled(false);
        btnEditPassword.setEnabled(false);
        btnEditPhone.setEnabled(false);
        btnEditAge.setEnabled(false);
        btnEditSchool.setEnabled(false);
    }
    public void enableButtons() {
        btnFoto.setEnabled(true);//Deshabilitar recycleview
        btnAtras.setEnabled(true);
        btnEditPassword.setEnabled(true);
        btnEditPhone.setEnabled(true);
        btnEditAge.setEnabled(true);
        btnEditSchool.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}