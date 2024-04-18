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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cookbook.Adapters.FollowersAdapter;
import com.example.cookbook.Adapters.FollowingAdapter;
import com.example.cookbook.Adapters.RecipeAdapter;
import com.example.cookbook.Interfaces.RefreshHomeListener;
import com.example.cookbook.MainActivity;
import com.example.cookbook.Models.Following;
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
    private RecyclerView home_LST_recipe, home_LST_following, home_LST_follower;
    private MaterialButton profile_BTN_setting, profile_BTN_setting_image, profile_BTN_setting_info
    , profile_BTN_setting_logout, profile_BTN_following, profile_BTN_followers;
    private LinearLayout profile_LLO_seting;
    private Context applicationContext;
    private ArrayList<Recipe> recipeList;
    private SwipeRefreshLayout profile_SWIPE_refresh;

    private User user;
    private boolean expandedSetting = false;
    private boolean expandedFollowing = false;
    private boolean expandedFollowers = false;
    private RefreshHomeListener refreshCallback;


    public UserProfileFragment(Context Context, User user, ArrayList<Recipe> recipeList, RefreshHomeListener refreshCallback) {
        this.recipeList = recipeList;
        this.applicationContext = Context;
        this.user = user;
        this.refreshCallback = refreshCallback;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        findViews(view);
        initViews();
        return view;

    }

    private void updateUserInfo() {
        if(user != null){
            profile_TV_name.setText(user.getFirstName() + " " + user.getLastName());
            profile_TV_info.setText(user.getBio());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(90));

            Glide.with(this)
                    .load(user.getProfile_URL())
                    .apply(requestOptions)
                    .placeholder(R.drawable.profile_icon)
                    .into(profile_SIV_image);
        }
    }

    private void initViews() {
        updateUserInfo();
        RecipeAdapter recipeAdapter = new RecipeAdapter(applicationContext, recipeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(applicationContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        home_LST_recipe.setLayoutManager(linearLayoutManager);
        home_LST_recipe.setAdapter(recipeAdapter);

//        Following following = new Following();
        FollowingAdapter followingAdapter = new FollowingAdapter(applicationContext, user.getFollows());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(applicationContext);
        linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);
        home_LST_following.setLayoutManager(linearLayoutManager1);
        home_LST_following.setAdapter(followingAdapter);

        FollowersAdapter followersAdapter = new FollowersAdapter(applicationContext, user.getFollows());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(applicationContext);
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        home_LST_follower.setLayoutManager(linearLayoutManager2);
        home_LST_follower.setAdapter(followersAdapter);

        profile_LLO_seting.setVisibility(View.GONE);
        home_LST_following.setVisibility(View.GONE);
        home_LST_follower.setVisibility(View.GONE);
        profile_SWIPE_refresh.setOnRefreshListener(() -> refreshCallback.refresh(profile_SWIPE_refresh));

        profile_BTN_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedSetting =! expandedSetting;
                if (expandedSetting) {
                    profile_LLO_seting.setVisibility(View.VISIBLE);
                    home_LST_following.setVisibility(View.GONE);
                    home_LST_follower.setVisibility(View.GONE);
                    expandedFollowing = false;
                    expandedFollowers = false;
                } else {
                    profile_LLO_seting.setVisibility(View.GONE);

                }
            }
        });
        profile_BTN_following.setText(profile_BTN_following.getText() + "\n" + user.getFollows().getFollowing().size());

        profile_BTN_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedFollowing =! expandedFollowing;
                if (expandedFollowing) {
                    home_LST_following.setVisibility(View.VISIBLE);
                    profile_LLO_seting.setVisibility(View.GONE);
                    home_LST_follower.setVisibility(View.GONE);
                    expandedFollowers = false;
                    expandedSetting = false;

                } else {
                    home_LST_following.setVisibility(View.GONE);

                }
            }
        });
        profile_BTN_followers.setText(profile_BTN_followers.getText() + "\n" + user.getFollows().getFollowers().size());
        profile_BTN_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedFollowers =! expandedFollowers;
                if (expandedFollowers) {
                    home_LST_follower.setVisibility(View.VISIBLE);
                    home_LST_following.setVisibility(View.GONE);
                    profile_LLO_seting.setVisibility(View.GONE);
                    expandedFollowing = false;
                    expandedSetting = false;

                } else {
                    home_LST_follower.setVisibility(View.GONE);

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
        home_LST_following = view.findViewById(R.id.home_LST_following);
        home_LST_follower = view.findViewById(R.id.home_LST_follower);
        profile_BTN_following = view.findViewById(R.id.profile_BTN_following);
        profile_BTN_followers = view.findViewById(R.id.profile_BTN_followers);
        profile_SWIPE_refresh = view.findViewById(R.id.profile_SWIPE_refresh);

    }
    public void updateUser(User user){
        this.user = user;
    }
    public void updateRecipeList(ArrayList<Recipe> recipeList){
        this.recipeList = recipeList;
        initViews();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // Set the selected image URI to the user's profile image
            user.setProfile_URL(selectedImageUri.toString());
            // Update the user's profile image in the UI

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(90));

            Glide.with(this)
                    .load(selectedImageUri)
                    .apply(requestOptions)
                    .placeholder(R.drawable.profile_icon)
                    .into(profile_SIV_image);
            SingleManager.getInstance().getDBManager().saveImage(user.getId(), SingleManager.getInstance().compresImage(selectedImageUri));
        }
    }
}