package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SelectStudents extends AppCompatActivity {
    ImageButton aumentar,disminuir;
    Button continuar;
    TextView contadorTexto;
    int contador = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_students);

        aumentar = (ImageButton) findViewById(R.id.btnAumentar);
        disminuir = (ImageButton) findViewById(R.id.btnDisminuir);
        contadorTexto = (TextView) findViewById(R.id.txtCount);
        continuar = (Button) findViewById(R.id.btnContinue);
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
                Intent i = new Intent(SelectStudents.this, DescriptionTravel.class);
                startActivity(i);
            }
        });
    }
}