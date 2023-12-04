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

public class SelectCuota extends AppCompatActivity {

    ImageButton aumentar,disminuir;
    Button continuar;
    TextView contadorTexto;
    int contador = 0;
    HashMap<String, Object> datos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cuota);

        aumentar = (ImageButton) findViewById(R.id.btnAumentar);
        disminuir = (ImageButton) findViewById(R.id.btnDisminuir);
        contadorTexto = (TextView) findViewById(R.id.txtCount);
        continuar = (Button) findViewById(R.id.btnContinue);


        //----Recibir los datos anteriores----
        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");

        if(getIntent().getBooleanExtra("editar", false)){
            contador = Integer.parseInt(datos.get("travelPrice").toString());
            contadorTexto.setText(String.valueOf("$"+contador));
            continuar.setText("Confirmar");
        }else{
            //Nada
        }

        aumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contador < 20) {
                    contador+=5;
                    contadorTexto.setText(String.valueOf("$"+contador));
                }else if (contador == 20){
                    Toast.makeText(SelectCuota.this,"El maximo de cuota es $20",Toast.LENGTH_SHORT).show();
                }
            }
        });
        disminuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contador > 0) {
                    contador-=5;
                    contadorTexto.setText(String.valueOf("$"+contador));
                }else if (contador == 0){
                    //Nada
                }

            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle cDatos = new Bundle();
                datos.put("travelPrice", ""+contador);
                cDatos.putSerializable("datos", datos);

                if(getIntent().getBooleanExtra("editar", false)){
                    Intent d = new Intent(SelectCuota.this, CreatePublicationDetails.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                    finish();
                }else{
                    Intent d = new Intent(SelectCuota.this, DescriptionPublication.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                }


            }
        });
    }
}