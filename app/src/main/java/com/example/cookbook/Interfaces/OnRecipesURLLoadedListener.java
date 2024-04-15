package com.example.cookbook.Interfaces;

import com.example.cookbook.Models.Recipe;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public interface OnRecipesURLLoadedListener {
    void onRecipesURLLoaded(URL url);
    void onRecipesURLLoadFailed(Exception e);
}
