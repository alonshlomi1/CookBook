package com.example.cookbook.DataBaseLayer;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
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

import java.util.ArrayList;
import java.util.Map;

public class DBManager {
    private FirebaseFirestore db;

    public DBManager(){
        db = SingleManager.getInstance().getDb();
    }

    public void addRecipe(Recipe recipe){
        db.collection("recipes").document(recipe.getId()).set(recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("@@@@@@@@@@@@@@", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("@@@@@@@@@@@@@@", "Error writing document", e);
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

}

