package com.example.cookbook.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    public void setupSpinner(Context context, Spinner spinner) {
        // Get the values of the AMOUNT_TYPE enum
        Ingredient.AMOUNT_TYPE[] amountTypes = Ingredient.AMOUNT_TYPE.values();
        String[] amountTypeNames = new String[amountTypes.length];

        // Convert enum values to string array
        for (int i = 0; i < amountTypes.length; i++) {
            amountTypeNames[i] = amountTypes[i].toString();
        }

        // Create ArrayAdapter using the enum values
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, amountTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set ArrayAdapter to the Spinner
        spinner.setAdapter(adapter);
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView Ingredient_MTV_Name, Ingredient_MTV_Amount, Ingredient_MTV_type;
        private Spinner  Ingredient_Spinner_Type;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            Ingredient_MTV_Name = itemView.findViewById(R.id.Ingredient_MTV_Name);
            Ingredient_MTV_Amount = itemView.findViewById(R.id.Ingredient_MTV_Amount);
            Ingredient_MTV_type = itemView.findViewById(R.id.Ingredient_MTV_type);
//            Ingredient_Spinner_Type = itemView.findViewById(R.id.Ingredient_Spinner_Type);
        }
        public void bind(Ingredient ingredient) {
            Ingredient_MTV_Name.setText(ingredient.getName());
            Ingredient_MTV_Amount.setText(ingredient.getAmount() + "");
            Ingredient_MTV_type.setText(ingredient.getType());
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
//                    R.array.amount_types_array , android.R.layout.simple_spinner_item);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//            // Set ArrayAdapter to the Spinner
//            Ingredient_Spinner_Type.setAdapter(adapter);

//            setupSpinner(context, Ingredient_Spinner_Type);
//
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                    R.array.dropdown_items_array, android.R.layout.simple_spinner_item);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(adapter);
//            Ingredient_MTV_Type.setText(ingredient.getType());
        }
    }
}
