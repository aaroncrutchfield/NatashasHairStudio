package com.acrutchfield.natashashairstudio.profile;

import android.content.Context;
import android.net.Uri;

class LoginContract {
    interface ViewInterface {
        void notifyUser(String message);
    }

    interface PresenterInterface {
        void deleteAccount(Context context);
        boolean isLoggedIn();
        String getUserDisplayName();
        Uri getUserPhotoUrl();
    }
}
