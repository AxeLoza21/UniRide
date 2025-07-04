package com.example.uniride.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.R;
import com.example.uniride.chat.Chat;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private List<MessagesList> messagesLists;
    private final Context context;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_adapter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list2 = messagesLists.get(position);
        Log.d("MessagesAdapter", "Nombre: " + list2.getName() + " | Último mensaje: " + list2.getLastMessages());

        // Configurar imagen de perfil usando Picasso
        if (list2.getProfilePic() != null && !list2.getProfilePic().isEmpty()) {
            Picasso.get().load(list2.getProfilePic()).placeholder(R.drawable.foto_2).into(holder.profilePic);
        } else {
            holder.profilePic.setImageResource(R.drawable.foto_2); // Imagen predeterminada
        }

        // Configurar nombre y último mensaje
        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessages());

        // Configurar mensajes no vistos
        if (list2.getUnseenMessages() == 0) {
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595")); // Color gris
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(String.valueOf(list2.getUnseenMessages()));
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.purple_700)); // Resalta el mensaje no visto
        }

        // Configurar evento de clic en el layout raíz
        holder.rootLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, Chat.class);
            intent.putExtra("mobile", list2.getMobile());
            intent.putExtra("name", list2.getName());
            intent.putExtra("profile_pic", list2.getProfilePic());
            intent.putExtra("chat_key", list2.getChatKey());
            context.startActivity(intent);
        });
    }

    // Método para actualizar la lista de datos
    public void updateData(List<MessagesList> messagesLists) {
        this.messagesLists = messagesLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView profilePic;
        private final TextView name;
        private final TextView lastMessage;
        private final TextView unseenMessages;
        private final LinearLayout rootLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
