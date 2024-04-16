package com.example.cookbook.UI;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.cookbook.DataBaseLayer.OnUserLoadedListener;
import com.example.cookbook.MainActivity;
import com.example.cookbook.Models.User;
import com.example.cookbook.R;
import com.example.cookbook.Utilities.SingleManager;
import com.example.cookbook.Utilities.UserManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements OnUserLoadedListener {

    private FirebaseAuth mAuth;
    private MaterialButton login_BTN_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initViews();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
            login();
        else {
            SingleManager.getInstance().getDBManager().getUser(this, user.getEmail());
        }
    }

    private void initViews() {
        login_BTN_signout.setOnClickListener(v ->{
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            login();
                        }
                    });
        });
    }

    private void findViews() {
        login_BTN_signout = findViewById(R.id.login_BTN_signout);
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

    private void login(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());//,
                //new AuthUI.IdpConfig.PhoneBuilder().build());
                //new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            SingleManager.getInstance().getDBManager().getUser(this, user.getEmail());
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();

        } else {
        }
    }

    private void onSignInResult3(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Check if user already exists
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.fetchSignInMethodsForEmail(user.getEmail())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().getSignInMethods().isEmpty()) {
                                // New user, create account
                                // (Handle new user creation logic)
                            } else {
                                // Existing user, log them in
                                // ... (Proceed with logged-in user operations)
                            }
                        } else {
                            // Handle error fetching sign-in methods
                        }
                    });
        } else {
            // Sign in failed handling
        }
    }

    @Override
    public void onUserLoaded(User user) {
        SingleManager.getInstance().getUserManager().setUser(user);
        Log.d("USER1", SingleManager.getInstance().getUserManager().getUser().toString());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserLoadFailed(Exception e) {

    }
}