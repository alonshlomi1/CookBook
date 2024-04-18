package com.example.cookbook.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class Following {

    private String id = "";
    private String userId = "";
    private HashMap<String, String> followers = new HashMap<String, String>();
    private HashMap<String, String> following = new HashMap<String, String>();

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

    public HashMap<String, String> getFollowers() {
        return followers;
    }

    public Following setFollowers(HashMap<String, String> followers) {
        this.followers = followers;
        return this;
    }

    public HashMap<String, String> getFollowing() {
        return following;
    }

    public Following setFollowing(HashMap<String, String> following) {
        this.following = following;
        return this;
    }

    public Following addFollowing(String  following_id, String name) {
        // Add the comment to the comments list
        following.put(following_id, name);
        return this;
    }
    public Following addFollower(String  follower_id, String name) {
        // Add the comment to the comments list
        followers.put(follower_id, name);
        return this;
    }
    public Following removeFollower(String  follower_id) {
        // Add the comment to the comments list
        followers.remove(follower_id);
        return this;
    }
    public Following removeFollowing(String  follower_id) {
        // Add the comment to the comments list
        following.remove(follower_id);
        return this;
    }

    @Override
    public String toString() {
        return "Following{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }
}
