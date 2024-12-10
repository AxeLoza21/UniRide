package com.example.uniride.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uniride.MemoryData;
import com.example.uniride.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://uniride-ec144-default-rtdb.firebaseio.com/");
    private final List<ChatList> chatLists = new ArrayList<>();
    private String chatKey;
    private String getUserMobile;
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageView backBtn = findViewById(R.id.back_Btn);
        final TextView nameTv = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.mensajeEditText);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final ImageView sendBtn = findViewById(R.id.sendBtn);

        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);

        // Obtener datos de la actividad anterior
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");

        getUserMobile = MemoryData.getData(Chat.this);

        nameTv.setText(getName);
        if (getProfilePic != null && !getProfilePic.isEmpty()) {
            Picasso.get().load(getProfilePic).placeholder(R.drawable.foto_2).into(profilePic);
        } else {
            profilePic.setImageResource(R.drawable.foto_2);
        }

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));

        chatAdapter = new ChatAdapter(chatLists, Chat.this);
        chattingRecyclerView.setAdapter(chatAdapter);

        // Configurar listener para leer mensajes
        databaseReference.child("chat").child(chatKey).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatLists.clear();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    if (messageSnapshot.hasChild("msg") && messageSnapshot.hasChild("mobile")) {
                        String timestamp = messageSnapshot.getKey();
                        String mobile = messageSnapshot.child("mobile").getValue(String.class);
                        String message = messageSnapshot.child("msg").getValue(String.class);

                        // Formatear fecha y hora
                        long timestampLong = Long.parseLong(timestamp);
                        Date date = new Date(timestampLong);
                        String formattedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
                        String formattedTime = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(date);

                        // Agregar a la lista
                        chatLists.add(new ChatList(mobile, getName, message, formattedDate, formattedTime));
                    }
                }
                chatAdapter.updateChatList(chatLists);
                chattingRecyclerView.scrollToPosition(chatLists.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Error al cargar los mensajes.", Toast.LENGTH_SHORT).show();
            }
        });

        // Enviar mensajes
        sendBtn.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();

            if (!message.isEmpty()) {
                String timestamp = String.valueOf(System.currentTimeMillis());

                // Guardar datos de la conversación (si no existen)
                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);

                // Guardar mensaje
                DatabaseReference messageRef = databaseReference.child("chat").child(chatKey).child("messages").child(timestamp);
                messageRef.child("msg").setValue(message);
                messageRef.child("mobile").setValue(getUserMobile);

                // Limpiar el campo de texto
                messageEditText.setText("");
            } else {
                Toast.makeText(Chat.this, "El mensaje no puede estar vacío.", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón de retroceso
        backBtn.setOnClickListener(v -> finish());
    }
}
