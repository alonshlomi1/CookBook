package com.example.cookbook.Models;

public class Comment {

    private String id = "";
    private String userId = "";
    private String recipeId = "";

    private String comment = "";

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public Comment setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Comment setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Comment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public Comment setRecipeId(String recipeId) {
        this.recipeId = recipeId;
        return this;
    }
}
