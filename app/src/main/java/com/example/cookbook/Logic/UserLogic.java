package com.example.cookbook.Logic;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.cookbook.DataBaseLayer.DBManager;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.UserLoadCallback;
import com.example.cookbook.Interfaces.UserRecipeListLoadCallback;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.Utilities.SingleManager;

import java.util.ArrayList;

public class UserLogic {
    
    private User user = null;
    private ArrayList<Recipe> userRecipeList = new ArrayList<>();
    private UserLoadCallback userCallback;
    private UserRecipeListLoadCallback userResipeListCallback;

    public UserLogic(UserLoadCallback userCallback, UserRecipeListLoadCallback userResipeListCallback){
        this.userCallback = userCallback;
        this.userResipeListCallback = userResipeListCallback;
        setUserListFromDB();
        setUserRecipeListFromDB();
    }

    private void setUserListFromDB() {
        user = new User();
        user.setId("userID_1")
                .setUsername("User1")
                .setFirstName("First")
                .setLastName("Last")
                .setBio("hi..................................\nby....................")
                .setEmail("email@gmail.com")
                .setProfile_URL("https://i.stack.imgur.com/l60Hf.png");
        if (userCallback != null) {
            userCallback.onUserLoaded(user);
        }
    }

    private void setUserRecipeListFromDB() {
        DBManager dbManager = new DBManager();
        dbManager.getUserRecipes(new OnRecipesLoadedListener() {
            @Override
            public void onRecipesLoaded(ArrayList<Recipe> recipes) {
                // Handle fetched recipes
                userRecipeList = recipes;
                if (userResipeListCallback != null) {
                    userResipeListCallback.onUserRecipeListLoaded(userRecipeList);
                }
            }
            @Override
            public void onRecipesLoadFailed(Exception e) {
                // Handle failure
                Log.e(TAG, "Error loading recipes", e);
            }
        }, user.getId());
    }

    public User getUser() {
        return user;
    }

    public UserLogic setUser(User user) {
        this.user = user;
        return this;
    }

    public ArrayList<Recipe> getUserRecipeList() {
        return userRecipeList;
    }

    public UserLogic setUserRecipeList(ArrayList<Recipe> userRecipeList) {
        this.userRecipeList = userRecipeList;
        return this;
    }
}
