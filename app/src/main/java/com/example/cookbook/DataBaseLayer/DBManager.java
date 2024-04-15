package com.example.cookbook.DataBaseLayer;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Interfaces.RecipeResetListener;
import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
}

