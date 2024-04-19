package com.example.cookbook.Models;

import android.util.Log;

public class Ingredient {
    public static enum AMOUNT_TYPE {
        CUP, TEASPOON, SPOON, GRAMS, COUNT , DEF
    }
    private String name = " ";
    private double amount = 0.0;

    private String recipeId = " ";

    private AMOUNT_TYPE type= AMOUNT_TYPE.DEF;
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

    public String getType() {
        return type.toString();
    }

    public Ingredient setType(AMOUNT_TYPE type) {
        this.type = type;
        return this;
    }
}
