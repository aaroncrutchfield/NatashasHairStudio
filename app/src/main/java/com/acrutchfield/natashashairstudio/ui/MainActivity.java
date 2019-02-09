package com.acrutchfield.natashashairstudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.acrutchfield.natashashairstudio.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {


    // TODO: 2/7/19 Check if the user is signed in
    // TODO: 2/7/19 Prompt the user to sign in if they aren't already
    // TODO: 2/7/19 Find a way to show the logo by default if the user isn't signed in
    private static final int REQUEST_SIGN_IN = 0;
    public static final String SIGNED_OUT = "Signed Out";
    public static final String WELCOME_BACK = "Welcome Back!";
    public static final String LOGIN_FAILED = "Login failed!";
    private FragmentManager fragmentManager;
    FirebaseAuth auth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();

        // Check if the user is already logged in
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            signIn();
        } else {
            Toast.makeText(this, WELCOME_BACK, Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)
                        .setTheme(R.style.AppTheme)
                        .build(),
                REQUEST_SIGN_IN
        );
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, SIGNED_OUT, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                FirebaseUser user = auth.getCurrentUser();
                String welcomeUser = "Welcome " + user.getDisplayName() + "!";
                displayMessage(welcomeUser);
            } else {
                displayMessage(LOGIN_FAILED);
            }
        }
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
