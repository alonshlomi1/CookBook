package com.example.cookbook.DataBaseLayer;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.onFavoritesListener;
import com.example.cookbook.Models.Favorites;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FavoritesDB {
    private FirebaseFirestore db;
    public FavoritesDB(FirebaseFirestore db){
        this.db = db;
    }


    public void getFavorites(String user_Id, onFavoritesListener listener){
        db.collection("favorites")
                .whereEqualTo("userId", user_Id)
                .limit(1) // Limit the query to retrieve only one document
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //Following follows = new Following();
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Retrieve the first document
                            Favorites favorites = document.toObject(Favorites.class);
                            Log.d("FOLLOW@@", favorites.toString());
                            listener.onFavoritesReady(true, favorites);

                        } else {
                            Log.d("FOLLOW@@", task.getResult().getDocuments().toString());
                            listener.onFavoritesReady(false, null);
                        }
                    }
                });
    }


    public void saveFavorites() {
        Favorites favorites = SingleManager.getInstance().getUserManager().getUser().getFavorites();
        db.collection("favorites").document(favorites.getUserId())
                .set(favorites)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SingleManager.getInstance().toast("Favorite Saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error Save UnFavorite document", e);
                        // If adding user document failed, invoke the listener with failure
                    }
                });
    }
}
