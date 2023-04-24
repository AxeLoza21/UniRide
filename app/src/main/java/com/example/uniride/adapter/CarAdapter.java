package com.example.uniride.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.CarDetails;
import com.example.uniride.MyVehicles;
import com.example.uniride.R;
import com.example.uniride.model.Car;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CarAdapter extends FirestoreRecyclerAdapter <Car, CarAdapter.ViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Activity activity;
    private boolean isEditButtonClicked = false;
    private boolean isDeleteButtonClicked = false;
    private ImageView btnExit, btnAddCar;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CarAdapter(@NonNull FirestoreRecyclerOptions<Car> options, Activity activity) {
        super(options);
        this.activity = activity;
        btnExit = activity.findViewById(R.id.btnexit);
        btnAddCar = activity.findViewById(R.id.btn_add);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Car Car) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.make.setText(Car.getMake());
        holder.model.setText(Car.getModel());
        holder.color.setText(Car.getColor());
        holder.year.setText(Car.getYear());

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEditButtonClicked) {
                    return; // do nothing if edit button is already clicked
                }
                isEditButtonClicked = true;
                holder.btn_delete.setEnabled(false);
                btnExit.setEnabled(false);
                btnAddCar.setEnabled(false);


                // Deshabilitar los botones btnExit y btnAddCar
                btnExit.setEnabled(false);
                btnAddCar.setEnabled(false);

                //          SEND DATA ACTIVITY
                Intent i = new Intent(activity, CarDetails.class);
                i.putExtra("id_car", id);
                activity.startActivity(i);
                btnExit.setEnabled(true);
                btnAddCar.setEnabled(true);
                isEditButtonClicked = false;
            }
        });

        // set click listener on delete button
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDeleteButtonClicked) {
                    return; // do nothing if delete button is already clicked
                }

                isDeleteButtonClicked = true;
                holder.btn_edit.setEnabled(false);

                // Deshabilitar los botones btnExit y btnAddCar
                btnExit.setEnabled(false);
                btnAddCar.setEnabled(false);
                deleteCar(id); // delete the selected car
                btnExit.setEnabled(true);
                btnAddCar.setEnabled(true);
            }
        });
    }

    private void deleteCar(String id) {
        db.collection("users").document(mAuth.getUid()).collection("cars").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Vehículo eliminado exitosamente ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, activity.getClass());
                activity.startActivity(intent);
                activity.finish();
                activity.overridePendingTransition(0, 0);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar el vehículo", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_car_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView make, model, color, year, btn_edit;
        ImageView btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            make = itemView.findViewById(R.id.marca);
            model = itemView.findViewById(R.id.modelo);
            color = itemView.findViewById(R.id.color);
            year = itemView.findViewById(R.id.año);
            btn_delete = itemView.findViewById(R.id.btn_eliminars);
            btn_edit = itemView.findViewById(R.id.btn_editar);
        }
    }
}
