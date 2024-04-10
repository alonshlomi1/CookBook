package com.example.cookbook.UI.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cookbook.Adapters.RecipeAdapter;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


public class UserProfileFragment extends Fragment {

    private ShapeableImageView profile_SIV_image;
    private MaterialTextView profile_TV_name;
    private MaterialTextView profile_TV_info;
    private RecyclerView home_LST_recipe;
    private Context applicationContext;
    private ArrayList<Recipe> recipeList;
    private User user;
    public UserProfileFragment(Context Context, User user, ArrayList<Recipe> recipeList) {
        int limit = Math.min(recipeList.size(), 10);
        this.recipeList = recipeList;//(ArrayList<Recipe>) recipeList.subList(0, limit);
        this.applicationContext = Context;
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        findViews(view);
        initViews(view);
        return view;

    }

    private void initViews(View view) {
        RecipeAdapter recipeAdapter = new RecipeAdapter(applicationContext, recipeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(applicationContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_LST_recipe.setLayoutManager(linearLayoutManager);
        home_LST_recipe.setAdapter(recipeAdapter);
    }

    private void findViews(View view) {
        profile_SIV_image= view.findViewById(R.id.profile_SIV_image);
        profile_TV_name= view.findViewById(R.id.profile_TV_name);
        profile_TV_info= view.findViewById(R.id.profile_TV_info);
        home_LST_recipe= view.findViewById(R.id.home_LST_recipe);
    }
}