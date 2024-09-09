package com.example.uniride.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.model.HolderMensaje;
import com.example.uniride.model.Mensaje;
import com.example.uniride.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {
    private List<Mensaje> listmensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje m) {
        listmensaje.add(m);
        notifyItemInserted(listmensaje.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        Mensaje mensaje = listmensaje.get(position);

        holder.getNombre().setText(mensaje.getNombre());
        holder.getMensaje().setText(mensaje.getMensaje());

        // Convertir el Timestamp a una cadena legible para mostrar la hora
        Timestamp timestamp = mensaje.getHora();
        if (timestamp != null) {
            Date date = timestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String horaFormateada = sdf.format(date);
            holder.getHora().setText(horaFormateada);
        } else {
            holder.getHora().setText("00:00"); // Valor por defecto si la hora es null
        }
    }

    @Override
    public int getItemCount() {
        return listmensaje.size();
    }
}
