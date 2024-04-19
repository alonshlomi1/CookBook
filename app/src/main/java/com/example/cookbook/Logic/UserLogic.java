package com.example.cookbook.Logic;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.DataBaseLayer.DBManager;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Interfaces.UserLoadCallback;
import com.example.cookbook.Interfaces.UserRecipeListLoadCallback;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class UserLogic {
    
    private User user = null;
    private ArrayList<Recipe> userRecipeList = new ArrayList<>();
    private UserLoadCallback userCallback;
    private UserRecipeListLoadCallback userResipeListCallback;
    private DBManager dbManager;

    public UserLogic(UserLoadCallback userCallback, UserRecipeListLoadCallback userResipeListCallback){
        this.userCallback = userCallback;
        this.userResipeListCallback = userResipeListCallback;
        this.dbManager = new DBManager();
        detMockupUser();
        setUserListFromDB();
        setUserRecipeListFromDB();
    }
    public UserLogic(UserLoadCallback userCallback, UserRecipeListLoadCallback userResipeListCallback, User theuser){
        this.userCallback = userCallback;
        this.userResipeListCallback = userResipeListCallback;
        this.dbManager = new DBManager();
//        this.user = new User();
        this.user = theuser;
        setUserListFromDB();
        setUserRecipeListFromDB();
    }
    private void detMockupUser(){
        user = new User();
        user.setId("userID_1")
                .setUsername("User1")
                .setFirstName("First")
                .setLastName("Last")
                .setBio("hi..................................\nby....................")
                .setEmail("email@gmail.com")
                .setProfile_URL("https://i.stack.imgur.com/l60Hf.png");
    }
    private void setUserListFromDB() {
        if(user != null){
            StorageReference storageRef = SingleManager.getInstance().getStorage().getReference().child("images/"+ user.getId() +".jpg");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    try {
                        user.setProfile_URL(new URL(uri.toString()).toString() );
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    if (userCallback != null) {
                        userCallback.onUserLoaded(user);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("URI-@@@@@@@@@", exception.toString());
                }
            });

        }

    }

    private void setUserRecipeListFromDB() {
        if(user != null) {
            dbManager.getUserRecipes(new OnRecipesLoadedListener() {
                @Override
                public void onRecipesLoaded(ArrayList<Recipe> recipes) {
                    // Handle fetched recipes
                    userRecipeList = recipes;
                    attachURL(recipes);
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
    }
    private void attachURL(ArrayList<Recipe> recipeList) {
        for (Recipe recipe: recipeList) {
            if(recipe.getPhotoUrl() != null){
                dbManager.getRecipesURI(new OnRecipesURLLoadedListener() {
                    @Override
                    public void onRecipesURLLoaded(URL url) {

                        recipe.setPhotoUrl(url.toString());
                    }

                    @Override
                    public void onRecipesURLLoadFailed(Exception e) {
                        Log.d("Recipe Photo","Fail loading for recipe "+ recipe.getTitle());
                    }
                }, recipe.getId());
            }
        }
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
