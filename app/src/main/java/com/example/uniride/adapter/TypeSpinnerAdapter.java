package com.example.uniride.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.uniride.R;

import java.util.ArrayList;

public class TypeSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> tipos;

    public TypeSpinnerAdapter(Context context, ArrayList<String> tipos) {
        super(context, R.layout.spinner_item, tipos);
        this.context = context;
        this.tipos = tipos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(tipos.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}