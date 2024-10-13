package com.example.uniride.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import de.hdodenhof.circleimageview.CircleImageView;

public class Chatfragment extends Fragment {

   // private CircleImageView fotoperfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private ImageButton btnenviar;

    private AdapterMensajes adapter;
    private List<Mensaje> listMensaje;

    private FirebaseFirestore firestore;
    private CollectionReference chatReference;
    private FirebaseAuth auth;
    private String senderId;
    private String recipientId;  // El ID del destinatario, pasado como argumento
    private String chatId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.chat_vista, container, false);

        // Inicializar las vistas
        //fotoperfil = view.findViewById(R.id.fotoperfil);
        nombre = view.findViewById(R.id.nombreusuario);
        rvMensajes = view.findViewById(R.id.rvMensajes);
        txtMensaje = view.findViewById(R.id.txtMensaje);
        btnenviar = view.findViewById(R.id.btnenviar);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        senderId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        recipientId = getArguments() != null ? getArguments().getString("recipientId") : null;

        // Inicializar la lista y el adaptador
        listMensaje = new ArrayList<>();
        adapter = new AdapterMensajes(getContext());
        rvMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMensajes.setAdapter(adapter);

        // Verificar o crear el chat
        checkOrCreateChat(senderId, recipientId);

        // Obtener chatId de los argumentos
        if (getArguments() != null) {
            chatId = getArguments().getString("chatId");
        } else {
            chatId = firestore.collection("chat").document().getId();
        }

        // Referencia al chat en Firestore
        chatReference = firestore.collection("chats").document(chatId).collection("messages");

        // Escuchar cambios en los mensajes del chat (Firestore listener)
        chatReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                // Manejar cambios en el chat (nuevos mensajes)
                for (DocumentChange dc : Objects.requireNonNull(snapshots).getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Mensaje m = dc.getDocument().toObject(Mensaje.class);
                            adapter.addMensaje(m);
                            break;
                        case MODIFIED:
                            // Manejar mensajes modificados si es necesario
                            break;
                        case REMOVED:
                            // Manejar mensajes eliminados si es necesario
                            break;
                    }
                }

                // Desplazar el RecyclerView al último mensaje
                setScrollbar();
            }
        });

        // Enviar mensaje cuando se presiona el botón de enviar
        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = txtMensaje.getText().toString();
                String nombreUsuario = nombre.getText().toString();

                // Validar que el mensaje no esté vacío
                if (!TextUtils.isEmpty(mensaje)) {
                    Mensaje nuevoMensaje = new Mensaje(
                            mensaje,
                            nombreUsuario,
                            "1", // Tipo de mensaje (texto)
                            Timestamp.now(),
                            recipientId,
                            senderId
                    );
                    // Agregar el nuevo mensaje a Firestore
                    chatReference.add(nuevoMensaje);
                    txtMensaje.setText(""); // Limpiar el campo de texto
                }
            }
        });

        return view;
    }

    // Método para verificar o crear un chat entre los usuarios
    private void checkOrCreateChat(String senderId, @Nullable String recipientId) {
        if (recipientId == null) {
            // Es un chat individual del usuario (su propio chat privado)
            firestore.collection("chats")
                    .whereArrayContains("users", senderId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        boolean chatExists = false;
                        for (DocumentSnapshot dc : queryDocumentSnapshots) {
                            List<String> users = (List<String>) dc.get("users");
                            if (users != null && users.size() == 1 && users.contains(senderId)) {
                                chatId = dc.getId();
                                chatExists = true;
                                break;
                            }
                        }

                        if (!chatExists) {
                            createNewChat(senderId, null);
                        }
                    });
        } else {
            // Es un chat 1 a 1
            firestore.collection("chats")
                    .whereArrayContains("users", senderId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        boolean chatExists = false;
                        for (DocumentSnapshot dc : queryDocumentSnapshots) {
                            List<String> users = (List<String>) dc.get("users");
                            if (users != null && users.contains(recipientId)) {
                                chatId = dc.getId();
                                chatExists = true;
                                break;
                            }
                        }

                        if (!chatExists) {
                            createNewChat(senderId, recipientId);
                        }
                    });
        }
    }

    // Crear un nuevo chat con los usuarios involucrados
    private void createNewChat(String senderId, @Nullable String recipientId) {
        List<String> users = new ArrayList<>();
        users.add(senderId);
        if (recipientId != null) {
            users.add(recipientId);  // Agregar el destinatario si es un chat 1 a 1
        }

        chatId = firestore.collection("chats").document().getId();  // Generar un nuevo ID de chat

        firestore.collection("chats")
                .document(chatId)
                .set(new Chat(users))
                .addOnSuccessListener(aVoid -> {
                    // Chat creado exitosamente
                });
    }

    // Método para desplazar el RecyclerView al último mensaje
    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
    }

    // Clase auxiliar para crear un nuevo chat
    private static class Chat {
        List<String> users;

        Chat(List<String> users) {
            this.users = users;
        }

        public List<String> getUsers() {
            return users;
        }

        public void setUsers(List<String> users) {
            this.users = users;
        }

    }
}
