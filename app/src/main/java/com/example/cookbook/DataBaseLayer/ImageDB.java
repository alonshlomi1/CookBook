package com.example.cookbook.DataBaseLayer;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageDB {
    private FirebaseFirestore db;
    public ImageDB(FirebaseFirestore db){
        this.db = db;
    }

    public void saveImage(String id, byte[] uri){
        StorageReference storageRef = SingleManager.getInstance().getStorage().getReference().child("images/"+id+".jpg");// new_recipe.getId() +".jpg");
        UploadTask uploadTask = storageRef.putBytes(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    public void getRecipesURI(OnRecipesURLLoadedListener listener, String recipeID){
        StorageReference storageRef = SingleManager.getInstance().getStorage().getReference().child("images/"+ recipeID +".jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                try {
                    listener.onRecipesURLLoaded(new URL(uri.toString()));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("URI-@@@@@@@@@", exception.toString());
            }
        });
    }
}
