package com.example.cookbook.Interfaces;
import com.example.cookbook.Models.Recipe;

import java.util.ArrayList;

public interface OnRecipesLoadedListener {
    void onRecipesLoaded(ArrayList<Recipe> recipes);
    void onRecipesLoadFailed(Exception e);
}
