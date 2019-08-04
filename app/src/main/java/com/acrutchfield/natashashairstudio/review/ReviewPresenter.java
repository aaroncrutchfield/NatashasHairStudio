package com.acrutchfield.natashashairstudio.review;

import com.acrutchfield.natashashairstudio.model.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ReviewPresenter implements ReviewContract.PresenterInterface{
    private FirebaseUser user;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reviewsRef = db.collection("/REVIEWS");
    private ReviewContract.ViewInterface viewInterface;
    private Review.Builder reviewBuilder = new Review.Builder();

    private static final String MESSAGE_FEEDBACK = "We appreciate your feedback!";
    private static final String CHECK_NETWORK = "Failure. Check your network connection.";
    private static final String FORMAT_DATE = "MM/dd/yyyy";
    private static final String DATE = "date";


    ReviewPresenter(ReviewContract.ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void addReview(String details) {
        reviewBuilder
                .clientName(user.getDisplayName())
                .date(getTodaysDate())
                .details(details)
                .photoUrl(Objects.requireNonNull(user.getPhotoUrl()).toString())
                .uid(user.getUid());

        Review review = reviewBuilder.build();
        reviewsRef.add(review)
                .addOnSuccessListener(
                        documentReference -> viewInterface.notifyUser(MESSAGE_FEEDBACK))
                .addOnFailureListener(e -> viewInterface.notifyUser(CHECK_NETWORK));
    }

    @Override
    public boolean isLoggedIn() {
        boolean loggedIn = false;
        user = auth.getCurrentUser();
        if (user != null) loggedIn = true;
        return loggedIn;
    }

    @Override
    public String getUid() {
        String uid = "";
        if (isLoggedIn()) uid = user.getUid();
        return uid;
    }

    @Override
    public Query getQuery() {
        return reviewsRef.orderBy(DATE, Query.Direction.DESCENDING);
    }

    @Override
    public void setReviewRating(String rating) {
        reviewBuilder.rating(rating);

    }

    @Override
    public void setReviewService(String service) {
        reviewBuilder.service(service);
    }

    private String getTodaysDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE, Locale.US);
        return sdf.format(date);
    }
}
