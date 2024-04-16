package com.example.cookbook.Models;

import java.util.ArrayList;

public class Following {

    private String id = "";
    private String userId = "";
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> following = new ArrayList<>();

    public Following() {
    }

    public String getId() {
        return id;
    }

    public Following setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Following setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public Following setFollowers(ArrayList<String> followers) {
        this.followers = followers;
        return this;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public Following setFollowing(ArrayList<String> following) {
        this.following = following;
        return this;
    }

    public void addFollowing(String  following_id) {
        // Add the comment to the comments list
        following.add(following_id);
    }
    public void addFollower(String  follower_id) {
        // Add the comment to the comments list
        followers.add(follower_id);    }
}
