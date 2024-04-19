package com.example.cookbook.DataBaseLayer;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.OnUserLoadedListener;
import com.example.cookbook.Interfaces.OnUserSavedListener;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class UserDB {
    private FirebaseFirestore db;


    public UserDB(FirebaseFirestore db){
        this.db = db;
    }
    public void getUser(OnUserLoadedListener listener, String email){
        ArrayList<Recipe> recipeList = new ArrayList<>();

        db.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                // Pass the fetched user object to the listener
                                listener.onUserLoaded(user);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            // Invoke the listener with the error
                            listener.onUserLoadFailed(task.getException());
                        }
                    }
                });
    }

    public void saveUser(User user, OnUserSavedListener listener){
        db.collection("Users").document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                        // If user document added successfully, invoke the listener with success
                        listener.onUserSaved(true, user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        // If adding user document failed, invoke the listener with failure
                        listener.onUserSaved(false, null);
                    }
                });
    }

    public void saveNewUser(User user, OnUserSavedListener listener) {
        // Add a new document with a generated ID
        db.collection("Users").document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                        // If user document added successfully, invoke the listener with success
                        listener.onUserSaved(true, user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        // If adding user document failed, invoke the listener with failure
                        listener.onUserSaved(false, null);
                    }
                });

        db.collection("favorites").document(user.getId())
                .set(user.getFavorites().setUserId(user.getId()).setId(UUID.randomUUID().toString()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });

        db.collection("follow").document(user.getId())
                .set(user.getFollows().setUserId(user.getId()).setId(UUID.randomUUID().toString()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
