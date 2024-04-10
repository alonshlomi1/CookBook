package com.example.cookbook.Models;

public class User {

    private String id = "";
    private String username = "";
    private String email = "";
    private String profile_URL = "";
    private String bio = "";

    public User() {
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getProfile_URL() {
        return profile_URL;
    }

    public User setProfile_URL(String profile_URL) {
        this.profile_URL = profile_URL;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public User setBio(String bio) {
        this.bio = bio;
        return this;
    }
}
