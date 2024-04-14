package com.example.cookbook.Interfaces;

import com.example.cookbook.Models.Recipe;

import java.util.ArrayList;

public interface UserRecipeListLoadCallback {
    void onUserRecipeListLoaded(ArrayList<Recipe> recipes);
    void onUserRecipeListLoadFailed(Exception e);
}
