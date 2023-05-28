package com.example.uniride.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.CarDetails;
import com.example.uniride.R;
import com.example.uniride.model.Car;
import com.example.uniride.model.Travel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.BreakIterator;

public class TravelAdapter extends FirestoreRecyclerAdapter<Travel, TravelAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Activity activity;
    boolean isCompletedFragment;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TravelAdapter(@NonNull FirestoreRecyclerOptions<Travel> options, Activity activity, boolean isCompletedFragment) {
        super(options);
        this.activity = activity;
        this.isCompletedFragment = isCompletedFragment;


    }

    @SuppressLint("ResourceType")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Travel Travel) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.Initiation.setText(Travel.getCampusDestination());
        holder.fecha.setText(Travel.getDatePublication());
        holder.Destination.setText(Travel.getLatInitation());
        if (isCompletedFragment) {
            // Cambiar el fondo del CardView para el CompletedFragment
            holder.borde.setBackgroundResource(R.drawable.cardview_completed);
        }



        holder.visualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, CarDetails.class);
                i.putExtra("id_car", id);
                activity.startActivity(i);
            }
        });


    }




    @NonNull
    @Override
    public TravelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mytravelsrecycler, parent, false);
        return new TravelAdapter.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Initiation, Destination, fecha;
        ImageView visualizar;
        LinearLayout borde;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Initiation = itemView.findViewById(R.id.txtinitiation);
            Destination = itemView.findViewById(R.id.txtDestination);
            fecha = itemView.findViewById(R.id.txtCalendar);
            visualizar = itemView.findViewById(R.id.arrow);
            borde = itemView.findViewById(R.id.primero);

        }
    }
}

