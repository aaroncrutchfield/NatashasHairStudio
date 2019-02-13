package com.acrutchfield.natashashairstudio.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.acrutchfield.natashashairstudio.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {


    // TODO: 2/7/19 Check if the user is signed in
    // TODO: 2/7/19 Prompt the user to sign in if they aren't already
    // TODO: 2/7/19 Find a way to show the logo by default if the user isn't signed in
    private static final int REQUEST_SIGN_IN = 0;
    public static final String SIGNED_OUT = "Signed Out";
    public static final String SELECTED_FRAGMENT = "selected_fragment";
    private static final String URL_STRING = "https://app.acuityscheduling.com/schedule.php?owner=11362345";

    private FragmentManager fragmentManager;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_shop:
                        replaceFragment(ShopFragment.newInstance());
                        return true;
                    case R.id.navigation_book:
                        replaceFragment(BookAppointmentFragment.newInstance(URL_STRING));
                        return true;
                    case R.id.navigation_review:
                        replaceFragment(ReviewFragment.newInstance());
                        return true;
                    case R.id.navigation_social:
                        replaceFragment(SocialFragment.newInstance(null, null));
                        return true;
                }
                return false;
            };

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        navigation = findViewById(R.id.navigation);
        FloatingActionButton fabProfile = findViewById(R.id.fab_profile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            int selectedFragment = savedInstanceState.getInt(SELECTED_FRAGMENT);
            navigation.setSelectedItemId(selectedFragment);
        } else {
            navigation.setSelectedItemId(R.id.navigation_shop);
        }

        fabProfile.setOnClickListener(v -> {
            signIn();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(SELECTED_FRAGMENT, navigation.getSelectedItemId());
    }

    // TODO: 2/7/19 Setup signing in with Email
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
                .addOnCompleteListener(task ->
                        Toast.makeText(MainActivity.this, SIGNED_OUT, Toast.LENGTH_SHORT).show());
    }

}
