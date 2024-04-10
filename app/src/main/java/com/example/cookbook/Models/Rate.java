package com.example.cookbook.Models;

public class Rate {

    private String userId = "";
    private double rate = 3.0;
    private String recipeId = "";

    public Rate() {
    }

    public String getUserId() {
        return userId;
    }

    public Rate setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public double getRate() {
        return rate;
    }

    public Rate setRate(double rate) {
        this.rate = rate;
        return this;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public Rate setRecipeId(String recipeId) {
        this.recipeId = recipeId;
        return this;
    }
}
