package com.example.cookbook.UI;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.cookbook.Interfaces.OnUserLoadedListener;
import com.example.cookbook.Interfaces.OnFollowsListener;
import com.example.cookbook.Interfaces.OnUserSavedListener;
import com.example.cookbook.Interfaces.onFavoritesListener;
import com.example.cookbook.MainActivity;
import com.example.cookbook.Models.Favorites;
import com.example.cookbook.Models.Following;
import com.example.cookbook.Models.User;
import com.example.cookbook.R;
import com.example.cookbook.Utilities.SingleManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LoginActivity extends AppCompatActivity implements OnUserLoadedListener, OnUserSavedListener, OnFollowsListener, onFavoritesListener {

    private FirebaseAuth mAuth;
   // private MaterialButton login_BTN_signout;
    private MaterialButton login_BTN_signin, login_BTN_signup;
    private LinearLayout login_LLO_signin_form;
    private MaterialTextView login_TV_signin;
    private EditText login_ET_first_name, login_ET_last_name, login_ET_email, login_ET_password
            , login_ET_password_again, login_ET_login_email, login_ET_login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        findViews();
        initViews();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
            SingleManager.getInstance().getDBManager().getUser(this, user.getEmail());
    }

    private void initViews() {

        login_BTN_signin.setOnClickListener(v -> login() );
        login_TV_signin.setOnClickListener(v -> toggleSignInFormVisibility() );
        login_BTN_signup.setOnClickListener(v -> signup() );
        login_LLO_signin_form.setVisibility(View.GONE);
        login_TV_signin.setPaintFlags(login_TV_signin.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

    }


    private void findViews() {

        //login_BTN_signout = findViewById(R.id.login_BTN_signout);
        login_BTN_signin = findViewById(R.id.login_BTN_signin);
        login_BTN_signup = findViewById(R.id.login_BTN_signup);
        login_LLO_signin_form = findViewById(R.id.login_LLO_signin_form);
        login_TV_signin = findViewById(R.id.login_TV_signin);
        login_ET_first_name = findViewById(R.id.login_ET_first_name);
        login_ET_last_name = findViewById(R.id.login_ET_last_name);
        login_ET_email = findViewById(R.id.login_ET_email);
        login_ET_password = findViewById(R.id.login_ET_password);
        login_ET_password_again = findViewById(R.id.login_ET_password_again);
        login_ET_login_email = findViewById(R.id.login_ET_login_email);
        login_ET_login_password = findViewById(R.id.login_ET_login_password);
    }

    private void signup() {
        if(validSignupDetails()){

            setUserAuth().thenAccept(authenticated -> {
                if (authenticated) {
                    User new_user = new User()
                            .setEmail(login_ET_email.getText().toString())
                            .setFirstName(login_ET_first_name.getText().toString())
                            .setLastName(login_ET_last_name.getText().toString())
                            .setId(UUID.randomUUID().toString())
                            .setFollows(new Following());
                    SingleManager.getInstance().getDBManager().saveNewUser(new_user, this);
                } else {
                    Log.d("signup", "auth Fail");
                }
            });

        }

    }

    private CompletableFuture<Boolean> setUserAuth() {
        CompletableFuture<Boolean> authResult = new CompletableFuture<>();

        mAuth.createUserWithEmailAndPassword(login_ET_email.getText().toString(), login_ET_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            authResult.complete(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            authResult.complete(false);
                        }
                    }
                });
        return authResult;
    }

    private boolean validSignupDetails() {
        if(login_ET_first_name.getText().length() < 1 || login_ET_first_name.getText().length() > 20){
            SingleManager.getInstance().toast("First Name length must be between 1 - 20 ");
            return false;
        }
        if(login_ET_last_name.getText().length() < 1 || login_ET_last_name.getText().length() > 20){
            SingleManager.getInstance().toast("Last Name length must be between 1 - 20 ");
            return false;
        }
        if(login_ET_email.getText().length() < 1 || login_ET_email.getText().length() > 50){
            SingleManager.getInstance().toast("Email length must be between 1 - 50 ");
            return false;
        }
        if(login_ET_password.getText().length() < 6 || login_ET_password.getText().length() > 20){
            SingleManager.getInstance().toast("Email length must be between 1 - 50 ");
            return false;
        }
        if(login_ET_password_again.getText().length() < 6 || login_ET_password_again.getText().length() > 20){
            SingleManager.getInstance().toast("Email length must be between 1 - 50 ");
            return false;
        }
        if(!login_ET_password.getText().toString().equals(login_ET_password_again.getText().toString())){
            SingleManager.getInstance().toast("Passwords not the same ");
            return false;
        }

        return true;
    }

    private void toggleSignInFormVisibility() {
        if (login_LLO_signin_form.getVisibility() == View.VISIBLE) {
            login_LLO_signin_form.setVisibility(View.GONE); // Hide the form
        } else {
            login_LLO_signin_form.setVisibility(View.VISIBLE); // Show the form
        }
    }
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

//    private void login(){
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder()
//                        .setAllowNewAccounts(false)
//                        .build());//,
//                //new AuthUI.IdpConfig.PhoneBuilder().build());
//                //new AuthUI.IdpConfig.GoogleBuilder().build());
//
//// Create and launch sign-in intent
//        Intent signInIntent = AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setIsSmartLockEnabled(false)
//                .setAvailableProviders(providers)
//                .build();
//        signInLauncher.launch(signInIntent);
//    }
    private void login() {
        String email = login_ET_login_email.getText().toString();
        String password = login_ET_login_password.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                SingleManager.getInstance().getDBManager().getUser(LoginActivity.this, user.getEmail());
                                SingleManager.getInstance().toast("Authentication successful.");
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                SingleManager.getInstance().toast("Authentication failed.");
                            }
                        }
                    });
        } else {
            // Handle empty email or password fields
            SingleManager.getInstance().toast("Please enter email and password");

        }
    }
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            SingleManager.getInstance().getDBManager().getUser(this, user.getEmail());
        } else {
            Log.d("onSignInResult", "fail");
        }
    }

    @Override
    public void onUserLoaded(User user) {
        SingleManager.getInstance().getUserManager().setUser(user);
        SingleManager.getInstance().getDBManager().getFollowing(SingleManager.getInstance().getUserManager().getUser().getId(), this);

    }

    @Override
    public void onUserLoadFailed(Exception e) {

    }

    @Override
    public void onUserSaved(boolean success, User user) {
        if(success){
            SingleManager.getInstance().getUserManager().setUser(user);
            SingleManager.getInstance().getDBManager().getFollowing(SingleManager.getInstance().getUserManager().getUser().getId(), this);
        }
        else {
            Log.d("User Save", "Failed");
        }
    }

    @Override
    public void onFollowReady(boolean success, Following follows) {
        if(success){
            SingleManager.getInstance().getUserManager().getUser().setFollows(follows);
            SingleManager.getInstance().getDBManager().getFavorites(SingleManager.getInstance().getUserManager().getUser().getId(), this);

        }
        else {
            Log.d("FOLLOW", null);
        }
    }

    @Override
    public void onFavoritesReady(boolean success, Favorites favorites) {
        if (success){
            SingleManager.getInstance().getUserManager().getUser().setFavorites(favorites);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Log.d("Favorites", null);
        }
    }
}