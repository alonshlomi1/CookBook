package com.example.cookbook.DataBaseLayer;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.OnFollowsListener;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Interfaces.OnUserSavedListener;
import com.example.cookbook.Interfaces.RecipeResetListener;
import com.example.cookbook.Models.Following;
import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.security.auth.callback.Callback;

public class DBManager {
    private FirebaseFirestore db;

    public DBManager(){
        db = SingleManager.getInstance().getDb();
    }

    public void addRecipe(Recipe recipe,byte[] uri, RecipeResetListener resetListener){
        db.collection("recipes").document(recipe.getId()).set(recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        saveImage(recipe.getId(), uri);
                        SingleManager.getInstance().toast("Recipe Uploaded!");
                        resetListener.resetRecipe();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        SingleManager.getInstance().toast("Recipe Upload Failed!");
                    }
                });
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
    public void getRecipes(OnRecipesLoadedListener listener){
        ArrayList<Recipe> recipeList = new ArrayList<>();

        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe recipe = document.toObject(Recipe.class);
                                // Assuming your Firestore document contains fields for ingredients and instructions
                                ArrayList<Map<String, Object>> ingredientsData = (ArrayList<Map<String, Object>>) document.get("ingredients");
                                ArrayList<String> instructions = (ArrayList<String>) document.get("instructions");
                                // Parse ingredientsData and instructionsData into appropriate objects
                                ArrayList<Ingredient> ingredients = parseIngredients(ingredientsData, recipe.getId());
                                // Set parsed ingredients and instructions to the recipe
                                recipe.setIngredients(ingredients);
                                recipe.setInstructions(instructions);
                                recipeList.add(recipe);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            // Invoke the listener with the fetched recipes
                            listener.onRecipesLoaded(recipeList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            // Invoke the listener with the error
                            listener.onRecipesLoadFailed(task.getException());
                        }
                    }
                });
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
                                Log.d("@@@@@@@@", user.getEmail());
                                Log.d("@@@@@@@@--", user.toString());
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
    public void getUserRecipes(OnRecipesLoadedListener listener, String id){
        ArrayList<Recipe> recipeList = new ArrayList<>();

        db.collection("recipes")
                .whereEqualTo("authorId", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe recipe = document.toObject(Recipe.class);
                                // Assuming your Firestore document contains fields for ingredients and instructions
                                ArrayList<Map<String, Object>> ingredientsData = (ArrayList<Map<String, Object>>) document.get("ingredients");
                                ArrayList<String> instructions = (ArrayList<String>) document.get("instructions");
                                // Parse ingredientsData and instructionsData into appropriate objects
                                ArrayList<Ingredient> ingredients = parseIngredients(ingredientsData, recipe.getId());
                                // Set parsed ingredients and instructions to the recipe
                                recipe.setIngredients(ingredients);
                                recipe.setInstructions(instructions);
                                recipeList.add(recipe);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            // Invoke the listener with the fetched recipes
                            listener.onRecipesLoaded(recipeList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            // Invoke the listener with the error
                            listener.onRecipesLoadFailed(task.getException());
                        }
                    }
                });
    }

    // Method to parse ingredients data into Ingredient objects
    private ArrayList<Ingredient> parseIngredients(ArrayList<Map<String, Object>> ingredientsData, String id) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (Map<String, Object> ingredientData : ingredientsData) {
            // Extract the name, amount, and recipeId from the map
            String name = (String) ingredientData.get("name");
            double amount = (double) ingredientData.get("amount");
            // Create and add the Ingredient object to the list
            ingredients.add(new Ingredient().setName(name).setAmount(amount).setRecipeId(id));
        }
        return ingredients;
    }

    public void updateRecipe(Recipe recipe) {
        db.collection("recipes").document(recipe.getId())
                // Update the fields of the document with the new data from the recipe object
                .set(recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Recipe updated successfully
                        Log.d(TAG, "Recipe updated successfully");
                        SingleManager.getInstance().toast("Comment Submitted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update recipe
                        Log.e(TAG, "Error updating recipe", e);
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
    }

    public void follow(Following following, String followingId) {
        following.addFollowing(followingId);
        db.collection("follow").document(following.getUserId())
                .set(following)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User followed successfully");
                        updateFollowing(followingId, following.getUserId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error following user", e);
                    }
                });
    }

    public void updateFollowing(String followingId, String followerId){
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
                                following.addFollower(followerId);


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
                            Log.d("FOLLOW@@", task.getResult().getDocuments().toString());
                            listener.onFollowReady(false, null);
                        }
                    }
                });
    }


}

