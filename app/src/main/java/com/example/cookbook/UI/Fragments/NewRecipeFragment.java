package com.example.cookbook.UI.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.cookbook.Adapters.IngredientAdapter;
import com.example.cookbook.Adapters.InstructionAdapter;
import com.example.cookbook.Interfaces.RecipeResetListener;
import com.example.cookbook.Logic.RecipeLogic;
import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.UUID;
import java.util.regex.Pattern;


public class NewRecipeFragment extends Fragment implements RecipeResetListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Context applicationContext;

    private MaterialTextView new_recipe_TV_title, new_recipe_TV_name, new_recipe_TV_ingredients
            ,new_recipe_TV_instructions, new_recipe_TV_image, new_recipe_TV_tags;
    private EditText new_recipe_ET_name, new_recipe_ET_ingredients_name, new_recipe_ET_ingredients_amount
            , new_recipe_ET_instructions, new_recipe_ET_tags;
    private ShapeableImageView new_recipe_SIV_image;
    private MaterialButton new_recipe_MBTN_submit, new_recipe_BTN_ingredients_add, new_recipe_BTN_instructions_add;
    private RecyclerView new_recipe_LST_ingredients, new_recipe_LST_instructions;
    private Spinner Ingredient_Spinner_Type;
    private RecipeLogic recipeLogic;
    private Recipe new_recipe;
    private String userID;
    private Uri imageUri;
    public NewRecipeFragment(Context Context, RecipeLogic recipeLogic, String userID) {
        this.applicationContext = Context;
        this.new_recipe = new Recipe();
        this.userID = userID;
        this.new_recipe.setAuthorId(userID);
        new_recipe.setId(UUID.randomUUID().toString());
        this.recipeLogic = recipeLogic;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        findViews(view);
        initViews(view);
        return view;
    }


    private void addIngredient(Ingredient ingredient){
        new_recipe.addIngredient(ingredient);
    }
    private void addInstruction(String instruction){
        new_recipe.addInstructions(instruction);
    }

    public boolean isNumeric(String str) {
        // Regular expression to match numeric values
        String regex = "-?\\d+(\\.\\d+)?";  // Allows for negative numbers and decimal points

        // Check if the string matches the regular expression
        return Pattern.matches(regex, str);
    }
    private void initViews(View view) {
        textCahngeListenerNew_recipe_ET_name();
        addListenerNew_recipe_BTN_instructions_add();
        addListenerNew_recipe_BTN_ingredients_add();
        setIngredientsViews();
        setInstructionViews();
        new_recipe_MBTN_submit.setOnClickListener(v -> submit_recipe());
        new_recipe_SIV_image.setOnClickListener(v -> openGalleryOrCamera());
        addSpinnerAdapter();
    }
    private void textCahngeListenerNew_recipe_ET_name(){
        new_recipe_ET_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                new_recipe.setTitle(s.toString());

            }
        });
    }
    private void addListenerNew_recipe_BTN_instructions_add() {
        new_recipe_BTN_instructions_add.setOnClickListener(v -> {
            if (new_recipe_ET_instructions.getText().length() > 0) {
                addInstruction(new_recipe_ET_instructions.getText().toString());
                setInstructionViews();
                new_recipe_ET_instructions.setText("");
            }
        });
    }
    private void addListenerNew_recipe_BTN_ingredients_add(){
        new_recipe_BTN_ingredients_add.setOnClickListener(v -> {
            int selectedItemPosition = Ingredient_Spinner_Type.getSelectedItemPosition();
            Ingredient.AMOUNT_TYPE selectedAmountType = Ingredient.AMOUNT_TYPE.values()[selectedItemPosition];

            if(new_recipe_ET_ingredients_name.getText().length() > 0 &&
                    new_recipe_ET_ingredients_amount.getText().length() > 0 &&
                    isNumeric(new_recipe_ET_ingredients_amount.getText().toString())){
                Ingredient temp_ingredient = new Ingredient()
                        .setName(new_recipe_ET_ingredients_name.getText().toString())
                        .setAmount(Double.parseDouble(new_recipe_ET_ingredients_amount.getText().toString()))
                        .setType(selectedAmountType);
                addIngredient(temp_ingredient);
                setIngredientsViews();
                new_recipe_ET_ingredients_name.setText("");
                new_recipe_ET_ingredients_amount.setText("");
            }
        });
    }
    private void addSpinnerAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(applicationContext,
                R.array.amount_types_array , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set ArrayAdapter to the Spinner
        Ingredient_Spinner_Type.setAdapter(adapter);
    }
    private void openGalleryOrCamera() {
        // Create an intent to open the gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Set the type of content to open
        galleryIntent.setType("image/*");

        // Create an intent to open the camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create a chooser intent to allow the user to select between gallery and camera
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
        // Add the camera intent to the chooser
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        // Start the activity with the chooser intent
        startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void submit_recipe() {
        recipeLogic.saveNewRecipe(new_recipe, imageUri, this);

    }
    @Override
    public  void resetRecipe() {
        // Clear EditText fields
        new_recipe_ET_name.setText("");
        new_recipe_ET_ingredients_name.setText("");
        new_recipe_ET_ingredients_amount.setText("");
        new_recipe_ET_instructions.setText("");
        //new_recipe_ET_tags.setText("");
        new_recipe = new Recipe();
        new_recipe.setAuthorId(userID);
        new_recipe.setId(UUID.randomUUID().toString());
        setIngredientsViews();
        setInstructionViews();
        new_recipe_SIV_image.setImageResource(R.drawable.default_recipe_image);
        new_recipe_SIV_image.setMinimumHeight(100);
    }


    private void setIngredientsViews(){
        IngredientAdapter ingredientAdapter = new IngredientAdapter(applicationContext, new_recipe.getIngredients());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(applicationContext);
        linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);
        new_recipe_LST_ingredients.setLayoutManager(linearLayoutManager1);
        new_recipe_LST_ingredients.setAdapter(ingredientAdapter);
    }

    private void setInstructionViews(){
        InstructionAdapter instructionAdapter = new InstructionAdapter(applicationContext, new_recipe.getInstructions());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(applicationContext);
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        new_recipe_LST_instructions.setLayoutManager(linearLayoutManager2);
        new_recipe_LST_instructions.setAdapter(instructionAdapter);
    }
    private void findViews(View view) {
        new_recipe_ET_name = view.findViewById(R.id.new_recipe_ET_name);
        new_recipe_ET_instructions = view.findViewById(R.id.new_recipe_ET_instructions);
        //new_recipe_ET_tags = view.findViewById(R.id.new_recipe_ET_tags);
        new_recipe_SIV_image = view.findViewById(R.id.new_recipe_SIV_image);
        new_recipe_MBTN_submit = view.findViewById(R.id.new_recipe_MBTN_submit);
        new_recipe_LST_ingredients = view.findViewById(R.id.new_recipe_LST_ingredients);
        new_recipe_LST_instructions = view.findViewById(R.id.new_recipe_LST_instructions);
        new_recipe_BTN_ingredients_add = view.findViewById(R.id.new_recipe_BTN_ingredients_add);
        new_recipe_BTN_instructions_add = view.findViewById(R.id.new_recipe_BTN_instructions_add);
        new_recipe_ET_ingredients_name = view.findViewById(R.id.new_recipe_ET_ingredients_name);
        new_recipe_ET_ingredients_amount = view.findViewById(R.id.new_recipe_ET_ingredients_amount);
        Ingredient_Spinner_Type = view.findViewById(R.id.Ingredient_Spinner_Type);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Handle the result when an image is successfully captured or selected from the gallery
            if (data != null) {
                // Get the image URI
                this.imageUri = data.getData();
                // Do something with the image URI, such as displaying it in the ImageView
                Glide.with(this)
                        .load(imageUri)
                        .fitCenter()
                        .placeholder(R.drawable.profile_icon1)
                        .into(new_recipe_SIV_image);
                new_recipe_SIV_image.setMinimumHeight(800);
                new_recipe.setPhotoUrl(imageUri.toString());
            }
        }
    }
}