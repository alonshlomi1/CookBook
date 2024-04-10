package com.example.cookbook.Models;

import java.util.ArrayList;

public class following {

    private String id = "";
    private String userId = "";
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> following = new ArrayList<>();

    public following() {
    }

    public String getId() {
        return id;
    }

    public following setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public following setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public following setFollowers(ArrayList<String> followers) {
        this.followers = followers;
        return this;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public following setFollowing(ArrayList<String> following) {
        this.following = following;
        return this;
    }
}
