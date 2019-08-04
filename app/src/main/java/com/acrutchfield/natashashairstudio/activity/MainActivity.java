package com.acrutchfield.natashashairstudio.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.book.BookAppointmentFragment;
import com.acrutchfield.natashashairstudio.profile.LoginActivity;
import com.acrutchfield.natashashairstudio.review.ReviewFragment;
import com.acrutchfield.natashashairstudio.shop.ShopFragment;
import com.acrutchfield.natashashairstudio.social.SocialFragment;
import com.acrutchfield.natashashairstudio.utils.AppointmentReminderTask;
import com.acrutchfield.natashashairstudio.utils.AppointmentReminderUtils;
import com.acrutchfield.natashashairstudio.utils.SharedPrefs;
import com.acrutchfield.natashashairstudio.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    private static final String SELECTED_FRAGMENT = "selected_fragment";
    private static final String URL_STRING = "https://app.acuityscheduling.com/schedule.php?owner=11362345";
    private static final String ACTION_SHOP = "shop";
    private static final String ACTION_BOOK = "book";
    private static final String ACTION_REVIEW = "review";
    private static final String ACTION_SOCIAL = "social";
    private static final String OFFLINE = "Offline";
    private static final String MESSAGE_NETWORK_WARNING = "You must be online to use this app.";
    private static final String NETWORK_SETTINGS = "Network Settings";
    private static final String EXIT = "Exit";
    public static final String TRY_AGAIN_LATER = "Please connect to a network and try again later.";

    private FragmentManager fragmentManager;
    private BottomNavigationView navigation;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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

        checkConnectionStatus();

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
                case ACTION_SHOP:
                    navigation.setSelectedItemId(R.id.navigation_shop);
                    break;
                case ACTION_BOOK:
                case AppointmentReminderTask.ACTION_APPOINTMENT_REMINDER:
                    navigation.setSelectedItemId(R.id.navigation_book);
                    break;
                case ACTION_REVIEW:
                    navigation.setSelectedItemId(R.id.navigation_review);
                    break;
                case ACTION_SOCIAL:
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

    private boolean checkConnectionStatus() {
        //Check the connection status
        boolean connected = Utils.isOnline(this);
        //If there is no network connection, alert the user and prompt for next action
        if (!connected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle(OFFLINE)
                    .setMessage(MESSAGE_NETWORK_WARNING)
                    .setPositiveButton(NETWORK_SETTINGS, (dialogInterface, i) ->
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                    .setNegativeButton(EXIT, (dialogInterface, i) -> {
                        Toast.makeText(MainActivity.this, TRY_AGAIN_LATER, Toast.LENGTH_SHORT).show();
                        finish();
                    });
            final AlertDialog dialog = alert.create();
            dialog.show();
        }
        return connected;
    }

}
