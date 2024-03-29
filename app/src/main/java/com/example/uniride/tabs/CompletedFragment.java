package com.example.uniride.tabs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.uniride.R;
import com.example.uniride.adapter.TravelAdapter;
import com.example.uniride.model.Travel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public class CompletedFragment extends Fragment {
    LinearLayout cTexto;
    RecyclerView mRecycler;
    TravelAdapter mAdapter;
    FirebaseFirestore mFirestore;
    Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed, container, false);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cTexto = view.findViewById(R.id.cTexto);
        mRecycler = view.findViewById(R.id.recyclerCompleted);

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler.setLayoutManager(new CompletedFragment.WrapContentLinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false));
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //--------Obtener la cantidad de elementos que va haber en el RecyclerView-------
        mFirestore.collection("publications").whereEqualTo("IdCreator", userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    if (value.size() > 0){
                        cTexto.setVisibility(View.INVISIBLE);
                    }else{
                        cTexto.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        //---------------------------------------------------------------------------------

        query = mFirestore.collection("publications").whereEqualTo("IdCreator", userId).whereEqualTo("State", "Activo");
        FirestoreRecyclerOptions<Travel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Travel>().setQuery(query, Travel.class).build();

        mAdapter = new TravelAdapter(firestoreRecyclerOptions, this.getActivity(), false, true);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);



    }
    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
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
}