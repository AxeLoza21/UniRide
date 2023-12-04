package com.example.uniride.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.uniride.R;
import com.example.uniride.model.SchoolsLocation;

import java.util.ArrayList;
import java.util.List;

public class SchoolsLocationAdapter extends RecyclerView.Adapter<SchoolsLocationAdapter.ViewHolder> implements Filterable {
    private List<SchoolsLocation> mData;
    private LayoutInflater mInflater;
    private Context context;
    final SchoolsLocationAdapter.OnItemClickListener listener;
    private List<SchoolsLocation> filteredElements;

    //-----Metodo Constructor-----
    public SchoolsLocationAdapter(List<SchoolsLocation> itemList, Context context, SchoolsLocationAdapter.OnItemClickListener listener) {
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
                List<SchoolsLocation> filteredList = new ArrayList<>();

                if (query.isEmpty()) {
                    filteredList.addAll(mData);
                } else {
                    for (SchoolsLocation element : mData) {
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
                filteredElements = (List<SchoolsLocation>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(SchoolsLocation item);
    }

    @Override
    public int getItemCount() {
        return filteredElements.size();
    }

    @Override
    public SchoolsLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.location_element, null);
        return new SchoolsLocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SchoolsLocationAdapter.ViewHolder holder, final int position) {
        holder.bindData(filteredElements.get(position));
    }
    public void setItems(List<SchoolsLocation> items) { mData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nEscuela, nCampus;

        ViewHolder(View itemView) {
            super(itemView);
            nEscuela = itemView.findViewById(R.id.card_L_escuela);
            nCampus = itemView.findViewById(R.id.card_L_campus);
        }

        void bindData(final SchoolsLocation item) {
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
