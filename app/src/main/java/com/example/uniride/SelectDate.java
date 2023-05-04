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

import java.util.Calendar;
import java.util.Locale;

public class SelectDate extends AppCompatActivity {
    private TextView selectedDateText;
    private CalendarView datePicker;
    Button continuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        selectedDateText = findViewById(R.id.selected_date_text);
        datePicker = findViewById(R.id.date_picker);
        continuar = (Button) findViewById(R.id.btnContinue);

        // Set the minimum date to today's date
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());

        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDateText.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year));
            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SelectDate.this, SelectTime.class);
                startActivity(i);
            }
        });
    }
}