package com.example.uniride.publicationCreation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uniride.R;

import java.util.HashMap;

public class SelectStudents extends AppCompatActivity {
    ImageButton aumentar,disminuir;
    Button continuar;
    TextView contadorTexto;
    int contador = 1;
    HashMap<String, Object> datos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_students);

        aumentar = (ImageButton) findViewById(R.id.btnAumentar);
        disminuir = (ImageButton) findViewById(R.id.btnDisminuir);
        contadorTexto = (TextView) findViewById(R.id.txtCount);
        continuar = (Button) findViewById(R.id.btnContinue);


        //----Recibir los datos anteriores----
        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");

        if(getIntent().getBooleanExtra("editar", false)){
            contador = Integer.parseInt(datos.get("travelSeating").toString());
            contadorTexto.setText(String.valueOf(contador));
            continuar.setText("Confirmar");
        }else{
            //Nada
        }

        aumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contador < 4) {
                    contador++;
                    contadorTexto.setText(String.valueOf(contador));
                }else if (contador == 4){
                    Toast.makeText(SelectStudents.this,"El maximo de estudiantes es 4",Toast.LENGTH_SHORT).show();
                }
            }
        });
        disminuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contador > 1) {
                    contador--;
                    contadorTexto.setText(String.valueOf(contador));
                }else if (contador == 1){
                    Toast.makeText(SelectStudents.this,"El minino de estudiantes es 1",Toast.LENGTH_SHORT).show();
                }

            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle cDatos = new Bundle();
                datos.put("travelSeating", ""+contador);
                cDatos.putSerializable("datos", datos);

                if(getIntent().getBooleanExtra("editar", false)){
                    Intent d = new Intent(SelectStudents.this, CreatePublicationDetails.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                    finish();
                }else{
                    Intent d = new Intent(SelectStudents.this, SelectCuota.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                }


            }
        });
    }
}