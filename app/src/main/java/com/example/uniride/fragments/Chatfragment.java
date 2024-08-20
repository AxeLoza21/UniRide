package com.example.uniride.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
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

import com.example.uniride.Mensaje;
import com.example.uniride.R;
import com.example.uniride.adapter.AdapterMensajes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatfragment extends Fragment {
    private CircleImageView fotoperfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private ImageButton btnenviar;

    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;

    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private CollectionReference chatReference;
    private static final int PHOTO_SEND =1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_vista, container, false);

        fotoperfil = (CircleImageView) view.findViewById(R.id.fotoperfil);
        nombre = (TextView) view.findViewById(R.id.nombreusuario);
        rvMensajes = (RecyclerView) view.findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) view.findViewById(R.id.txtMensaje);
        btnenviar = (ImageButton) view.findViewById(R.id.btnenviar);
        btnEnviarFoto = (ImageButton) view.findViewById(R.id.btnEnviarfoto);

        // Inicializar Firestore
        firestore = FirebaseFirestore.getInstance();
        chatReference = firestore.collection("chat"); // Sala de chat (nombre)
        storage = FirebaseStorage.getInstance();

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
                    Mensaje nuevoMensaje = new Mensaje(mensaje, nombreUsuario, "", "1", "00:00");
                    chatReference.add(nuevoMensaje);
                    txtMensaje.setText(""); // Limpiar el campo de texto después de enviar
                }
            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtener galeria
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona fotografia"),PHOTO_SEND);
            }
        });

        // Escuchar los cambios en la base de datos
        chatReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Manejar errores
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

    // Método para desplazarse a la última posición
    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount() - 1);
    }

    //metodo nos devuelva un resultado
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");//carpeta para imagenees
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener((Executor) this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Uri u = taskSnapshot.getDownloadUrl();
                    Mensaje m = new Mensaje("Usuario te ha enviado una foto",nombre.getText().toString(),"","2","00:00");
                    databaseReference.push().setValue(m);

                }
            });
        }
    }
}
