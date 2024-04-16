package com.example.cookbook.UI.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.cookbook.Adapters.RecipeAdapter;
import com.example.cookbook.MainActivity;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.User;
import com.example.cookbook.R;
import com.example.cookbook.UI.LoginActivity;
import com.example.cookbook.Utilities.SingleManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


public class UserProfileFragment extends Fragment {

    private ShapeableImageView profile_SIV_image;
    private MaterialTextView profile_TV_name, profile_TV_info;
    private RecyclerView home_LST_recipe;
    private MaterialButton profile_BTN_setting, profile_BTN_setting_image, profile_BTN_setting_info
    , profile_BTN_setting_logout;
    private LinearLayout profile_LLO_seting;
    private Context applicationContext;
    private ArrayList<Recipe> recipeList;
    private User user;
    private boolean expandedSetting = false;

    public UserProfileFragment(Context Context, User user, ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.applicationContext = Context;
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        findViews(view);
        initViews(view);
        return view;

    }

    private void updateUserInfo() {
        if(user != null){
            profile_TV_name.setText(user.getFirstName() + " " + user.getLastName());
            profile_TV_info.setText(user.getBio());
            Glide.with(this)
                    .load(user.getProfile_URL())
                    .centerCrop()
                    .placeholder(R.drawable.profile_icon)
                    .into(profile_SIV_image);
        }
    }

    private void initViews(View view) {
        updateUserInfo();
        RecipeAdapter recipeAdapter = new RecipeAdapter(applicationContext, recipeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(applicationContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_LST_recipe.setLayoutManager(linearLayoutManager);
        home_LST_recipe.setAdapter(recipeAdapter);
        profile_LLO_seting.setVisibility(View.GONE);

        profile_BTN_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedSetting =! expandedSetting;
                if (expandedSetting) {
                    profile_LLO_seting.setVisibility(View.VISIBLE);

                } else {
                    profile_LLO_seting.setVisibility(View.GONE);

                }
            }
        });
        profile_BTN_setting_image.setOnClickListener(v -> openGalleryOrCamera());
        profile_BTN_setting_logout.setOnClickListener(v ->{
            AuthUI.getInstance()
                    .signOut(applicationContext)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {

                            SingleManager.getInstance().getUserManager().setUser(null);
                            logout();
                            //login();
                        }
                    });
        });
    }
    private void logout(){
        Intent intent = new Intent(applicationContext, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
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
        startActivityForResult(chooserIntent, 1);
    }
    private void findViews(View view) {
        profile_SIV_image= view.findViewById(R.id.profile_SIV_image);
        profile_TV_name= view.findViewById(R.id.profile_TV_name);
        profile_TV_info= view.findViewById(R.id.profile_TV_info);
        home_LST_recipe= view.findViewById(R.id.home_LST_recipe);
        profile_BTN_setting = view.findViewById(R.id.profile_BTN_setting);
        profile_BTN_setting_image = view.findViewById(R.id.profile_BTN_setting_image);
        profile_BTN_setting_info = view.findViewById(R.id.profile_BTN_setting_info);
        profile_BTN_setting_logout = view.findViewById(R.id.profile_BTN_setting_logout);
        profile_LLO_seting = view.findViewById(R.id.profile_LLO_seting);
    }
    public void updateUser(User user){
        this.user = user;
    }
    public void updateRecipeList(ArrayList<Recipe> recipeList){
        this.recipeList = recipeList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // Set the selected image URI to the user's profile image
            user.setProfile_URL(selectedImageUri.toString());
            // Update the user's profile image in the UI
            Glide.with(this)
                    .load(selectedImageUri)
                    .centerCrop()
                    .placeholder(R.drawable.profile_icon)
                    .into(profile_SIV_image);
            SingleManager.getInstance().getDBManager().saveImage(user.getId(), SingleManager.getInstance().compresImage(selectedImageUri));
        }
    }
}