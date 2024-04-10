package com.example.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cookbook.Models.Ingredient;
import com.example.cookbook.Models.Rate;
import com.example.cookbook.Models.Recipe;
import com.example.cookbook.Models.Comment;
import com.example.cookbook.Models.User;
import com.example.cookbook.UI.Fragments.HomePageFragment;
import com.example.cookbook.UI.Fragments.NewRecipeFragment;
import com.example.cookbook.UI.Fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private User user = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMockupList();
        setMockupUser();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
//        bottomNavigationView.setItemActiveIndicatorColor(getColorStateList(R.color.theme_6));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomePageFragment(getApplicationContext(), recipeList))
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.navigation_home)
                        selectedFragment = new HomePageFragment(getApplicationContext(), recipeList);
                    else if (item.getItemId() == R.id.navigation_profile)
                        selectedFragment = new UserProfileFragment(getApplicationContext(), user,  recipeList);
                    else if (item.getItemId() == R.id.new_recipe)
                        selectedFragment = new NewRecipeFragment();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();

                    return true;
                }
            };


    private void setMockupUser() {
        user.setId("userID_1")
                .setUsername("User1")
                .setFirstName("First")
                .setLastName("Last")
                .setBio("hi..................................\nby....................")
                .setEmail("email@gmail.com")
                .setProfile_URL("http://www.gravatar.com/avatar/3b3be63a4c2a439b013787725dfce802?d=identicon\n");
    }
    private void setMockupList(){
        // Recipe 1
        Recipe recipe1 = new Recipe();
        recipe1.setId("1")
                .setTitle("Spaghetti Carbonara")
                .setPhotoUrl("https://example.com/spaghetti_carbonara.jpg")
                .setAuthorId("author123");
        ArrayList<String> categoriesTags1 = new ArrayList<>();
        categoriesTags1.add("Italian");
        categoriesTags1.add("Pasta");
        recipe1.setCategoriesTags(categoriesTags1);

        ArrayList<Ingredient> ingredients1 = new ArrayList<>();
        ingredients1.add(new Ingredient().setName("Spaghetti").setAmount(200).setRecipeId("1"));
        ingredients1.add(new Ingredient().setName("Eggs").setAmount(2).setRecipeId("1"));
        ingredients1.add(new Ingredient().setName("Bacon").setAmount(100).setRecipeId("1"));
        ingredients1.add(new Ingredient().setName("Parmesan Cheese").setAmount(50).setRecipeId("1"));
        recipe1.setIngredients(ingredients1);

        ArrayList<String> instructions1 = new ArrayList<>();
        instructions1.add("Cook spaghetti according to package instructions.");
        instructions1.add("In a bowl, whisk together eggs and grated Parmesan cheese.");
        instructions1.add("In a skillet, cook bacon until crispy, then remove from pan and chop into pieces.");
        instructions1.add("Add cooked spaghetti to skillet with bacon fat, toss to coat.");
        instructions1.add("Remove skillet from heat, quickly stir in egg and cheese mixture, tossing to coat pasta evenly.");
        instructions1.add("Serve immediately, topping with additional Parmesan cheese and black pepper if desired.");
        recipe1.setInstructions(instructions1);


        ArrayList<Rate> rate1 = new ArrayList<>();
        rate1.add(new Rate().setRate(4).setRecipeId("1"));
        rate1.add(new Rate().setRate(1).setRecipeId("1"));
        rate1.add(new Rate().setRate(5).setRecipeId("1"));
        recipe1.setRatings(rate1);

        ArrayList<Comment> comments1 = new ArrayList<>();
        comments1.add(new Comment().setUserId("user1").setComment("This recipe is delicious!").setRecipeId("1"));
        comments1.add(new Comment().setUserId("user2").setComment("I love this dish, it's my favorite!, I love this dish, it's my favorite!, I love this dish, it's my favorite!, I love this dish, it's my favorite!, I love this dish, it's my favorite!, I love this dish, it's my favorite!, I love this dish, it's my favorite!").setRecipeId("1"));
        comments1.add(new Comment().setUserId("user2").setComment("I love this dish, it's my favorite!").setRecipeId("1"));
        recipe1.setComments(comments1);

        recipeList.add(recipe1);

// Recipe 2
        Recipe recipe2 = new Recipe();
        recipe2.setId("2")
                .setTitle("Chicken Alfredo")
                .setPhotoUrl("https://example.com/chicken_alfredo.jpg")
                .setAuthorId("author456");

        ArrayList<String> categoriesTags2 = new ArrayList<>();
        categoriesTags2.add("Italian");
        categoriesTags2.add("Chicken");
        recipe2.setCategoriesTags(categoriesTags2);

        ArrayList<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(new Ingredient().setName("Fettuccine").setAmount(250).setRecipeId("2"));
        ingredients2.add(new Ingredient().setName("Chicken Breast").setAmount(300).setRecipeId("2"));
        ingredients2.add(new Ingredient().setName("Heavy Cream").setAmount(200).setRecipeId("2"));
        ingredients2.add(new Ingredient().setName("Garlic").setAmount(2).setRecipeId("2"));
        recipe2.setIngredients(ingredients2);

        ArrayList<String> instructions2 = new ArrayList<>();
        instructions2.add("Cook fettuccine according to package instructions.");
        instructions2.add("Season chicken breast with salt, pepper, and any desired herbs.");
        instructions2.add("In a skillet, cook chicken until golden brown and cooked through. Set aside.");
        instructions2.add("In the same skillet, add minced garlic and cook until fragrant.");
        instructions2.add("Pour in heavy cream and let it simmer until slightly thickened.");
        instructions2.add("Add cooked fettuccine to the skillet, toss to coat in the sauce.");
        instructions2.add("Slice cooked chicken breast and serve over the fettuccine.");
        recipe2.setInstructions(instructions2);

        ArrayList<Rate> rate2 = new ArrayList<>();
        rate2.add(new Rate().setRate(4).setRecipeId("2"));
        rate2.add(new Rate().setRate(3).setRecipeId("2"));
        rate2.add(new Rate().setRate(5).setRecipeId("2"));
        recipe2.setRatings(rate2);

        ArrayList<Comment> comments2 = new ArrayList<>();
        comments2.add(new Comment().setUserId("user3").setComment("This chicken alfredo is amazing!").setRecipeId("2"));
        comments2.add(new Comment().setUserId("user4").setComment("I'm going to try making this for dinner tonight!").setRecipeId("2"));
        recipe2.setComments(comments2);

        recipeList.add(recipe2);
    }
}