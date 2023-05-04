package com.example.uniride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterLocation extends RecyclerView.Adapter<ListAdapterLocation.ViewHolder> implements Filterable {
    private List<locationElement> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapterLocation.OnItemClickListener listener;
    private List<locationElement> filteredElements;

    //-----Metodo Constructor-----
    public ListAdapterLocation(List<locationElement> itemList, Context context, ListAdapterLocation.OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
        this.filteredElements = itemList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                List<locationElement> filteredList = new ArrayList<>();

                if (query.isEmpty()) {
                    filteredList.addAll(mData);
                } else {
                    for (locationElement element : mData) {
                        if (element.getnCampus().toLowerCase().contains(query) ||
                                element.getnEscuela().toLowerCase().contains(query) ||
                                element.getCategorias().toLowerCase().contains(query)) {
                            filteredList.add(element);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredElements = (List<locationElement>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(locationElement item);
    }

    @Override
    public int getItemCount() {
        return filteredElements.size();
    }

    @Override
    public ListAdapterLocation.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.location_element, null);
        return new ListAdapterLocation.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterLocation.ViewHolder holder, final int position) {
        holder.bindData(filteredElements.get(position));
    }
    public void setItems(List<locationElement> items) { mData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nEscuela, nCampus;

        ViewHolder(View itemView) {
            super(itemView);
            nEscuela = itemView.findViewById(R.id.card_L_escuela);
            nCampus = itemView.findViewById(R.id.card_L_campus);
        }

        void bindData(final locationElement item) {
            nEscuela.setText(item.getnEscuela());
            nCampus.setText(item.getnCampus());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
