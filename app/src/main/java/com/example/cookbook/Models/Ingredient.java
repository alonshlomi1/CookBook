package com.example.cookbook.Models;

public class Ingredient {
    private String name = "";
    private double amount = 0.0;
    private String recipeId = "";

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Ingredient setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public Ingredient setRecipeId(String recipeID) {
        this.recipeId = recipeID;
        return this;
    }
}
