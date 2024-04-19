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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cookbook.DataBaseLayer.DBManager;
import com.example.cookbook.DataBaseLayer.UserDB;
import com.example.cookbook.Models.Comment;
import com.example.cookbook.Models.Favorites;
import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.R;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        holder.recipe_BTN_seg.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.recipeLSTComments.setVisibility(holder.commentsVisible ? View.VISIBLE : View.GONE);
        holder.recipe_BTN_seg.setIconResource(holder.commentsVisible? R.drawable.minus_icon: R.drawable.plus_icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    expandedPosition = -1;
                } else {
                    int prevExpandedPosition = expandedPosition;
                    expandedPosition = position;
                    notifyItemChanged(prevExpandedPosition);
                }
                notifyItemChanged(position);
            }
        });
    }
    private void setItemOnClick(@NonNull RecipeViewHolder holder, boolean isExpanded, int position){
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
        private Recipe recipe;
        private ImageView recipeImage, recipe_comments_SIV_icon, home_SIV_comments_icon;
        private TextView recipeTitle, recipe_date, recipeIngredients, recipeInstructions, recipeCommentsTitle;
        private RatingBar recipeRating;
        private RecyclerView recipeLSTComments;
        private RelativeLayout recipeLLOSeg;
        private LinearLayout home_LLO_comments_title, home_LLO_comments, recipe_rating_clickable;
        private EditText home_ET_comments;
        private MaterialButton home_BTN_comments, recipe_BTN_seg, recipe_BTN_add_comment;
        private boolean commentsVisible = false;
        private boolean newCommentsVisible = false;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipe_date = itemView.findViewById(R.id.recipe_date);
            recipeRating = itemView.findViewById(R.id.recipe_rating);
            recipeIngredients = itemView.findViewById(R.id.recipe_ingredients);
            recipeInstructions = itemView.findViewById(R.id.recipe_instructions);
            recipeLSTComments = itemView.findViewById(R.id.recipe_LST_comments);
            //home_LLO_comments_title = itemView.findViewById(R.id.home_LLO_comments_title);
            home_LLO_comments = itemView.findViewById(R.id.home_LLO_comments);
            home_ET_comments = itemView.findViewById(R.id.home_ET_comments);
            home_BTN_comments = itemView.findViewById(R.id.home_BTN_comments);
            //home_SIV_comments_icon = itemView.findViewById(R.id.home_SIV_comments_icon);
            recipe_rating_clickable = itemView.findViewById(R.id.recipe_rating_clickable);
            recipe_BTN_seg = itemView.findViewById(R.id.recipe_BTN_seg);
            recipe_BTN_add_comment = itemView.findViewById(R.id.recipe_BTN_add_comment);
            setListenerRecipeLLOSeg();
            setListenerHome_LLO_comments_title();
            setListenerHome_BTN_comments();
            setListenerRecipe_rating_clickable();
        }

        private void setListenerRecipeLLOSeg(){
            recipe_BTN_seg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (commentsVisible) {
                        recipe_BTN_add_comment.setVisibility(View.VISIBLE);
                        home_LLO_comments.setVisibility(View.VISIBLE);
                        recipeLSTComments.setVisibility(View.VISIBLE);
                        recipe_BTN_seg.setIconResource(R.drawable.minus_icon);
                    } else {
                        recipe_BTN_add_comment.setVisibility(View.GONE);
                        home_LLO_comments.setVisibility(View.GONE);
                        recipeLSTComments.setVisibility(View.GONE);
                        recipe_BTN_seg.setIconResource(R.drawable.plus_icon);
                        home_ET_comments.setVisibility(View.GONE);
                        home_BTN_comments.setVisibility(View.GONE);
                        recipe_BTN_add_comment.setIconResource(R.drawable.plus_icon);
                    }
                    commentsVisible = !commentsVisible;
                }
            });
        }
        private void setListenerHome_LLO_comments_title(){
            recipe_BTN_add_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle comments visibility
                    if (newCommentsVisible) {
                        home_ET_comments.setVisibility(View.VISIBLE);
                        home_BTN_comments.setVisibility(View.VISIBLE);
                        recipe_BTN_add_comment.setIconResource(R.drawable.minus_icon);
                    } else {
                        home_ET_comments.setVisibility(View.GONE);
                        home_BTN_comments.setVisibility(View.GONE);
                        recipe_BTN_add_comment.setIconResource(R.drawable.plus_icon);
                    }
                    newCommentsVisible = !newCommentsVisible;
                }
            });
        }
        private void setListenerHome_BTN_comments(){
            home_BTN_comments.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    String commentText = home_ET_comments.getText().toString().trim();
                    User user = SingleManager.getInstance().getUserManager().getUser();
                    Recipe recipe = recipeList.get(getAdapterPosition());
                    if (!commentText.isEmpty()) {
                        Comment newComment = new Comment()
                                .setUserId(user.getEmail())
                                .setUserName(user.getFirstName()+" "+ user.getLastName())
                                .setComment(commentText)
                                .setRecipeId(recipe.getId());;

                        // Add the new comment to the recipe
                        recipe.addComment(newComment);
                        DBManager db = new DBManager();
                        db.updateRecipe(recipe);
                        // Notify the adapter of the data change
                        notifyItemChanged(getAdapterPosition());
                        // Clear the EditText
                        home_ET_comments.setText("");
                    }
                }
            });
        }
        private void setListenerRecipe_rating_clickable(){
            recipe_rating_clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Favorites favorites = SingleManager.getInstance().getUserManager().getUser().getFavorites();
                    if(recipeRating.getRating() == 1){
                        favorites.getFavoritesId().remove(recipe.getId());
                        recipeRating.setRating(0);
                    }
                    else {
                        favorites.getFavoritesId().add(recipe.getId());
                        recipeRating.setRating(1);
                    }
                    SingleManager.getInstance().getUserManager().getUser().setFavorites(favorites);
                    SingleManager.getInstance().getDBManager().saveFavorites();

                }
            });
        }

        public void bind(Recipe recipe) {
            this.recipe = recipe;
            recipeTitle.setText(recipe.getTitle());
            setDateFormat();
            setRecipeRating();
            setRecipeIngredients();
            setRecipeInstructions();
            setCommentsList(recipe);
            setRecipeImage();
            setVisible();
        }

        private void setDateFormat(){
            Date date = recipe.getDate().toDate();
            // Format the Date using SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm - dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(date);
            recipe_date.setText(formattedDate);
        }
        private void setRecipeRating(){
            recipeRating.setNumStars(1);
            ArrayList<String> favoritesIdList = SingleManager.getInstance().getUserManager().getUser().getFavorites().getFavoritesId();
            if(favoritesIdList.contains(recipe.getId()))
                recipeRating.setRating(1);
            else
                recipeRating.setRating(0);
        }
        private void setRecipeIngredients(){
            StringBuilder ingredientsBuilder = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredientsBuilder.append(ingredient.getName()).append(": ").append(ingredient.getAmount()).append("\n");
            }
            recipeIngredients.setText(ingredientsBuilder.toString());
        }
        private void setRecipeInstructions(){
            StringBuilder instructionsBuilder = new StringBuilder();
            for (String instruction : recipe.getInstructions()) {
                instructionsBuilder.append(instruction).append("\n");
            }
            recipeInstructions.setText(instructionsBuilder.toString());
        }
        private void setCommentsList(Recipe recipe){
            CommentAdapter commentAdapter = new CommentAdapter(context, recipe.getComments());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recipeLSTComments.setLayoutManager(linearLayoutManager);
            recipeLSTComments.setAdapter(commentAdapter);
        }
        private void setVisible(){
            recipeLSTComments.setVisibility(View.GONE);
            recipe_BTN_add_comment.setVisibility(View.GONE);
            home_LLO_comments.setVisibility(View.GONE);
            home_ET_comments.setVisibility(View.GONE);
            home_BTN_comments.setVisibility(View.GONE);
            commentsVisible = false;
        }
        private void setRecipeImage(){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(90));
            Glide.with(context)
                    .load(recipe.getPhotoUrl())
                    .apply(requestOptions)
                    .placeholder(R.drawable.default_recipe_image)
                    .into(recipeImage);
        }
    }
}
