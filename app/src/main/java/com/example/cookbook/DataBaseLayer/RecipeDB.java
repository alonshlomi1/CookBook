package com.example.cookbook.DataBaseLayer;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.RecipeResetListener;
import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class RecipeDB {

    private FirebaseFirestore db;
    private ImageDB imageDB;

    public RecipeDB(FirebaseFirestore db){
        this.db = db;
        imageDB = new ImageDB(db);
    }


    public void addRecipe(Recipe recipe, byte[] uri, RecipeResetListener resetListener){
        db.collection("recipes").document(recipe.getId()).set(recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        imageDB.saveImage(recipe.getId(), uri);
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

    public void getFavoriteRecipes(OnRecipesLoadedListener listener){

        ArrayList<String > favoritesRecipeIDList = SingleManager.getInstance().getUserManager().getUser().getFavorites().getFavoritesId();
        ArrayList<Recipe> recipeList = new ArrayList<>();
        Log.d("FAVORI##LIST", favoritesRecipeIDList.toString());

        if(favoritesRecipeIDList.size() > 0){
            db.collection("recipes")
                    .whereIn(FieldPath.documentId(), favoritesRecipeIDList)
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
        listener.onRecipesLoaded(recipeList);


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
