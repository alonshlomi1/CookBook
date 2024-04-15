package com.example.cookbook.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookbook.Adapters.RecipeAdapter;
import com.example.cookbook.Interfaces.RefreshHomeListener;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.R;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {
    private RecyclerView home_LST_recipe;
    private SwipeRefreshLayout home_SWIPE_refresh;
    private Context applicationContext;
    private ArrayList<Recipe> recipeList;
    private RefreshHomeListener refreshCallback;


//    public HomePageFragment() {
//        // Required empty public constructor
//    }


    public HomePageFragment(Context Context, ArrayList<Recipe> recipeList, RefreshHomeListener refreshCallback) {
        //int limit = Math.min(recipeList.size(), 10);
        this.recipeList = recipeList;//(ArrayList<Recipe>) recipeList.subList(0, limit);
        applicationContext = Context;
        this.refreshCallback = refreshCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        findViews(view);
        initViews();

        return view;
    }
    private void initViews() {
        RecipeAdapter recipeAdapter = new RecipeAdapter(applicationContext, recipeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(applicationContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_LST_recipe.setLayoutManager(linearLayoutManager);
        home_LST_recipe.setAdapter(recipeAdapter);
        //highscoreAdapter.setHighscoreCallback(this.callbackHighScoreClicked);
        home_SWIPE_refresh.setOnRefreshListener(() -> refreshCallback.refresh(
//                () -> {
//            home_SWIPE_refresh.setRefreshing(false);
//        })
        ));
    }

//    private void refresh() {
//        Log.d("REFRESH-------", "Refresh");
//        home_SWIPE_refresh.setRefreshing(false);
//    }


    private void findViews(View view) {
        home_LST_recipe = view.findViewById(R.id.home_LST_recipe);
        home_SWIPE_refresh = view.findViewById(R.id.home_SWIPE_refresh);
    }
    public void updateRecipeList(ArrayList<Recipe> recipeList){
        this.recipeList = recipeList;
        initViews();
    }

}