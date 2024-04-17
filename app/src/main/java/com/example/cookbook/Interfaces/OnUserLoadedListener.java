package com.example.cookbook.Interfaces;

import com.example.cookbook.Models.User;

public interface OnUserLoadedListener {
    void onUserLoaded(User user);
    void onUserLoadFailed(Exception e);

}
