package com.example.cookbook.Interfaces;

import com.example.cookbook.Models.User;

public interface UserLoadCallback {
    void onUserLoaded(User user);
    void onUserLoadFailed(Exception e);
}
