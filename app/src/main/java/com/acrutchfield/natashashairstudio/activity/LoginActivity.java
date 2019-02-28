package com.acrutchfield.natashashairstudio.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.utils.GlideApp;
import com.acrutchfield.natashashairstudio.utils.SharedPrefs;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGN_IN = 0;
    private static final String SIGNED_OUT = "Signed Out";
    public static final String ACCOUNT_DELETED = "Account Deleted.";
    public static final String CANCELED = "Canceled.";
    public static final String TITLE_DELETE_ACCOUNT = "Delete Account";
    public static final String MESSAGE_DELETE_ACCOUNT = "Are you sure you want to delete your account?";
    public static final String CONFIRM = "Confirm";
    public static final String CANCEL = "Cancel";
    public static final String SIGN_IN_TO_DELETE = "You must be signed in to delete your account";
    public static final String SIGNED_IN = "Signed In";
    public static final String REMINDER_SET = "Appointment reminder set for 7 days.";
    public static final String REMINDER_REMOVED = "Appointment reminder removed.";
    public static final String COLOR_GREY = "#676767";

    private TextView tvWelcome;
    private TextView tvSignedOut;
    private TextView tvUsername;
    private ImageView ivLoginPicture;
    private ImageView ivPictureOutline;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);


        auth = FirebaseAuth.getInstance();

        tvWelcome = findViewById(R.id.tv_welcome_title);
        tvSignedOut = findViewById(R.id.tv_signed_out);
        tvUsername = findViewById(R.id.tv_username);

        TextView tvSignOut = findViewById(R.id.tv_sign_out);
        TextView tvDeleteAccount = findViewById(R.id.tv_delete_account);
        Button btnSignInGoogle = findViewById(R.id.btn_sign_in_google);
        ivLoginPicture = findViewById(R.id.iv_login_picture);
        ivPictureOutline = findViewById(R.id.iv_picture_outline);
        setupReminderSwitch();

        btnSignInGoogle.setOnClickListener(v -> signIn());
        tvSignOut.setOnClickListener(v -> signOut());

        tvDeleteAccount.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                promptForDeleteAccount();
            else
                Toast.makeText(LoginActivity.this, SIGN_IN_TO_DELETE, Toast.LENGTH_SHORT).show();
        });

        if (auth.getCurrentUser() != null) {
            // Update UI
            signedInUI();
        } else {
            signedOutUI();
        }
    }

    private void setupReminderSwitch() {
        Switch swAppointmentReminder = findViewById(R.id.sw_appointment_reminder);

        // Restore checked state before setting the listener
        if (SharedPrefs.getReminder(this)) swAppointmentReminder.setChecked(true);

        swAppointmentReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPrefs.setReminder(this, isChecked);

            if (isChecked) {
                Toast.makeText(LoginActivity.this, REMINDER_SET, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, REMINDER_REMOVED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signedOutUI() {
        tvWelcome.setVisibility(View.INVISIBLE);
        tvUsername.setVisibility(View.INVISIBLE);
        tvSignedOut.setVisibility(View.VISIBLE);

        GlideApp.with(this)
                .load(R.drawable.ic_account_circle_black_124dp)
                .apply(RequestOptions.circleCropTransform())
                .into(ivLoginPicture);

        int backgoundColor = Color.parseColor(COLOR_GREY);
        ColorDrawable drawable = new ColorDrawable(backgoundColor);

        GlideApp.with(this)
                .load(drawable)
                .apply(RequestOptions.circleCropTransform())
                .into(ivPictureOutline);

    }

    private void signedInUI() {
        tvSignedOut.setVisibility(View.INVISIBLE);
        tvWelcome.setVisibility(View.VISIBLE);

        tvUsername.setText(Objects.requireNonNull(
                auth.getCurrentUser()).getDisplayName());
        tvUsername.setVisibility(View.VISIBLE);


        GlideApp.with(this)
                .load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(ivLoginPicture);

        int backgoundColor = Color.parseColor(COLOR_GREY);
        ColorDrawable drawable = new ColorDrawable(backgoundColor);

        GlideApp.with(this)
                .load(drawable)
                .apply(RequestOptions.circleCropTransform())
                .into(ivPictureOutline);
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
                .addOnCompleteListener(task -> {
                    signedOutUI();
                    Toast.makeText(LoginActivity.this, SIGNED_OUT, Toast.LENGTH_SHORT).show();
                });
    }

    private void promptForDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle(TITLE_DELETE_ACCOUNT)
                .setMessage(MESSAGE_DELETE_ACCOUNT)
                .setPositiveButton(CONFIRM, (dialog, which) -> deleteAccount())
                .setNegativeButton(CANCEL, (dialog, which) ->
                        Toast.makeText(LoginActivity.this, CANCELED, Toast.LENGTH_SHORT).show())
                .create()
                .show();
    }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(task ->
                        Toast.makeText(LoginActivity.this, ACCOUNT_DELETED, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN && resultCode == RESULT_OK) {
            signedInUI();
            Toast.makeText(LoginActivity.this, SIGNED_IN, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.no_transition, R.anim.slide_down_animation);
            return true;
        }
        return false;
    }
}
