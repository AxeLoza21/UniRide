package com.example.uniride.components;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.uniride.MainActivityFragment;
import com.example.uniride.MyPerfil;
import com.example.uniride.R;
import com.example.uniride.functions.CalculateAge;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class DialogElement {

    Activity activity;
    SnackBarElement snackbar;
    CalculateAge edad;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;


    public DialogElement(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        snackbar = new SnackBarElement(activity);
        edad = new CalculateAge();
    }

    //----------Metodos para mostrar los diferentes Dialogs----------

    //-----Dialog para editar la Edad-----
    public void showDialogAge(String uBirthDay){

        Dialog d_edit = new Dialog(activity);
        TextView uAge = (TextView)activity.findViewById(R.id.edad);

        d_edit.setContentView(R.layout.edit_user_dialog2);
        d_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_edit.setCanceledOnTouchOutside(false);
        d_edit.show();

        EditText et_dato = d_edit.findViewById(R.id.et_dato);
        Button btnGuardar = d_edit.findViewById(R.id.btnGuardar);
        ImageView btnCalendar = d_edit.findViewById(R.id.btnAbrirCalendario);

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

        //------Boton para abrir el calendario-------
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] parts = uBirthDay.split(" / ");
                int dia = Integer.parseInt(parts[0]);
                int mes = Integer.parseInt(parts[1]);
                int ano = Integer.parseInt(parts[2]);
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et_dato.setText(dayOfMonth+" / "+(month+1)+" / "+year);
                    }
                },ano,mes-1,dia);
                datePickerDialog.show();
            }
        });

        //-------Boton para guardar los cambios-------
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("birthDay", et_dato.getText().toString().replace(" / ", "/"));
                fStore.collection("users").document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        uAge.setText(edad.calcularEdad(et_dato.getText().toString().replace(" / ", "/")));
                        d_edit.dismiss();
                        snackbar.showSnackBar(activity.getResources().getColor(R.color.purple_500), "Edad Actualizada Correctamente");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackbar.showSnackBar(activity.getResources().getColor(R.color.red),"Error al actualizar la edad");

                    }
                });
            }
        });

        //-------Boton para cerrar el dialog-------
        ImageView btnClose = d_edit.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_edit.dismiss();
            }
        });
    }
    //---------------------------------

    //------Dialog para editar la Escuela o el telefono------
    public void showDialogSchoolYPhone(String tDato, int opcion){

        Dialog d_edit = new Dialog(activity);


        d_edit.setContentView(R.layout.edit_user_dialog);
        d_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_edit.setCanceledOnTouchOutside(false);
        d_edit.show();

        TextView datoText = d_edit.findViewById(R.id.datoEdit);
        EditText et_dato = d_edit.findViewById(R.id.et_dato);
        Button btnGuardar = d_edit.findViewById(R.id.btnGuardar);

        btnGuardar.setEnabled(false);
        datoText.setText(tDato);

        //Switch para identificar si se va a editar la escuela o el telefono
        switch(opcion){
            //-----Editar la escuela-----
            case 1:
                TextView uSchool = (TextView)activity.findViewById(R.id.nEscuela);

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

                //-------Boton para guardar los cambios de la escuela-------
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("school", et_dato.getText().toString());
                        fStore.collection("users").document(mAuth.getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                uSchool.setText(et_dato.getText().toString());
                                d_edit.dismiss();
                                snackbar.showSnackBar(activity.getResources().getColor(R.color.purple_500), "Escuela Actualizada Correctamente");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                d_edit.dismiss();
                                snackbar.showSnackBar(activity.getResources().getColor(R.color.red), "Error al actualizar el campo");

                            }
                        });
                    }
                });
                break;

            //-----Editar el Telefono-----
            case 2:
                TextView uPhone = (TextView)activity.findViewById(R.id.telefono);

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

                //-------Boton para guardar los cambios del telefono-------
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("phone", et_dato.getText().toString());
                        fStore.collection("users").document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                uPhone.setText(et_dato.getText().toString());
                                d_edit.dismiss();
                                snackbar.showSnackBar(activity.getResources().getColor(R.color.purple_500), "Telefono Actualizado Correctamente");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                snackbar.showSnackBar(activity.getResources().getColor(R.color.red),"Error al actualizar la edad");
                            }
                        });
                    }
                });
                break;

        }

        //-------Boton para cerrar el dialog-------
        ImageView btnClose = d_edit.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_edit.dismiss();
            }
        });
    }


    public void showDialogPassword(){

    }

    public void showDialogWarningExit(){
        Dialog d_edit = new Dialog(activity);

        d_edit.setContentView(R.layout.warning_exit_dialog);
        d_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_edit.setCanceledOnTouchOutside(true);
        d_edit.show();

        //-------Boton para borrar el viaje-------
        Button btnSalir = d_edit.findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, MainActivityFragment.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(i);
                activity.finish();
            }
        });

        //-------Boton para cerrar el dialog-------
        Button btnCancelar = d_edit.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_edit.dismiss();
            }
        });
    }

    public void showDialogConfirmPredeterminado(String idDocumento, String idUser){
        Dialog d_edit = new Dialog(activity);


        d_edit.setContentView(R.layout.confirm_predeterminado_dialog);
        d_edit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d_edit.setCanceledOnTouchOutside(true);
        d_edit.show();

        //-------Boton para borrar el viaje-------
        Button btnSi = d_edit.findViewById(R.id.btnSi);
        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_edit.dismiss();
                fStore.collection("users").document(idUser).collection("cars").document(idDocumento).update("predeterminado",true);
                fStore.collection("users").document(idUser).collection("cars").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot cars : task.getResult().getDocuments()) {
                            if(!idDocumento.equals(cars.getId())){
                                fStore.collection("users").document(idUser).collection("cars").document(cars.getId()).update("predeterminado",false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        //-------Boton para cerrar el dialog-------
        Button btnNo = d_edit.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_edit.dismiss();
            }
        });
    }


}
