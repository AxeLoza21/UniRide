package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.uniride.adapter.TravelAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyHistoryTravels extends AppCompatActivity {
    RecyclerView mRecycler;
    TravelAdapter mAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ImageView btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history_travels);
        mAuth = FirebaseAuth.getInstance();
        btnExit = (ImageView)findViewById(R.id.btnexit);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                onBackPressed();
            }
        });
        db = FirebaseFirestore.getInstance();
    }
    private void disableButtons() {
        mRecycler.setEnabled(false);//Deshabilitar recycleview
        btnExit.setEnabled(false);
    }
}