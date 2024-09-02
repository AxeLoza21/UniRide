package com.example.uniride.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.R;
import com.example.uniride.adapter.AdapterMensajes;
import com.example.uniride.model.Mensaje;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatfragment extends Fragment {
    private CircleImageView fotoperfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private ImageButton btnenviar;

    private AdapterMensajes adapter;

    private FirebaseFirestore firestore;
    private CollectionReference chatReference;

    // Aquí se recibe el ID del chat entre dos usuarios
    private String chatId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_vista, container, false);

        fotoperfil = (CircleImageView) view.findViewById(R.id.fotoperfil);
        nombre = (TextView) view.findViewById(R.id.nombreusuario);
        rvMensajes = (RecyclerView) view.findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) view.findViewById(R.id.txtMensaje);
        btnenviar = (ImageButton) view.findViewById(R.id.btnenviar);

        // Inicializar Firestore
        firestore = FirebaseFirestore.getInstance();

        // Obtener el ID del chat desde los argumentos o crear uno nuevo
        if (getArguments() != null) {
            chatId = getArguments().getString("chatId");
        } else {
            chatId = firestore.collection("chat").document().getId(); // Crear un nuevo chat si no se proporciona uno
        }

        // Referencia a la subcolección `messages` dentro del chat específico
        chatReference = firestore.collection("chat").document(chatId).collection("messages");

        // Configurar el adaptador
        adapter = new AdapterMensajes(getContext());
        LinearLayoutManager l = new LinearLayoutManager(getContext());
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        // Agregar mensaje nuevo
        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = txtMensaje.getText().toString();
                String nombreUsuario = nombre.getText().toString();

                if (!mensaje.isEmpty() && !nombreUsuario.isEmpty()) {
                    // Enviar el mensaje al chat específico
                    Mensaje nuevoMensaje = new Mensaje(mensaje, nombreUsuario, "1", "00:00");
                    chatReference.add(nuevoMensaje);
                    txtMensaje.setText(""); // Limpiar el campo de texto después de enviar
                }
            }
        });

        // Escuchar los cambios en la base de datos para este chat específico
        chatReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for (DocumentChange dc : Objects.requireNonNull(snapshots).getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Mensaje m = dc.getDocument().toObject(Mensaje.class);
                            adapter.addMensaje(m);
                            break;
                        case MODIFIED:
                            // Manejar modificaciones si es necesario
                            break;
                        case REMOVED:
                            // Manejar eliminaciones si es necesario
                            break;
                    }
                }

                // Desplazarse a la última posición
                setScrollbar();
            }
        });

        return view;
    }

    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
    }
}

