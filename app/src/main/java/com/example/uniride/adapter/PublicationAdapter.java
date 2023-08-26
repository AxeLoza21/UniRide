package com.example.uniride.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.ListAdapterLocation;
import com.example.uniride.R;
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

import java.util.List;

public class PublicationAdapter extends FirestoreRecyclerAdapter<Publications, PublicationAdapter.ViewHolder>{
    FirebaseFirestore fStore;
    Activity activity;

    public interface OnItemClickListener {
        void onItemClick(Publications item);
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PublicationAdapter(@NonNull FirestoreRecyclerOptions<Publications> options, Activity activity) {
        super(options);
        fStore = FirebaseFirestore.getInstance();
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull PublicationAdapter.ViewHolder holder, int position, @NonNull Publications publication) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String idItem = documentSnapshot.getId();

        fStore.collection("users").document(publication.getIdCreator()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    holder.userName = value.getString("username");
                    holder.nameUser.setText(holder.userName);
                    if(!value.getString("photo").equals("")){
                        Picasso.get().load(value.getString("photo")).into(holder.imgUser);
                    }
                }else{
                    //El Documento no existe
                }
            }
        });

        holder.timePublication.setText(publication.getTimePublication());
        holder.travelSeating.setText(publication.getTravelSeating());
        holder.direccion.setText(new SplitDirection().getDirection(publication.getDireccionPartida()));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, travelDetails2.class);
                i.putExtra("idItem", idItem);
                i.putExtra("originActivity", "PublicationAdapter");
                activity.startActivity(i);

            }
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
        TextView nameUser, direccion, timePublication, travelSeating;
        CardView item;

        String userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.imgUser);
            nameUser = itemView.findViewById(R.id.cardNameUser);
            direccion = itemView.findViewById(R.id.cardMiniDireccion);
            timePublication = itemView.findViewById(R.id.cardSalida);
            travelSeating = itemView.findViewById(R.id.cardAsientos);
            item = itemView.findViewById(R.id.cardItem);


        }
    }

}
/*public class  PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.ViewHolder> {
    private List<Publications> mData;
    private LayoutInflater mInflater;
    private Context context;
    final PublicationAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Publications item);
    }

    public PublicationAdapter(List<Publications> itemList, Context context, PublicationAdapter.OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {return mData.size(); }

    @Override
    public PublicationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.raite_element, null);
        return new PublicationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PublicationAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Publications> items) { mData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView nameUser, direccion, hSalida, asientos;

        ViewHolder(View itemView) {
            super(itemView);
            nameUser = itemView.findViewById(R.id.cardNameUser);
            direccion = itemView.findViewById(R.id.cardMiniDireccion);
            hSalida = itemView.findViewById(R.id.cardSalida);
            asientos = itemView.findViewById(R.id.cardAsientos);
        }

        void bindData(final Publications item) {
            nameUser.setText(item.getNameUser());
            direccion.setText(item.getDireccion());
            hSalida.setText(item.getSalida());
            asientos.setText(item.getAsientos());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}*/
