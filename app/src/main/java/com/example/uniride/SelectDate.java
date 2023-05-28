package com.example.uniride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uniride.functions.FormatDateName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class SelectDate extends AppCompatActivity {
    private TextView selectedDateText;
    private CalendarView datePicker;
    Button continuar;
    String fecha;

    HashMap<String, Object> datos = new HashMap<>();
    FormatDateName cf;
    DateFormat formateador= new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        selectedDateText = findViewById(R.id.selected_date_text);
        datePicker = findViewById(R.id.date_picker);
        continuar = (Button) findViewById(R.id.btnContinue);
        cf = new FormatDateName();

        //----Recibir los datos anteriores----
        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");


        if(getIntent().getBooleanExtra("editar", false)){
            fecha = datos.get("datePublication").toString();
            continuar.setText("Confirmar");
        }else{
            //Nada
        }

        // Set the minimum date to today's date
        Calendar calendar = new GregorianCalendar();
        if(getIntent().getBooleanExtra("editar", false)){
            try {
                calendar = Calendar.getInstance();
                datePicker.setMinDate(calendar.getTimeInMillis());
                Date fechaAnt = formateador.parse(fecha);
                calendar.setTime(fechaAnt);
                fecha = formateador.format(calendar.getTime());
                datePicker.setDate(calendar.getTimeInMillis());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            calendar = Calendar.getInstance();
            datePicker.setMinDate(calendar.getTimeInMillis());
            fecha = formateador.format(calendar.getTime());
        }
        selectedDateText.setText(cf.getDiaSemana(fecha)+" "+fecha.substring(0,2)+" de "+cf.getNombreMes(fecha)+" del "+fecha.substring(6));

        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                fecha = (String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year));
                selectedDateText.setText(cf.getDiaSemana(fecha)+" "+fecha.substring(0,2)+" de "+cf.getNombreMes(fecha)+" del "+fecha.substring(6));

            }
        });


        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle cDatos = new Bundle();
                datos.put("datePublication", fecha);
                cDatos.putSerializable("datos", datos);

                if(getIntent().getBooleanExtra("editar", false)){
                    Intent d = new Intent(SelectDate.this, CreateTravelDetails.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                    finish();
                }else{
                    Intent d = new Intent(SelectDate.this, SelectTime.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                }

            }
        });



    }
}