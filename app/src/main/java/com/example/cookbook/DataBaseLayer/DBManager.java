package com.example.cookbook.DataBaseLayer;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cookbook.Interfaces.OnFollowsListener;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Interfaces.OnUserLoadedListener;
import com.example.cookbook.Interfaces.OnUserSavedListener;
import com.example.cookbook.Interfaces.RecipeResetListener;
import com.example.cookbook.Interfaces.onFavoritesListener;
import com.example.cookbook.Models.Favorites;
import com.example.cookbook.Models.Following;
import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class DBManager {
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

    public void getRecipes(OnRecipesLoadedListener listener){
        recipeDB.getRecipes(listener);
    }

    public void getFavoriteRecipes(OnRecipesLoadedListener listener){
        recipeDB.getFavoriteRecipes(listener);
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
        Log.d("Email@@@", name);

    }
    public void followWithEmail(String email) {
        Log.d("Email@@", email);


        userDB.getUser(new OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                Log.d("Email@@", user.toString());

                follow(SingleManager.getInstance().getUserManager().getUser().getFollows(), user.getId(), user.getFirstName() +" "+ user.getLastName());
            }

            @Override
            public void onUserLoadFailed(Exception e) {
                SingleManager.getInstance().toast("User was not found");
            }
        }, email);
    }
    public void unfollow(String followerId) {
        followsDB.unfollow(followerId, SingleManager.getInstance().getUserManager().getUser().getId());
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

