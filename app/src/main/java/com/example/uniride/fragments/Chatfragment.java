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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
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

    private String chatId;
    private String userId;
    private String recipientId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_vista, container, false);

        fotoperfil = view.findViewById(R.id.fotoperfil);
        nombre = view.findViewById(R.id.nombreusuario);
        rvMensajes = view.findViewById(R.id.rvMensajes);
        txtMensaje = view.findViewById(R.id.txtMensaje);
        btnenviar = view.findViewById(R.id.btnenviar);

        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (getArguments() != null) {
            chatId = getArguments().getString("chatId");
            recipientId = getArguments().getString("recipientId");
        } else {
            chatId = firestore.collection("chat").document().getId();
        }

        chatReference = firestore.collection("chat").document(chatId).collection("messages");

        adapter = new AdapterMensajes(getContext());
        LinearLayoutManager l = new LinearLayoutManager(getContext());
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = txtMensaje.getText().toString();
                String nombreUsuario = nombre.getText().toString();

                if (!mensaje.isEmpty() && !nombreUsuario.isEmpty()) {
                    // Usar el constructor completo con Timestamp y otros parámetros
                    Mensaje nuevoMensaje = new Mensaje(
                            mensaje,
                            nombreUsuario,
                            "1",
                            Timestamp.now(),
                            recipientId,
                            userId
                    );
                    chatReference.add(nuevoMensaje);
                    txtMensaje.setText("");
                }
            }
        });

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

                setScrollbar();
            }
        });

        return view;
    }

    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
    }
}
