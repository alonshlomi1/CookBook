package com.example.cookbook.Models;

import java.io.Serializable;

public class User implements Serializable {

    private String id = "";
    private String username = "";
    private String firstName = "";
    private String lastName = "";
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

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", profile_URL='" + profile_URL + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
