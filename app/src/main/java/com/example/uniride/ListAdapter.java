package com.example.uniride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<raiteElement> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(raiteElement item);
    }

    public ListAdapter(List<raiteElement> itemList, Context context, ListAdapter.OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {return mData.size(); }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.raite_element, null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<raiteElement> items) { mData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView nameUser, direccion, hSalida, asientos;

        ViewHolder(View itemView) {
            super(itemView);
            nameUser = itemView.findViewById(R.id.cardNameUser);
            direccion = itemView.findViewById(R.id.cardMiniDireccion);
            hSalida = itemView.findViewById(R.id.cardSalida);
            asientos = itemView.findViewById(R.id.cardAsientos);
        }

        void bindData(final raiteElement item) {
            nameUser.setText(item.getNameUser());
            direccion.setText(item.getDireccion());
            hSalida.setText(item.getSalida());
            asientos.setText(item.getAsientos());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
