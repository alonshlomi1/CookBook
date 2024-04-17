package com.example.cookbook.Models;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class Favorites {
    private String id = "";
    private String userId = "";
    private ArrayList<String> favoritesId = new ArrayList<>();

    public Favorites() {
    }

    public String getId() {
        return id;
    }

    public Favorites setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Favorites setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public ArrayList<String> getFavoritesId() {
        return favoritesId;
    }

    public Favorites setFavoritesId(ArrayList<String> favoritesId) {
        this.favoritesId = favoritesId;
        return this;
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", favoritesId=" + favoritesId +
                '}';
    }
}
