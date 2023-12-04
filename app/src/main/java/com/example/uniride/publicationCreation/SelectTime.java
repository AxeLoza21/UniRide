package com.example.uniride.publicationCreation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.uniride.R;

import java.util.HashMap;
import java.util.Locale;

public class SelectTime extends AppCompatActivity {
    Button continuar;
    private TextView selectedTimeText;
    private TimePicker timePicker;
    String hora;
    HashMap<String, Object> datos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        selectedTimeText = findViewById(R.id.selected_time_text);
        timePicker = findViewById(R.id.time_picker);
        continuar = (Button) findViewById(R.id.btnContinue);

        timePicker.setIs24HourView(false);


        //----Recibir los datos anteriores----
        Bundle c = getIntent().getExtras();
        datos = (HashMap<String, Object>) c.getSerializable("datos");

        if(getIntent().getBooleanExtra("editar", false)){
            String timeAnt = datos.get("timePublication").toString();
            int houreAnt = Integer.parseInt(timeAnt.substring(0,2));
            int minuteAnt = Integer.parseInt(timeAnt.substring(3,5));

            timePicker.setCurrentHour(houreAnt);
            timePicker.setCurrentMinute(minuteAnt);

            String am_pm = (houreAnt < 12) ? "AM" : "PM";
            int hourFormat = (houreAnt > 12) ? houreAnt - 12 : houreAnt;
            hora = String.format(Locale.getDefault(), "%02d:%02d %s", hourFormat, minuteAnt, am_pm);
            continuar.setText("Confirmar");
        }else{
            int hourOfDay = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();
            String am_pm = (hourOfDay < 12) ? "AM" : "PM";
            int hourFormat = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
            hora = String.format(Locale.getDefault(), "%02d:%02d %s", hourFormat, minute, am_pm);
        }


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String am_pm = (hourOfDay < 12) ? "AM" : "PM";
                int hour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                selectedTimeText.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, am_pm));
                hora = selectedTimeText.getText().toString();
            }
        });


        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle cDatos = new Bundle();
                datos.put("timePublication", hora);
                cDatos.putSerializable("datos", datos);

                if(getIntent().getBooleanExtra("editar", false)){
                    Intent d = new Intent(SelectTime.this, CreatePublicationDetails.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                    finish();
                }else{
                    Intent d = new Intent(SelectTime.this, SelectStudents.class);
                    d.putExtras(cDatos);
                    startActivity(d);
                }


            }
        });

    }
}