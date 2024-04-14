package com.example.cookbook.Interfaces;

import com.example.cookbook.Models.Recipe;

import java.util.ArrayList;

public interface RecipeLoadCallback {
    void onRecipeListLoaded(ArrayList<Recipe> recipes);
    void onRecipeListLoadFailed(Exception e);
}
