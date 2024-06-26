package com.example.cookbook;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.RefreshHomeListener;
import com.example.cookbook.Interfaces.UserLoadCallback;
import com.example.cookbook.Interfaces.UserRecipeListLoadCallback;
import com.example.cookbook.Logic.RecipeLogic;
import com.example.cookbook.Logic.UserLogic;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.UI.Fragments.HomePageFragment;
import com.example.cookbook.UI.Fragments.NewRecipeFragment;
import com.example.cookbook.UI.Fragments.UserProfileFragment;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRecipesLoadedListener, UserLoadCallback, UserRecipeListLoadCallback, RefreshHomeListener {
    private UserLogic userLogic;
    private RecipeLogic recipeLogic;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        userLogic = new UserLogic(this, this, SingleManager.getInstance().getUserManager().getUser());
        recipeLogic = new RecipeLogic(this, getApplicationContext(), R.id.home_MTV_gen_segment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomePageFragment(getApplicationContext(), recipeLogic.getRecipeList(),this, recipeLogic))
                .commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    if (item.getItemId() == R.id.navigation_home){
                        selectedFragment = getSupportFragmentManager().findFragmentByTag("HomePageFragment");
                        if (selectedFragment == null) {
                            selectedFragment = new HomePageFragment(getApplicationContext(), recipeLogic.getRecipeList(), MainActivity.this, recipeLogic);
                        }
                    }
                        //selectedFragment = new HomePageFragment(getApplicationContext(), recipeLogic.getRecipeList(), MainActivity.this);
                    else if (item.getItemId() == R.id.navigation_profile){
                        selectedFragment = getSupportFragmentManager().findFragmentByTag("UserProfileFragment");
                        if (selectedFragment == null) {
                            selectedFragment = new UserProfileFragment(getApplicationContext(), userLogic.getUser(), userLogic.getUserRecipeList(), MainActivity.this);
                        }
                    }
//                        selectedFragment = new UserProfileFragment(getApplicationContext(), userLogic.getUser(),  userLogic.getUserRecipeList());
                    else if (item.getItemId() == R.id.new_recipe)
                        selectedFragment = new NewRecipeFragment(getApplicationContext(), recipeLogic, userLogic.getUser().getId());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
            };


    public void refresh(SwipeRefreshLayout home_SWIPE_refresh, int current){
        swipeRefreshLayout = home_SWIPE_refresh;
        recipeLogic = new RecipeLogic(this, getApplicationContext(), current);
        userLogic = new UserLogic(this, this, SingleManager.getInstance().getUserManager().getUser());

    }
    @Override
    public void onRecipesLoaded(ArrayList<Recipe> recipes) {
        Fragment homePageFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (homePageFragment instanceof HomePageFragment) {
            ((HomePageFragment) homePageFragment).updateRecipeList(recipes);
         }
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRecipesLoadFailed(Exception e) {
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
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void onUserRecipeListLoadFailed(Exception e) {
        Log.e(TAG, "Error loading user recipes data", e);
    }

}