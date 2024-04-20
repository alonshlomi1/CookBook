package com.example.cookbook.Logic;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.net.URL;

import com.example.cookbook.DataBaseLayer.DBManager;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Interfaces.RecipeResetListener;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.R;
import com.example.cookbook.UI.Fragments.HomePageFragment;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class RecipeLogic implements OnRecipesLoadedListener{
    public static ArrayList<Recipe> recipeList = new ArrayList<>();
    private OnRecipesLoadedListener callback;
    private DBManager dbManager;
    private Context context;
    public RecipeLogic(OnRecipesLoadedListener callback, Context context, int current){
        this.callback = callback;
        this.dbManager = new DBManager();
        this.context = context;
        this.recipeList = new ArrayList<>();

        if(current == R.id.home_MTV_favorite_segment)
            setFavoriteRecipeListFromDB();
        else if(current == R.id.home_MTV_follow_segment)
            setFollowingRecipeListFromDB();
        else
            setRecipeListFromDB();
        //setFavoriteRecipeListFromDB();
    }
    private Timestamp getLastRecipeDate(){
        int recipeListSize = recipeList.size();
        Timestamp lastRecipeDate = recipeListSize == 0? null : recipeList.get(recipeListSize-1).getDate();
        return lastRecipeDate;
    }
    public void setRecipeListFromDB() {
        dbManager.getRecipes(this, getLastRecipeDate());
    }

    public void setFavoriteRecipeListFromDB() {

        dbManager.getFavoriteRecipes(this, getLastRecipeDate());
    }
    public void setFollowingRecipeListFromDB() {
        dbManager.getFollowingRecipes(this, getLastRecipeDate());
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



    public boolean saveNewRecipe(Recipe recipe, Uri uri, RecipeResetListener resetListener){
        if(validateRecipe(recipe)){
            //recipe.setId(UUID.randomUUID().toString());
            dbManager.addRecipe(recipe, SingleManager.getInstance().compresImage(uri), resetListener);
            return true;
        }
        return false;

    }

    private boolean validateRecipe(Recipe recipe){
        if(recipe.getTitle().length() < 3 || recipe.getTitle().length() > 40){
            SingleManager.getInstance().toast("Recipe Title must be between 3 and 20");
            Log.d("Recipe Title Error", "min 3 max 40 " +recipe.getTitle());
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


    @Override
    public void onRecipesLoaded(ArrayList<Recipe> recipes) {
        HomePageFragment.isLoading = false;
        attachURL(recipes);
        recipeList.addAll(recipes);

        if (callback != null) {
            callback.onRecipesLoaded(recipeList);
        }
    }

    @Override
    public void onRecipesLoadFailed(Exception e) {
        Log.e(TAG, "Error loading recipes", e);
        if (callback != null) {
            callback.onRecipesLoadFailed(e);
        }
    }
}
