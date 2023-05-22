package com.example.uniride.components;

import android.app.Activity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarElement {

    Activity activity;


    public SnackBarElement(Activity activity) {
        this.activity = activity;

    }

    public void showSnackBar(int color, String texto){
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), texto, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(color); // Establecer el color de fondo
        snackbar.show();
    }
}
