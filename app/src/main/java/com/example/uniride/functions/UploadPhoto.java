package com.example.uniride.functions;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uniride.MyPerfil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class UploadPhoto {

    Activity activity;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    private Uri image_url;
    private static final int COD_SEL_IMAGE = 300;
    private static final int COD_SEL_STORAGE = 200;

    public UploadPhoto(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    public void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        activity.startActivityForResult(i, COD_SEL_IMAGE);

    }

    public void subirPhoto(Uri image_url) {
        String rute_storage_photo = "photoUserProfile/" + mAuth.getUid();
        storageReference.child(rute_storage_photo).putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url_photo = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", url_photo);
                            fStore.collection("users").document(mAuth.getUid()).update(map);
                            Toast.makeText(activity, "Foto Actualizada Correctamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al cargar la Foto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
