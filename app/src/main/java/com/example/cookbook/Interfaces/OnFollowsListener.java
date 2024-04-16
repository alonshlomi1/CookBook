package com.example.cookbook.Interfaces;

import com.example.cookbook.Models.Following;
import com.example.cookbook.Models.User;

public interface OnFollowsListener {
    void onFollowReady(boolean success, Following follows);
}
