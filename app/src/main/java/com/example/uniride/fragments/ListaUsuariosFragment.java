package com.example.uniride.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.R;
import com.example.uniride.adapter.AdapterUsuarios;
import com.example.uniride.model.Usuarios;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaUsuariosFragment extends Fragment implements AdapterUsuarios.OnUserClickListener {
    private RecyclerView recyclerView;
    private AdapterUsuarios adapterUsuarios;
    private List<Usuarios> usuarios;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_lista_usuarios, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Inicializar la lista de usuarios y el adaptador
        usuarios = new ArrayList<>();
        adapterUsuarios = new AdapterUsuarios(getContext(), usuarios, this);
        recyclerView.setAdapter(adapterUsuarios);

        firestore = FirebaseFirestore.getInstance();

        // Cargar usuarios desde Firestore
        cargarUsuarios();

        return view;
    }

    private void cargarUsuarios() {
        firestore.collection("usuarios").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                usuarios.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Usuarios usuario = document.toObject(Usuarios.class);
                    usuarios.add(usuario);
                }
                adapterUsuarios.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onUserClick(Usuarios usuario) {
        String chatId = firestore.collection("chat").document().getId();  // Puedes generar un nuevo chatId
        Bundle bundle = new Bundle();
        bundle.putString("chatId", chatId);
        bundle.putString("recipientId", usuario.getUserId()); // ID del usuario al que le mandas mensajes

        Chatfragment chatFragment = new Chatfragment();
        chatFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, chatFragment)
                .commit();
    }

}
