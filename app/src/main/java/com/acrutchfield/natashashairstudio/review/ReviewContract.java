package com.acrutchfield.natashashairstudio.review;

import com.google.firebase.firestore.Query;

class ReviewContract {
    interface ViewInterface {
        void notifyUser(String message);
    }

    interface PresenterInterface {
        void addReview(String details);
        boolean isLoggedIn();
        String getUid();
        Query getQuery();
        void setReviewRating(String rating);
        void setReviewService(String service);
    }
}
