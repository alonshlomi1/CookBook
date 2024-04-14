package com.example.cookbook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.cookbook.Interfaces.RecipeLoadCallback;
import com.example.cookbook.Interfaces.UserLoadCallback;
import com.example.cookbook.Interfaces.UserRecipeListLoadCallback;
import com.example.cookbook.Logic.RecipeLogic;
import com.example.cookbook.Logic.UserLogic;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.UI.Fragments.HomePageFragment;
import com.example.cookbook.UI.Fragments.NewRecipeFragment;
import com.example.cookbook.UI.Fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeLoadCallback, UserLoadCallback, UserRecipeListLoadCallback {
    private UserLogic userLogic;
    private RecipeLogic recipeLogic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        userLogic = new UserLogic(this, this);
        recipeLogic = new RecipeLogic(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomePageFragment(getApplicationContext(), recipeLogic.getRecipeList()))
                .commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    if (item.getItemId() == R.id.navigation_home)
                        selectedFragment = new HomePageFragment(getApplicationContext(), recipeLogic.getRecipeList());
                    else if (item.getItemId() == R.id.navigation_profile)
                        selectedFragment = new UserProfileFragment(getApplicationContext(), userLogic.getUser(),  userLogic.getUserRecipeList());
                    else if (item.getItemId() == R.id.new_recipe)
                        selectedFragment = new NewRecipeFragment(getApplicationContext());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
            };



    public void onRecipeListLoaded(ArrayList<Recipe> recipes) {
        Fragment homePageFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (homePageFragment instanceof HomePageFragment) {
            ((HomePageFragment) homePageFragment).updateRecipeList(recipes);
        }

    }

    @Override
    public void onRecipeListLoadFailed(Exception e) {
        // Handle failure
        Log.e(TAG, "Error loading recipes", e);
    }
    @Override
    public void onUserLoaded(User user) {
        Fragment userProfileFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (userProfileFragment instanceof UserProfileFragment) {
            ((UserProfileFragment) userProfileFragment).updateUser(user);
        }
    }

    @Override
    public void onUserLoadFailed(Exception e) {
        Log.e(TAG, "Error loading user data", e);
    }

    @Override
    public void onUserRecipeListLoaded(ArrayList<Recipe> recipes) {
        Fragment userProfileFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (userProfileFragment instanceof UserProfileFragment) {
            ((UserProfileFragment) userProfileFragment).updateRecipeList(recipes);
        }
    }

    @Override
    public void onUserRecipeListLoadFailed(Exception e) {
        Log.e(TAG, "Error loading user recipes data", e);
    }

}