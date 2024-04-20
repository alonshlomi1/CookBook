package com.example.cookbook.DataBaseLayer;

import android.util.Log;

import com.example.cookbook.Interfaces.OnFollowsListener;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Interfaces.OnUnfollowListener;
import com.example.cookbook.Interfaces.OnUserLoadedListener;
import com.example.cookbook.Interfaces.OnUserSavedListener;
import com.example.cookbook.Interfaces.RecipeResetListener;
import com.example.cookbook.Interfaces.onFavoritesListener;
import com.example.cookbook.Models.Following;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.Utilities.SingleManager;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DBManager {
    private final int batchSize = 5;
    private FirebaseFirestore db;
    private UserDB userDB;
    private RecipeDB recipeDB;
    private ImageDB imageDB;
    private FollowsDB followsDB;
    private FavoritesDB favoritesDB;

    public DBManager(){

        db = SingleManager.getInstance().getDb();
        userDB = new UserDB(db);
        recipeDB = new RecipeDB(db);
        imageDB = new ImageDB(db);
        followsDB = new FollowsDB(db);
        favoritesDB = new FavoritesDB(db);
    }

    public void addRecipe(Recipe recipe,byte[] uri, RecipeResetListener resetListener){
        recipeDB.addRecipe(recipe,uri,resetListener);
    }

    public void getRecipes(OnRecipesLoadedListener listener, Timestamp lastRecipeDate){
        recipeDB.getRecipes(listener, batchSize, lastRecipeDate);
    }

    public void getFavoriteRecipes(OnRecipesLoadedListener listener, Timestamp lastRecipeDate){
        recipeDB.getFavoriteRecipes(listener, batchSize, lastRecipeDate);
    }
    public void getFollowingRecipes(OnRecipesLoadedListener listener, Timestamp lastRecipeDate){
        recipeDB.getFollowingRecipes(listener, batchSize, lastRecipeDate);
    }
    public void getUser(OnUserLoadedListener listener, String email){
        userDB.getUser(listener,email);
    }
    public void getRecipesURI(OnRecipesURLLoadedListener listener, String recipeID){
        imageDB.getRecipesURI(listener, recipeID);
    }
    public void saveImage(String id, byte[] uri) {
        imageDB.saveImage(id,uri);
    }
    public void getUserRecipes(OnRecipesLoadedListener listener, String id){
        recipeDB.getUserRecipes(listener,id);
    }

    public void updateRecipe(Recipe recipe) {
        recipeDB.updateRecipe(recipe);
    }

    public void saveUser(User user, OnUserSavedListener listener) {
        userDB.saveUser(user, listener);
    }
    public void saveNewUser(User user, OnUserSavedListener listener) {
        userDB.saveNewUser(user, listener);
    }

    public void follow(Following following, String followingId, String name) {
        followsDB.follow(following, followingId, name);

    }
    public void followWithEmail(String email) {
        userDB.getUser(new OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                HashMap<String , String> list = SingleManager.getInstance().getUserManager().getUser().getFollows().getFollowing();
                if(!list.containsKey(user.getId())){
                    follow(SingleManager.getInstance().getUserManager().getUser().getFollows(), user.getId(), user.getFirstName() +" "+ user.getLastName());
                }
                else{
                    SingleManager.getInstance().toast("Already Follow that User");
                }

            }

            @Override
            public void onUserLoadFailed(Exception e) {
                SingleManager.getInstance().toast("User was not found");
            }
        }, email);
    }
    public void unfollow(String followerId, OnUnfollowListener listener) {
        followsDB.unfollow(followerId, SingleManager.getInstance().getUserManager().getUser().getId(), listener);
    }
    public void getFollowing(String user_Id, OnFollowsListener listener){
        followsDB.getFollowing(user_Id, listener);
    }

    public void getFavorites(String user_Id, onFavoritesListener listener){
        favoritesDB.getFavorites(user_Id, listener);
    }


    public void saveFavorites() {
        favoritesDB.saveFavorites();
    }


















}

