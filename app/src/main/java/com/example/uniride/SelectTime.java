package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SelectTime extends AppCompatActivity {
    Button continuar;
    private TextView selectedTimeText;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        selectedTimeText = findViewById(R.id.selected_time_text);
        timePicker = findViewById(R.id.time_picker);
        continuar = (Button) findViewById(R.id.btnContinue);

        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String am_pm = (hourOfDay < 12) ? "AM" : "PM";
                int hour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                selectedTimeText.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, am_pm));
                Toast.makeText(SelectTime.this, selectedTimeText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectTime.this, SelectStudents.class);
                startActivity(i);
            }
        });

    }
}