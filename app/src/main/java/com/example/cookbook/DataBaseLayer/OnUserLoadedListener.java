package com.example.cookbook.DataBaseLayer;

import com.example.cookbook.Models.User;

public interface OnUserLoadedListener {
    void onUserLoaded(User user);
    void onUserLoadFailed(Exception e);

}
