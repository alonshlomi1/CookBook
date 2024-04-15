package com.example.cookbook.Utilities;

import com.example.cookbook.Models.User;

public class UserManager {
    public static User user = null;

    public UserManager(){

    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserManager.user = user;
    }
}
