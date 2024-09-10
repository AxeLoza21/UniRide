package com.example.uniride.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.R;
import com.example.uniride.model.Usuarios;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.UsuarioViewHolder> {
    private List<Usuarios> usuarios; // Lista de usuarios a mostrar
    private Context context; // Contexto de la actividad o fragmento
    private OnUserClickListener listener; // Interfaz para manejar clics en los usuarios

    // Constructor para inicializar los valores del adaptador
    public AdapterUsuarios(Context context, List<Usuarios> usuarios, OnUserClickListener listener) {
        this.context = context;
        this.usuarios = usuarios;
        this.listener = listener;
    }

    // Inflar el layout vista_usuarios para cada ítem del RecyclerView
    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vista_usuarios, parent, false);
        return new UsuarioViewHolder(view);
    }

    // Asignar los valores correspondientes a cada ítem del RecyclerView
    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuarios usuario = usuarios.get(position); // Obtener el usuario en la posición actual

        holder.nombreUsuario.setText(usuario.getNombre()); // Asignar el nombre del usuario
        holder.estadoUsuario.setText(usuario.isOnline() ? "Online" : "Offline"); // Mostrar el estado del usuario (Online/Offline)

        // Puedes usar Glide o Picasso para cargar una imagen desde una URL si tienes una imagen de perfil
        // Glide.with(context).load(usuario.getFotoPerfilUrl()).into(holder.fotoPerfil);

        // Manejar el clic en cada ítem de la lista
        holder.itemView.setOnClickListener(v -> listener.onUserClick(usuario));
    }

    // Devolver la cantidad de usuarios en la lista
    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    // ViewHolder para almacenar las vistas de cada ítem
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        CircleImageView fotoPerfil; // Imagen de perfil del usuario
        TextView nombreUsuario; // Nombre del usuario
        TextView estadoUsuario; // Estado del usuario (Online/Offline)

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoPerfil = itemView.findViewById(R.id.fotoPerfil); // Referencia a la vista de imagen de perfil
            nombreUsuario = itemView.findViewById(R.id.nombreUsuario); // Referencia al nombre del usuario
            estadoUsuario = itemView.findViewById(R.id.estadoUsuario); // Referencia al estado del usuario
        }
    }

    // Interfaz para manejar los clics en los usuarios
    public interface OnUserClickListener {
        void onUserClick(Usuarios usuario); // Método que se llama cuando un usuario es clicado
    }
}
