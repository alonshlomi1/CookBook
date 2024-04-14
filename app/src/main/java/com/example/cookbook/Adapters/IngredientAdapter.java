package com.example.cookbook.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{

    private ArrayList<Ingredient> ingredientList;
    private Context context;

    public IngredientAdapter(Context context) {
        this.context = context;
        this.ingredientList = new ArrayList<>();
    }
    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView Ingredient_MTV_Name, Ingredient_MTV_Amount, Ingredient_MTV_Type;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            Ingredient_MTV_Name = itemView.findViewById(R.id.Ingredient_MTV_Name);
            Ingredient_MTV_Amount = itemView.findViewById(R.id.Ingredient_MTV_Amount);
            Ingredient_MTV_Type = itemView.findViewById(R.id.Ingredient_MTV_Type);
        }
        public void bind(Ingredient ingredient) {
            Ingredient_MTV_Name.setText(ingredient.getName());
            Ingredient_MTV_Amount.setText(ingredient.getAmount() + "");
            Ingredient_MTV_Type.setText(ingredient.getType());
        }
    }
}
