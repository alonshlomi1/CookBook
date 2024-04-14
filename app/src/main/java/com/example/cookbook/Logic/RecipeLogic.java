package com.example.cookbook.Logic;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.cookbook.DataBaseLayer.DBManager;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.RecipeLoadCallback;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.R;
import com.example.cookbook.UI.Fragments.HomePageFragment;
import com.example.cookbook.UI.Fragments.NewRecipeFragment;
import com.example.cookbook.UI.Fragments.UserProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeLogic {
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private Recipe new_Recipe = null;
    private RecipeLoadCallback callback;

    public RecipeLogic(RecipeLoadCallback callback){
        this.callback = callback;
        setRecipeListFromDB();
    }

    private void setRecipeListFromDB() {
        DBManager dbManager = new DBManager();
        dbManager.getRecipes(new OnRecipesLoadedListener() {
            @Override
            public void onRecipesLoaded(ArrayList<Recipe> recipes) {
                // Handle fetched recipes
                recipeList = recipes;
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

    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    public RecipeLogic setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        return this;
    }

    public Recipe getNew_Recipe() {
        return new_Recipe;
    }

    public RecipeLogic setNew_Recipe(Recipe new_Recipe) {
        this.new_Recipe = new_Recipe;
        return this;
    }



}
