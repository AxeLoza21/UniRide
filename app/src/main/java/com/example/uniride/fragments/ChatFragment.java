package com.example.uniride.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uniride.R;
import com.example.uniride.messages.MessagesAdapter;
import com.example.uniride.messages.MessagesList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    private String id; // ID del usuario actual
    private RecyclerView messagesRecyclerView;
    private CircleImageView userProfilePic;
    private MessagesAdapter messagesAdapter;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://uniride-ec144-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        userProfilePic = view.findViewById(R.id.userProfilePic);

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messagesAdapter = new MessagesAdapter(messagesLists, getContext());
        messagesRecyclerView.setAdapter(messagesAdapter);

        // Obtener ID del usuario actual desde FirebaseAuth
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Configurar foto de perfil (opcional, si está disponible)
        setupProfilePic();

        // Cargar lista de chats
        loadChatList();
    }

    private void setupProfilePic() {
        Bundle args = getArguments();
        if (args != null) {
            String profilePic = args.getString("profile_pic", "");
            if (profilePic != null && !profilePic.isEmpty()) {
                Picasso.get()
                        .load(profilePic)
                        .placeholder(R.drawable.foto_2)
                        .into(userProfilePic);
            } else {
                userProfilePic.setImageResource(R.drawable.foto_2);
            }
        }
    }

    private void loadChatList() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Cargando chats...");
        progressDialog.show();

        // Leer usuarios de la base de datos
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String getId = userSnapshot.getKey();

                    // Excluir al usuario actual
                    if (!getId.equals(id)) {
                        String getName = userSnapshot.child("username").getValue(String.class);
                        String getMobile = userSnapshot.child("phone").getValue(String.class);
                        String getProfilePic = userSnapshot.child("profile_pic").getValue(String.class);

                        // Verificar si existe un chat entre el usuario actual y el usuario listado
                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                String chatKey = "";
                                String lastMessage = "";

                                for (DataSnapshot chatNode : chatSnapshot.getChildren()) {
                                    String user1 = chatNode.child("user_1").getValue(String.class);
                                    String user2 = chatNode.child("user_2").getValue(String.class);

                                    if ((user1 != null && user1.equals(id) && user2 != null && user2.equals(getId)) ||
                                            (user2 != null && user2.equals(id) && user1 != null && user1.equals(getId))) {
                                        chatKey = chatNode.getKey();

                                        // Obtener último mensaje
                                        if (chatNode.hasChild("messages")) {
                                            DataSnapshot messagesNode = chatNode.child("messages");
                                            for (DataSnapshot message : messagesNode.getChildren()) {
                                                lastMessage = message.child("msg").getValue(String.class);
                                            }
                                        }
                                        break;
                                    }
                                }

                                // Agregar el usuario a la lista de mensajes
                                messagesLists.add(new MessagesList(getName, getMobile, lastMessage, getProfilePic, 0, chatKey));
                                messagesAdapter.updateData(messagesLists);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Error al cargar los datos de chats.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error al cargar los datos de chats.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
