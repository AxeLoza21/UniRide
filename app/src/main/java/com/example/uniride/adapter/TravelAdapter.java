package com.example.uniride.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.MyTravelsCreated;
import com.example.uniride.R;
import com.example.uniride.model.Travel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class TravelAdapter extends FirestoreRecyclerAdapter<Travel, TravelAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    Activity activity;
    boolean isMyRequestFragment, isMyTravelCreatedFragment;



    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TravelAdapter(@NonNull FirestoreRecyclerOptions<Travel> options, Activity activity, boolean isMyRequestFragment , boolean isMyTravelCreatedFragment) {
        super(options);
        this.activity = activity;
        this.isMyRequestFragment = isMyRequestFragment;
        this.isMyTravelCreatedFragment = isMyTravelCreatedFragment;



    }

    @SuppressLint("ResourceType")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Travel Travel) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.Initiation.setText(Travel.getDireccionPartida());
        holder.fecha.setText(Travel.getDatePublication());
        holder.hora.setText(Travel.getTimePublication());
        holder.Destination.setText(Travel.getCampusDestination());

        if(isMyRequestFragment){
            holder.cMyTravelCreated.setVisibility(View.INVISIBLE);
            holder.cMyRequest.setVisibility(View.VISIBLE);
            holder.borde.setBackgroundResource(R.drawable.cardview_process);
        }else if(isMyTravelCreatedFragment){
            holder.cMyTravelCreated.setVisibility(View.VISIBLE);
            holder.cMyRequest.setVisibility(View.INVISIBLE);
            holder.borde.setBackgroundResource(R.drawable.cardview_border);

            fStore.collection("solicitud").whereEqualTo("IdPublication", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    holder.numSolicitudes.setText(task.getResult().getDocuments().size()+"");
                }
            });
        }
        /*if (isCompletedFragment) {
            // Cambiar el fondo del CardView para el CompletedFragment
            holder.borde.setBackgroundResource(R.drawable.cardview_border);
        } else if (isMyHistoryTravels) {
            holder.borde.setBackgroundResource(R.drawable.cardview_history);
        }*/

        holder.borde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMyTravelCreatedFragment){
                    Intent e = new Intent(activity, MyTravelsCreated.class);
                    e.putExtra("idItem", id);
                    e.putExtra("state", Travel.getState());
                    activity.startActivity(e);
                }
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
        TextView Initiation, Destination, fecha, hora, estadoSolicitud, numSolicitudes;
        LinearLayout borde;
        RelativeLayout cMyRequest, cMyTravelCreated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Initiation = itemView.findViewById(R.id.txtinitiation);
            Destination = itemView.findViewById(R.id.txtDestination);
            fecha = itemView.findViewById(R.id.txtCalendar);
            hora = itemView.findViewById(R.id.txtHour);
            borde = itemView.findViewById(R.id.primero);
            cMyRequest = itemView.findViewById(R.id.cMyRequest);
            cMyTravelCreated = itemView.findViewById(R.id.cMyTravelCreated);
            estadoSolicitud = itemView.findViewById(R.id.estadoSolicitud);
            numSolicitudes = itemView.findViewById(R.id.numSolicitudes);

        }
    }
}

