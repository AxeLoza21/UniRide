package com.example.uniride.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.MapClientLocation;
import com.example.uniride.R;
import com.example.uniride.functions.CalculateAge;
import com.example.uniride.model.Publications;
import com.example.uniride.model.Request;
import com.example.uniride.travelDetails2;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class RequestAdapter extends FirestoreRecyclerAdapter<Request, RequestAdapter.ViewHolder> {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RequestAdapter(@NonNull FirestoreRecyclerOptions<Request> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position, @NonNull Request requests) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String idRequest = documentSnapshot.getId();

        fStore.collection("users").document(requests.getIdUser()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot value = task.getResult();
                if (value != null) {
                    String ageUser = new CalculateAge().calcularEdad(value.getString("birthDay"));
                    holder.nameUser.setText(value.getString("username"));
                    holder.ageUser.setText(ageUser);
                    holder.schoolUser.setText(value.getString("school"));
                    if(!value.getString("photo").equals("")){
                        Picasso.get().load(value.getString("photo")).into(holder.imgUser);
                    }
                }else{

                }
            }
        });

        holder.txtEstado.setText(requests.getState().toString());
        switch (requests.getState()){
            case "Aceptado":
                holder.cEstado.setCardBackgroundColor(Color.parseColor("#58CA5D"));
                break;
            case "Rechazado":
                holder.cEstado.setCardBackgroundColor(Color.parseColor("#CF0A0A"));
                break;
            case "Pendiente":
                holder.cEstado.setCardBackgroundColor(Color.parseColor("#01B1EA"));
                break;

        }

        if(requests.getState().equals("Pendiente")){
            holder.cRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), MapClientLocation.class);
                    i.putExtra("idItem", idRequest);
                    view.getContext().startActivity(i);
                }
            });
        }
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.solicitud_element, parent, false);
        return new RequestAdapter.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUser;
        TextView nameUser, ageUser, schoolUser, txtEstado;
        RelativeLayout cRequest;
        CardView cEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.imgUser);
            nameUser = itemView.findViewById(R.id.nameUser);
            ageUser = itemView.findViewById(R.id.ageUser);
            schoolUser = itemView.findViewById(R.id.schoolUser);
            cRequest = itemView.findViewById(R.id.cRequest);
            cEstado = itemView.findViewById(R.id.cEstado);
            txtEstado = itemView.findViewById(R.id.txtEstado);
        }
    }
}
