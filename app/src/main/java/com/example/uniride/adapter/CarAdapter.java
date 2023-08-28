package com.example.uniride.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.CarDetails;
import com.example.uniride.MyVehicles;
import com.example.uniride.R;
import com.example.uniride.components.DialogElement;
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
        final String idDocumento = documentSnapshot.getId();

        holder.make.setText(Car.getMake());
        holder.model.setText(Car.getModel());
        holder.color.setText(Car.getColor());
        holder.placas.setText(Car.getPlateNumber());
        holder.cardColor.setCardBackgroundColor(Color.parseColor(getColor(Car.getColor())));
        holder.year.setText(Car.getYear());
        if(Car.isPredeterminado()){
            holder.predeterminado.setVisibility(View.VISIBLE);
            holder.btnPredeterminado.setEnabled(false);
            holder.btnPredeterminado.setColorFilter(Color.parseColor("#DADADA"));
        }

        holder.btnPredeterminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogElement(activity).showDialogConfirmPredeterminado(idDocumento, mAuth.getUid());
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtons();
                holder.btn_delete.setEnabled(false);
                Intent i = new Intent(activity, CarDetails.class);
                i.putExtra("id_car", idDocumento);
                activity.startActivity(i);
            }
        });

        // set click listener on delete button
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableButtons();
                holder.btn_edit.setEnabled(false);
                deleteCar(idDocumento); // delete the selected car
                enableButtons();
            }
        });
    }

    private void disableButtons() {
        btnAddCar.setEnabled(false);
        btnExit.setEnabled(false);
    }
    private void enableButtons() {

        btnAddCar.setEnabled(true);
        btnExit.setEnabled(true);
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
        TextView make, model, color, year, placas, predeterminado;
        ImageView btn_delete, btn_edit, btnPredeterminado;
        CardView cardColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            make = itemView.findViewById(R.id.marca);
            model = itemView.findViewById(R.id.modelo);
            placas = itemView.findViewById(R.id.placas);
            color = itemView.findViewById(R.id.color);
            cardColor = itemView.findViewById(R.id.cardColor);
            predeterminado = itemView.findViewById(R.id.txtPredeterminado);
            year = itemView.findViewById(R.id.año);
            btnPredeterminado = itemView.findViewById(R.id.fijarPredeterminado);
            btn_delete = itemView.findViewById(R.id.btn_eliminars);
            btn_edit = itemView.findViewById(R.id.btn_editar);
        }
    }

    public String getColor(String nombreColor){
        switch(nombreColor){
            case "Rojo":
                return "#E30D00";
            case "Verde":
                return "#03BD06";
            case "Azul":
                return "#05B0EC";
            case "Blanco":
                return "#FFFFFF";
            case "Negro":
                return "#000000";
            case "Plateado":
                return "#DCD5D5";
            case "Amarillo":
                return "#EAD105";
            case "Rosa":
                return "#D905EA";
            case "Morado":
                return "#9A05EA";
            case "Gris":
                return "#545454";
            case "Café":
                return "#764310";
        }
        return "#FFFFFF";
    }
}
