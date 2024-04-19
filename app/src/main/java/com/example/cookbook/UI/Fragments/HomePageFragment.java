package com.example.cookbook.UI.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookbook.Adapters.RecipeAdapter;
import com.example.cookbook.Interfaces.OnRecipesLoadedListener;
import com.example.cookbook.Interfaces.OnRecipesURLLoadedListener;
import com.example.cookbook.Interfaces.RefreshHomeListener;
import com.example.cookbook.Logic.RecipeLogic;
import com.example.cookbook.MainActivity;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.R;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.material.textview.MaterialTextView;

import java.net.URL;
import java.util.ArrayList;

public class HomePageFragment extends Fragment {
    private RecyclerView home_LST_recipe;
    private SwipeRefreshLayout home_SWIPE_refresh;
    private Context applicationContext;
    private ArrayList<Recipe> recipeList;
    private RefreshHomeListener refreshCallback;
    private MaterialTextView home_MTV_gen_segment, home_MTV_follow_segment, home_MTV_favorite_segment, current_segment;
    private RecipeLogic recipeLogic;

    public static boolean isLoading = false;
    private int scrollPosition = 0;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.OnScrollListener paginationScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            scrollPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (!isLoading && !recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                isLoading = true;
                if(current_segment == home_MTV_gen_segment)
                    recipeLogic.setRecipeListFromDB();
                if(current_segment == home_MTV_follow_segment)
                    recipeLogic.setFollowingRecipeListFromDB();
                if(current_segment == home_MTV_favorite_segment)
                    recipeLogic.setFavoriteRecipeListFromDB();
            }
        }

    };

    public HomePageFragment(Context Context, ArrayList<Recipe> recipeList, RefreshHomeListener refreshCallback, RecipeLogic recipeLogic) {
        this.recipeList = recipeList;
        applicationContext = Context;
        this.refreshCallback = refreshCallback;
        this.recipeLogic = recipeLogic;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        findViews(view);
        initViews();

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Save the current scroll position
        scrollPosition = linearLayoutManager.findFirstVisibleItemPosition();
    }


    private void initViews() {
        updateSegmentState(home_MTV_gen_segment);

        home_MTV_gen_segment.setOnClickListener(v -> {
            if(current_segment != home_MTV_gen_segment){
                RecipeLogic.recipeList = new ArrayList<>();
                updateRecipeList(new ArrayList<>());
            }
            updateSegmentState(home_MTV_gen_segment);
            scrollPosition = 0;
            recipeLogic.setRecipeListFromDB();
        });
        home_MTV_follow_segment.setOnClickListener(v -> {
            if(current_segment != home_MTV_follow_segment){
                RecipeLogic.recipeList = new ArrayList<>();
                updateRecipeList(new ArrayList<>());
            }
            updateSegmentState(home_MTV_follow_segment);
            scrollPosition = 0;
            recipeLogic.setFollowingRecipeListFromDB();
        });
        home_MTV_favorite_segment.setOnClickListener(v -> {
            if(current_segment != home_MTV_favorite_segment){
                RecipeLogic.recipeList = new ArrayList<>();
                updateRecipeList(new ArrayList<>());
            }
            updateSegmentState(home_MTV_favorite_segment);
            scrollPosition = 0;
            recipeLogic.setFavoriteRecipeListFromDB();

        });
        setRecipeAdapter();
        home_SWIPE_refresh.setOnRefreshListener(() -> refreshCallback.refresh(home_SWIPE_refresh));
        home_LST_recipe.addOnScrollListener(paginationScrollListener);

        //home_MTV_gen_segment
    }
    private void setRecipeAdapter(){
        RecipeAdapter recipeAdapter = new RecipeAdapter(applicationContext, recipeList);
        linearLayoutManager = new LinearLayoutManager(applicationContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_LST_recipe.setLayoutManager(linearLayoutManager);
        home_LST_recipe.setAdapter(recipeAdapter);
        linearLayoutManager.scrollToPosition(scrollPosition);

    }
    private void updateSegmentState(MaterialTextView textView) {
        // Reset all segments to default state
        current_segment = textView;
        home_MTV_gen_segment.setBackgroundColor(Color.TRANSPARENT);
        home_MTV_gen_segment.setTypeface(null, Typeface.NORMAL);
        home_MTV_follow_segment.setBackgroundColor(Color.TRANSPARENT);
        home_MTV_follow_segment.setTypeface(null, Typeface.NORMAL);
        home_MTV_favorite_segment.setBackgroundColor(Color.TRANSPARENT);
        home_MTV_favorite_segment.setTypeface(null, Typeface.NORMAL);

        // Set the selected segment to pressed state
        textView.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.theme_1));
        textView.setTypeface(null, Typeface.BOLD);
    }
    private void findViews(View view) {
        home_LST_recipe = view.findViewById(R.id.home_LST_recipe);
        home_SWIPE_refresh = view.findViewById(R.id.home_SWIPE_refresh);
        home_MTV_gen_segment = view.findViewById(R.id.home_MTV_gen_segment);
        home_MTV_follow_segment = view.findViewById(R.id.home_MTV_follow_segment);
        home_MTV_favorite_segment = view.findViewById(R.id.home_MTV_favorite_segment);
    }
    public void updateRecipeList(ArrayList<Recipe> recipeList){
        this.recipeList = recipeList;
        setRecipeAdapter();
    }


}