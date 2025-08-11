package com.example.uniride.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.R;
import com.example.uniride.functions.CalculateAge;
import com.example.uniride.functions.SplitDirection;
import com.example.uniride.model.Publications;
import com.example.uniride.travelDetails2;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class PublicationAdapter extends FirestoreRecyclerAdapter<Publications, PublicationAdapter.ViewHolder> {
    FirebaseFirestore fStore;
    Activity activity;

    public PublicationAdapter(@NonNull FirestoreRecyclerOptions<Publications> options, Activity activity) {
        super(options);
        fStore = FirebaseFirestore.getInstance();
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull PublicationAdapter.ViewHolder holder, int position, @NonNull Publications publication) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String idItem = documentSnapshot.getId();

        // Cargar datos del creador del viaje
        fStore.collection("users").document(publication.getIdCreator())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && value.exists()) {
                            String userName = value.getString("username") != null ? value.getString("username") : "N/A";
                            String birthDay = value.getString("birthDay") != null ? value.getString("birthDay") : "01-01-2000";
                            String userAge = new CalculateAge().calcularEdad(birthDay);
                            holder.nameUser.setText(userName);
                            holder.ageUser.setText(userAge);

                            if (value.getString("photo") != null && !value.getString("photo").isEmpty()) {
                                Picasso.get().load(value.getString("photo")).into(holder.imgUser);
                            } else {
                                holder.imgUser.setImageResource(R.drawable.foto_2);
                            }
                        } else {
                            holder.nameUser.setText("Usuario desconocido");
                            holder.ageUser.setText("--");
                            holder.imgUser.setImageResource(R.drawable.foto_2);
                        }
                    }
                });

        // Datos del viaje
        holder.timePublication.setText(publication.getTimePublication());
        holder.travelSeating.setText(publication.getTravelSeating());
        holder.direccion.setText(new SplitDirection().getDirection(publication.getDireccionPartida()));

        // Mostrar rating si existe
        if (publication.getRating() != null) {
            holder.ratingBar.setRating(publication.getRating());
        } else {
            holder.ratingBar.setRating(0);
        }

        // Guardar rating cuando el usuario lo cambie
        holder.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                fStore.collection("publications")
                        .document(idItem)
                        .update("rating", rating);
            }
        });

        // Click para abrir detalles
        holder.item.setOnClickListener(v -> {
            Intent i = new Intent(activity, travelDetails2.class);
            i.putExtra("idItem", idItem);
            i.putExtra("originActivity", "HomeFragment");
            activity.startActivity(i);
        });
    }

    @NonNull
    @Override
    public PublicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raite_element, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView nameUser, ageUser, direccion, timePublication, travelSeating;
        RatingBar ratingBar;
        CardView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            nameUser = itemView.findViewById(R.id.cardNameUser);
            ageUser = itemView.findViewById(R.id.cardAgeUser);
            direccion = itemView.findViewById(R.id.cardMiniDireccion);
            timePublication = itemView.findViewById(R.id.cardSalida);
            travelSeating = itemView.findViewById(R.id.cardAsientos);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            item = itemView.findViewById(R.id.cardItem);
        }
    }
}
