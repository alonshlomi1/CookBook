package com.example.cookbook.DataBaseLayer;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.OnFollowsListener;
import com.example.cookbook.Interfaces.onFavoritesListener;
import com.example.cookbook.Models.Favorites;
import com.example.cookbook.Models.Following;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FollowsDB {

    private FirebaseFirestore db;
    public FollowsDB(FirebaseFirestore db){
        this.db = db;
    }

    public void follow(Following following, String followingId, String name) {
        following.addFollowing(followingId, name);
        db.collection("follow").document(following.getUserId())
                .set(following)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User followed successfully");
                        updateFollowing(followingId, following.getUserId(), SingleManager.getInstance().getUserManager().getUser().getFirstName() + " "+SingleManager.getInstance().getUserManager().getUser().getLastName() );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error following user", e);
                    }
                });
    }
    public void updateFollowing(String followingId, String followerId, String follower_name){
        db.collection("follow")
                .whereEqualTo("userId", followingId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Following following = document.toObject(Following.class);
                                Log.d("@@@@@@@@", following.getUserId());
                                following.addFollower(followerId, follower_name);


                                db.collection("follow").document(followingId)
                                        .set(following)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "User following successfully");
                                                SingleManager.getInstance().toast("User followed successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Error following user", e);
                                            }
                                        });


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            // Invoke the listener with the error
                        }
                    }
                });
    }
    public void unfollow(String followingId, String followerId) {
        db.collection("follow")
                .whereEqualTo("userId", followingId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Following following = document.toObject(Following.class);
                                following.removeFollower(followerId);
                                SingleManager.getInstance().getUserManager().getUser().setFollows(following);

                                db.collection("follow").document(followingId)
                                        .set(following)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                updateUnfollow(followingId, followerId);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Error unfollowing user", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void updateUnfollow(String followingId, String followerId) {
        db.collection("follow")
                .whereEqualTo("userId", followerId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Following following = document.toObject(Following.class);
                                following.removeFollowing(followingId);

                                db.collection("follow").document(followerId)
                                        .set(following)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "User unfollowed successfully");
                                                SingleManager.getInstance().toast("User unfollowed successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Error unfollowing user", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void getFollowing(String user_Id, OnFollowsListener listener){
        db.collection("follow")
                .whereEqualTo("userId", user_Id)
                .limit(1) // Limit the query to retrieve only one document
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //Following follows = new Following();
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Retrieve the first document
                            Following follows = document.toObject(Following.class);
                            listener.onFollowReady(true, follows);

                        } else {
                            listener.onFollowReady(false, null);
                        }
                    }
                });
    }

}
