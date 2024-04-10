package com.example.cookbook.Models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String id = "";
    private String Title = "";
    private ArrayList<Ingredient> Ingredients = new ArrayList<>();
    private ArrayList<String> Instructions = new ArrayList<>();
    private String photoUrl = "";
    private String authorId = "";
    private ArrayList<String> categoriesTags = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Rate> ratings = new ArrayList<>();

    public Recipe() {
    }

    public String getId() {
        return id;
    }

    public Recipe setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return Title;
    }

    public Recipe setTitle(String title) {
        Title = title;
        return this;
    }

    public ArrayList<Ingredient> getIngredients() {
        return Ingredients;
    }

    public Recipe setIngredients(ArrayList<Ingredient> ingredients) {
        Ingredients = ingredients;
        return this;
    }

    public ArrayList<String> getInstructions() {
        return Instructions;
    }

    public Recipe setInstructions(ArrayList<String> instructions) {
        Instructions = instructions;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Recipe setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Recipe setAuthorId(String authorId) {
        this.authorId = authorId;
        return this;
    }

    public ArrayList<String> getCategoriesTags() {
        return categoriesTags;
    }

    public Recipe setCategoriesTags(ArrayList<String> categoriesTags) {
        this.categoriesTags = categoriesTags;
        return this;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public Recipe setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public ArrayList<Rate> getRatings() {
        return ratings;
    }

    public Recipe setRatings(ArrayList<Rate> ratings) {
        this.ratings = ratings;
        return this;
    }

    public float getAVGRating(){
        float sum = 0;
        for(Rate rate : ratings)
            sum += rate.getRate();
        return (sum/ratings.size());
    }
}
