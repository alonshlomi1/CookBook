package com.example.cookbook.Interfaces;

import com.example.cookbook.Models.Favorites;
import com.example.cookbook.Models.Following;

public interface onFavoritesListener{
    void onFavoritesReady(boolean success, Favorites favorites);

}
