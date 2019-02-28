package com.acrutchfield.natashashairstudio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.fragment.BookAppointmentFragment;
import com.acrutchfield.natashashairstudio.fragment.ReviewFragment;
import com.acrutchfield.natashashairstudio.fragment.ShopFragment;
import com.acrutchfield.natashashairstudio.fragment.SocialFragment;
import com.acrutchfield.natashashairstudio.utils.AppointmentReminderTask;
import com.acrutchfield.natashashairstudio.utils.AppointmentReminderUtils;
import com.acrutchfield.natashashairstudio.utils.SharedPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {


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
                        replaceFragment(SocialFragment.newInstance());
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

        if (SharedPrefs.getReminder(this)) {
            AppointmentReminderUtils.scheduleAppointmentReminder(this);
        } else {
            AppointmentReminderUtils.cancelAppointmentReminder(this);
        }

        handleIntent();

        fabProfile.setOnClickListener(v -> launchProfileActivity());

    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            assert action != null;
            switch (action) {
                case "shop":
                    navigation.setSelectedItemId(R.id.navigation_shop);
                    break;
                case "book":
                case AppointmentReminderTask.ACTION_APPOINTMENT_REMINDER:
                    navigation.setSelectedItemId(R.id.navigation_book);
                    break;
                case "review":
                    navigation.setSelectedItemId(R.id.navigation_review);
                    break;
                case "social":
                    navigation.setSelectedItemId(R.id.navigation_social);
                    break;
            }
        }
    }

    private void launchProfileActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(SELECTED_FRAGMENT, navigation.getSelectedItemId());
    }

}
