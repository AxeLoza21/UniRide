package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class DescriptionTravel extends AppCompatActivity {

    Button continuar;
    EditText description;
    HashMap<String, Object> datos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_travel);

        continuar = (Button)findViewById(R.id.btnContinue);
        description = (EditText)findViewById(R.id.et_descripction);


        //----Recibir los datos anteriores----
        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");

        if(getIntent().getBooleanExtra("editar", false)){
            description.setText(datos.get("travelDescription").toString().trim());
            continuar.setText("Confirmar");
        }else{
            //Nada
        }

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cDatos = new Bundle();
                datos.put("travelDescription", description.getText().toString().trim());
                cDatos.putSerializable("datos", datos);

                if(getIntent().getBooleanExtra("editar", false)){
                    Intent d = new Intent(DescriptionTravel.this, CreateTravelDetails.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                    finish();
                }else{
                    Intent d = new Intent(DescriptionTravel.this, CreateTravelDetails.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                }
            }
        });
    }
}