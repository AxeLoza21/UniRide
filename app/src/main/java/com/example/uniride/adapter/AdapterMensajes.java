package com.example.uniride.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.HolderMensaje;
import com.example.uniride.Mensaje;
import com.example.uniride.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {
    //crear lista de mensajes
    private List<Mensaje> listmensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {

        this.c = c;
    }
    //agregar mensajes
    public void addMensaje(Mensaje m){
        listmensaje.add(m);
        notifyItemInserted(listmensaje.size());
    }
    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes,parent,false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        holder.getNombre().setText(listmensaje.get(position).getNombre());
        holder.getMensaje().setText(listmensaje.get(position).getMensaje());
        holder.getHora().setText(listmensaje.get(position).getHora());

    }

    @Override
    public int getItemCount() {
        return listmensaje.size();
    }
}
