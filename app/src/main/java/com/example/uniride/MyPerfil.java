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

import com.google.android.gms.common.images.ImageManager;
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
    Dialog d_edit;
    String uBirthDay;

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

        //----Botones de Editar----
        d_edit = new Dialog(this);

        btnEditAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                openDialogFecha();
            }
        });

        btnEditSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                openDialogSchool_and_Phone("Facultad a la que Asistes", 1);
            }
        });

        btnEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                openDialogSchool_and_Phone("Telefono", 2);
            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                openDialogContasena();
            }
        });



        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                onBackPressed();
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                uploadPhoto();

            }
        });

        setInformation();
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
                uName.setText(value.getString("username"));
                uAge.setText(calcularEdad(value.getString("birthDay")));
                uBirthDay = value.getString("birthDay").replace("/", " / ");
                uSchool.setText(value.getString("school"));
                uPhone.setText(value.getString("phone"));
                if(value.getString("photo").isEmpty()){
                    Picasso.get().load(R.drawable.person_2).into(imgUser);
                }else{
                    Picasso.get().load(value.getString("photo")).into(imgUser);
                }
            }
        });
    }

    private String calcularEdad(String fecha) {
        String edad = "";
        String[] parts = fecha.split("/");
        int dia = Integer.parseInt(parts[0]); // dia
        int mes = Integer.parseInt(parts[1]); // mes
        int ano = Integer.parseInt(parts[2]); // año

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate today = LocalDate.now();
            LocalDate birthdate = LocalDate.of(ano, mes, dia);
            Period p = Period.between(birthdate, today);
            edad = p.getYears() + " años";

        }

        return edad;
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
        String rute_storage_photo = "photoUserProfile/" + mAuth.getUid();
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
                            fStore.collection("users").document(mAuth.getUid()).update(map);
                            Toast.makeText(MyPerfil.this, "Foto Actualizada Correctamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyPerfil.this, "Error al cargar la Foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialogFecha(){
        d_edit.setContentView(R.layout.edit_user_dialog2);
        d_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_edit.setCanceledOnTouchOutside(false);
        d_edit.show();

        EditText et_dato = d_edit.findViewById(R.id.et_dato);
        Button btnGuardar = d_edit.findViewById(R.id.btnGuardar);
        ImageView btnCalendar = d_edit.findViewById(R.id.btnAbrirCalendario);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] parts = uBirthDay.split(" / ");
                int dia = Integer.parseInt(parts[0]);
                int mes = Integer.parseInt(parts[1]);
                int ano = Integer.parseInt(parts[2]);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MyPerfil.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et_dato.setText(dayOfMonth+" / "+(month+1)+" / "+year);
                    }
                },ano,mes-1,dia);
                datePickerDialog.show();
            }
        });

        btnGuardar.setEnabled(false);
        et_dato.setFocusable(false);
        et_dato.setText(uBirthDay);
        et_dato.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(et_dato.getText().toString().equals(uBirthDay)){
                    btnGuardar.setEnabled(false);
                }else{
                    btnGuardar.setEnabled(true);
                }
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("birthDay", et_dato.getText().toString().replace(" / ", "/"));
                fStore.collection("users").document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        uAge.setText(calcularEdad(et_dato.getText().toString().replace(" / ", "/")));
                        enableButtons();
                        d_edit.dismiss();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Edad Actualizada Correctamente", Snackbar.LENGTH_LONG);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.purple_500)); // Establecer el color de fondo
                        snackbar.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error al actualizar la edad", Snackbar.LENGTH_LONG);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.red)); // Establecer el color de fondo
                        snackbar.show();
                    }
                });
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
    }


    private void openDialogSchool_and_Phone(String tDato, int opcion) {
        d_edit.setContentView(R.layout.edit_user_dialog);
        d_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_edit.setCanceledOnTouchOutside(false);
        d_edit.show();

        TextView datoText = d_edit.findViewById(R.id.datoEdit);
        EditText et_dato = d_edit.findViewById(R.id.et_dato);
        Button btnGuardar = d_edit.findViewById(R.id.btnGuardar);

        btnGuardar.setEnabled(false);
        datoText.setText(tDato);

        switch(opcion){
            case 1:
                et_dato.setText(uSchool.getText());
                et_dato.setFilters(new InputFilter[]{new InputFilter.LengthFilter(35)});
                et_dato.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(et_dato.getText().toString().equals(uSchool.getText().toString())){
                            btnGuardar.setEnabled(false);
                        }else{
                            btnGuardar.setEnabled(true);
                        }
                    }
                });
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("school", et_dato.getText().toString());
                        fStore.collection("users").document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                uSchool.setText(et_dato.getText().toString());
                                d_edit.dismiss();
                                enableButtons();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Escuela Actualizada Correctamente", Snackbar.LENGTH_LONG);
                                View snackbarView = snackbar.getView();
                                snackbarView.setBackgroundColor(getResources().getColor(R.color.purple_500)); // Establecer el color de fondo
                                snackbar.show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error al actualizar la edad", Snackbar.LENGTH_LONG);
                                View snackbarView = snackbar.getView();
                                snackbarView.setBackgroundColor(getResources().getColor(R.color.red)); // Establecer el color de fondo
                                snackbar.show();
                            }
                        });

                    }
                });
                break;
            case 2:
                et_dato.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_dato.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                et_dato.setText(uPhone.getText());
                et_dato.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(et_dato.getText().toString().equals(uPhone.getText().toString())){
                            btnGuardar.setEnabled(false);
                        }else{
                            btnGuardar.setEnabled(true);
                        }
                    }
                });
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("phone", et_dato.getText().toString());
                        fStore.collection("users").document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                uSchool.setText(et_dato.getText().toString());
                                d_edit.dismiss();
                                enableButtons();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Telefono Actualizado Correctamente", Snackbar.LENGTH_LONG);
                                View snackbarView = snackbar.getView();
                                snackbarView.setBackgroundColor(getResources().getColor(R.color.purple_500)); // Establecer el color de fondo
                                snackbar.show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error al actualizar la edad", Snackbar.LENGTH_LONG);
                                View snackbarView = snackbar.getView();
                                snackbarView.setBackgroundColor(getResources().getColor(R.color.red)); // Establecer el color de fondo
                                snackbar.show();
                            }
                        });
                    }
                });
                break;
        }

        ImageView btnClose = d_edit.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableButtons();
                d_edit.dismiss();
            }
        });

    }

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

    }

    private void disableButtons() {
        btnFoto.setEnabled(false);//Deshabilitar recycleview
        btnAtras.setEnabled(false);
        btnEditPassword.setEnabled(false);
        btnEditPhone.setEnabled(false);
        btnEditAge.setEnabled(false);
        btnEditSchool.setEnabled(false);
    }
    private void enableButtons() {
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