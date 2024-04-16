package com.example.cookbook.Interfaces;

import com.example.cookbook.Models.User;

public interface OnUserSavedListener {
    void onUserSaved(boolean success, User user);
}
