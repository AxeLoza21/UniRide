package com.example.uniride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.uniride.adapter.CarAdapter;
import com.example.uniride.adapter.TravelAdapter;
import com.example.uniride.model.Car;
import com.example.uniride.model.Travel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyHistoryTravels extends AppCompatActivity {
    RecyclerView mRecycler;
    TravelAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    ImageView btnExit;
    Query query;
    boolean isMyHistoryTravels = true;

    @SuppressLint("MissingInflatedId")
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
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.recyclerHistory);
        //mRecycler.setLayoutManager(new LinearLayoutManager(this));
        //arreglar problema del recyclerview
        mRecycler.setLayoutManager(new MyVehicles.WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        query = mFirestore.collection("publications").whereEqualTo("IdCreator", userId).whereEqualTo("State", "history");

        FirestoreRecyclerOptions<Travel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Travel>().setQuery(query, Travel.class).build();
        mAdapter = new TravelAdapter(firestoreRecyclerOptions, this, false, isMyHistoryTravels);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }
    private void disableButtons() {
        mRecycler.setEnabled(false);//Deshabilitar recycleview
        btnExit.setEnabled(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        enableButtons();

    }

    private void enableButtons() {
        mRecycler.setEnabled(true);//habilitar recycleview
        btnExit.setEnabled(true);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}