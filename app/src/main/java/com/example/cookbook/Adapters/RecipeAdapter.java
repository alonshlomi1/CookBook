package com.example.cookbook.Adapters;

// Inside your RecyclerView adapter

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> recipeList;
    private Context context;
    private int expandedPosition = -1;

    // Constructor and other methods
    public RecipeAdapter(Context context, ArrayList<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe);

        final boolean isExpanded = position == expandedPosition;
        holder.recipeIngredients.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.recipeInstructions.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.recipeCommentsTitle.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.recipeLLOSeg.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.recipeLSTComments.setVisibility(holder.commentsVisible ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    expandedPosition = -1; // Collapse the currently expanded item
                } else {
                    int prevExpandedPosition = expandedPosition; // Store the previous expanded position
                    expandedPosition = position; // Set the newly expanded position
                    notifyItemChanged(prevExpandedPosition); // Collapse the previous expanded item
                }
                notifyItemChanged(position); // Expand or collapse the clicked item
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImage, recipe_comments_SIV_icon;
        private TextView recipeTitle, recipeIngredients, recipeInstructions, recipeCommentsTitle;
        private RatingBar recipeRating;
        private RecyclerView recipeLSTComments;
        private RelativeLayout recipeLLOSeg;
        private boolean commentsVisible = false;

        private void styleComments(){

            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
            backgroundDrawable.setCornerRadius(20); // Larger corner radius
            backgroundDrawable.setColor(ContextCompat.getColor(context, R.color.theme_3));
            backgroundDrawable.setStroke(1, ContextCompat.getColor(context, R.color.theme_5)); // Black border

            // Set the background drawable to the relative layout
            recipeLLOSeg.setBackground(backgroundDrawable);
        }
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeRating = itemView.findViewById(R.id.recipe_rating);
            recipeIngredients = itemView.findViewById(R.id.recipe_ingredients);
            recipeInstructions = itemView.findViewById(R.id.recipe_instructions);
            recipeCommentsTitle = itemView.findViewById(R.id.recipe_comments_title);
            recipeLSTComments = itemView.findViewById(R.id.recipe_LST_comments);
            recipeLLOSeg = itemView.findViewById(R.id.recipe_RLO_seg);
            styleComments();
            recipe_comments_SIV_icon = itemView.findViewById(R.id.recipe_comments_SIV_icon);
            recipeLLOSeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle comments visibility

                    if (commentsVisible) {
                        recipeLSTComments.setVisibility(View.VISIBLE);
                        recipe_comments_SIV_icon.setImageResource(R.drawable.minus_icon);
                    } else {
                        recipeLSTComments.setVisibility(View.GONE);
                        recipe_comments_SIV_icon.setImageResource(R.drawable.plus_icon);
                    }
                    commentsVisible = !commentsVisible;
                }
            });
        }

        public void bind(Recipe recipe) {
            // Bind data to views
            recipeTitle.setText(recipe.getTitle());
            recipeRating.setNumStars(5);
            recipeRating.setRating(recipe.getAVGRating());
            StringBuilder ingredientsBuilder = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredientsBuilder.append(ingredient.getName()).append(": ").append(ingredient.getAmount()).append("\n");
            }
            recipeIngredients.setText(ingredientsBuilder.toString());
            StringBuilder instructionsBuilder = new StringBuilder();
            for (String instruction : recipe.getInstructions()) {
                instructionsBuilder.append(instruction).append("\n");
            }
            recipeInstructions.setText(instructionsBuilder.toString());
            Log.d("@@@@@@@", String.valueOf(recipe.getComments()));
            CommentAdapter commentAdapter = new CommentAdapter(context, recipe.getComments());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recipeLSTComments.setLayoutManager(linearLayoutManager);
            recipeLSTComments.setAdapter(commentAdapter);
            //recipeLSTComments.setAdapter(commentAdapter);
            recipeLSTComments.setVisibility(View.GONE);
            commentsVisible = false;
        }
    }
}
