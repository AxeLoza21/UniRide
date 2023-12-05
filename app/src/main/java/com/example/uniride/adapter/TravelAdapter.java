package com.example.uniride.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;

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

        holder.Initiation.setText(Travel.getDireccionPartida() != null ? Travel.getDireccionPartida() : "Desconocido");
        holder.fecha.setText(Travel.getDatePublication() != null ? Travel.getDatePublication() : "Fecha desconocida");
        holder.hora.setText(Travel.getTimePublication() != null ? Travel.getTimePublication() : "Hora desconocida");
        holder.Destination.setText(Travel.getCampusDestination() != null ? Travel.getCampusDestination() : "Destino desconocido");

        if(isMyRequestFragment){
            holder.cMyTravelCreated.setVisibility(View.INVISIBLE);
            holder.cMyRequest.setVisibility(View.VISIBLE);

            fStore.collection("solicitud").document(Travel.getIdSolicitud()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value != null && value.getString("State") != null){
                        String state = value.getString("State");
                        switch(state){
                            case "Aceptado":
                                holder.borde.setBackgroundResource(R.drawable.cardview_completed);
                                holder.estadoSolicitud.setText("Aceptado");
                                holder.estadoSolicitud.setTextColor(Color.parseColor("#58CA5D"));
                                holder.iconSolictud.setImageResource(R.drawable.baseline_check_24);
                                holder.iconSolictud.setColorFilter(Color.parseColor("#58CA5D"));
                                break;
                            case "Rechazado":
                                holder.borde.setBackgroundResource(R.drawable.border_delete);
                                holder.estadoSolicitud.setText("Rechazado");
                                holder.estadoSolicitud.setTextColor(Color.parseColor("#CF0A0A"));
                                holder.iconSolictud.setImageResource(R.drawable.ic_baseline_close_24);
                                holder.iconSolictud.setColorFilter(Color.parseColor("#CF0A0A"));
                                break;
                            case "Pendiente":
                                holder.borde.setBackgroundResource(R.drawable.cardview_process);
                                holder.estadoSolicitud.setText("Pendiente");
                                holder.estadoSolicitud.setTextColor(Color.parseColor("#01B1EA"));
                                holder.iconSolictud.setColorFilter(Color.parseColor("#01B1EA"));
                                break;
                        }
                    }
                }
            });
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
        ImageView iconSolictud;
        LinearLayout borde;
        RelativeLayout cMyRequest, cMyTravelCreated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconSolictud = itemView.findViewById(R.id.iconSolictud);
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

