package com.acrutchfield.natashashairstudio.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.acrutchfield.natashashairstudio.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {


    // TODO: 2/7/19 Check if the user is signed in
    // TODO: 2/7/19 Prompt the user to sign in if they aren't already
    // TODO: 2/7/19 Find a way to show the logo by default if the user isn't signed in
    private static final int REQUEST_SIGN_IN = 0;
    public static final String SIGNED_OUT = "Signed Out";
    private TextView mTextMessage;
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    replaceFragment(ShopFragment.newInstance());
                    return true;
                case R.id.navigation_book:
                    replaceFragment(BookAppointmentFragment.newInstance(urlString));
                    return true;
                case R.id.navigation_review:
                    replaceFragment(ReviewFragment.newInstance());
                    return true;
                case R.id.navigation_social:
                    replaceFragment(SocialFragment.newInstance(null, null));
                    return true;
            }
            return false;
        }
    };
    private String urlString = "https://app.acuityscheduling.com/schedule.php?owner=11362345";

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
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
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, SIGNED_OUT, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
