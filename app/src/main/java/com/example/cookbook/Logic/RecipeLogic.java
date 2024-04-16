package com.example.cookbook.Logic;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import com.example.cookbook.DataBaseLayer.DBManager;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Interfaces.RecipeLoadCallback;
import com.example.cookbook.Interfaces.RecipeResetListener;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Utilities.SingleManager;
import java.util.ArrayList;

public class RecipeLogic {
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private RecipeLoadCallback callback;
    private DBManager dbManager;
    private Context context;
    public RecipeLogic(RecipeLoadCallback callback, Context context){
        this.callback = callback;
        this.dbManager = new DBManager();
        this.context = context;
        setRecipeListFromDB();
    }

    private void setRecipeListFromDB() {
        dbManager.getRecipes(new OnRecipesLoadedListener() {
            @Override
            public void onRecipesLoaded(ArrayList<Recipe> recipes) {
                // Handle fetched recipes
                recipeList = recipes;
                attachURL(recipeList);
                if (callback != null) {
                    callback.onRecipeListLoaded(recipeList);
                }

            }
            @Override
            public void onRecipesLoadFailed(Exception e) {
                Log.e(TAG, "Error loading recipes", e);
                if (callback != null) {
                    callback.onRecipeListLoadFailed(e);
                }
            }
        });
    }

    private void attachURL(ArrayList<Recipe> recipeList) {
        for (Recipe recipe: recipeList) {
            if(recipe.getPhotoUrl() != null) {
                dbManager.getRecipesURI(new OnRecipesURLLoadedListener() {
                    @Override
                    public void onRecipesURLLoaded(URL url) {
                        recipe.setPhotoUrl(url.toString());
                    }

                    @Override
                    public void onRecipesURLLoadFailed(Exception e) {
                        Log.d("Recipe Photo","Fail loading for recipe "+ recipe.getTitle());
                    }
                }, recipe.getId());
            }

        }
    }

    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }


    public RecipeLogic setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        return this;
    }

    public boolean saveNewRecipe(Recipe recipe, Uri uri, RecipeResetListener resetListener){
        if(validateRecipe(recipe)){
            //recipe.setId(UUID.randomUUID().toString());
            dbManager.addRecipe(recipe, SingleManager.getInstance().compresImage(uri), resetListener);
            return true;
        }

        return false;

    }
//    public byte[] compresImage(Uri uri){
//        Bitmap bmp = null;
//        try {
//            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        // here we can choose quality factor
//        // in third parameter(ex. here it is 25)
//        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
//
//        byte[] fileInBytes = baos.toByteArray();
//        return fileInBytes;
//    }

    private boolean validateRecipe(Recipe recipe){
        if(recipe.getTitle().length() < 3 || recipe.getTitle().length() > 20){
            SingleManager.getInstance().toast("Recipe Title must be between 3 and 20");
            Log.d("Recipe Title Error", "min 3 max 20 " +recipe.getTitle());
            return false;
        }
        if(recipe.getIngredients().size() < 1 || recipe.getIngredients().size() > 100){
            SingleManager.getInstance().toast("Recipe Ingredients must be between 1 and 100");
            Log.d("Recipe Ingredients Error", "min 1 max 100");
            return false;
        }
        if(recipe.getInstructions().size() < 1 || recipe.getInstructions().size() > 100){
            SingleManager.getInstance().toast("Recipe Instructions must be between 1 and 100");
            Log.d("Recipe Instructions Error", "min 1 max 100");
            return false;
        }




        return true;
    }


}
