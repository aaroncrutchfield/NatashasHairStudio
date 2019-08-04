package com.acrutchfield.natashashairstudio.profile;

import android.content.Context;
import android.net.Uri;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginContract.PresenterInterface {

    private static final String ACCOUNT_DELETED = "Account Deleted.";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user;



    private LoginContract.ViewInterface viewInterface;

    LoginPresenter(LoginContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void deleteAccount(Context context) {
        AuthUI.getInstance()
                .delete(context)
                .addOnCompleteListener(task ->
                        viewInterface.notifyUser(ACCOUNT_DELETED));
    }

    @Override
    public boolean isLoggedIn() {
        boolean loggedIn = false;
        user = auth.getCurrentUser();
        if (user != null) loggedIn = true;
        return loggedIn;
    }

    @Override
    public String getUserDisplayName() {
        return user.getDisplayName();
    }

    @Override
    public Uri getUserPhotoUrl() {
        return user.getPhotoUrl();
    }
}
