package com.acrutchfield.natashashairstudio.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.adapter.ReviewAdapter;
import com.acrutchfield.natashashairstudio.model.Review;
import com.acrutchfield.natashashairstudio.utils.DeleteItemCallback;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewFragment extends Fragment implements DeleteItemCallback.DeletePromptInterface {

    private static final String REVIEW_DELETED = "Your review was deleted.";
    private static final String REVIEW_ERROR = "Error. Your review could not be deleted.";
    private static final String CONFIRM = "Confirm";
    private static final String REVIEW_CONFIRM_DELETE = "Are you sure you want to delete this review?";
    private static final String REVIEW_DELETE_TITLE = "Delete Review";
    private static final String REVIEW_NOT_YOURS = "You can only delete reviews you created.";
    private static final String REVIEW_DELETE_CANCELED = "Delete canceled.";
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reviewsRef = db.collection("/REVIEWS");

    private ReviewAdapter adapter;

    private Review.Builder reviewBuilder;

    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, container, false);
        setupRecyclerView(view);

        FloatingActionButton fabAddReview = view.findViewById(R.id.fab_add_review);

        fabAddReview.setOnClickListener(v -> {
            // TODO: 2/13/19 check if logged on
            // TODO: 2/13/19 if not, warn user
            promptForReview();

        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setupRecyclerView(View view) {
        Query query = reviewsRef.orderBy("date");

        FirestoreRecyclerOptions<Review> options = new FirestoreRecyclerOptions.Builder<Review>()
                .setQuery(query, Review.class)
                .build();

        adapter = new ReviewAdapter(options, getContext());

        ItemTouchHelper.SimpleCallback simpleCallback =
                new DeleteItemCallback(getContext(), this, 0, ItemTouchHelper.LEFT);

        RecyclerView recyclerView = view.findViewById(R.id.rv_reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void promptForReview() {
        reviewBuilder = new Review.Builder();
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_review, null);

        EditText etDetails = view.findViewById(R.id.et_details);
        Spinner spRating = view.findViewById(R.id.sp_rating);
        Spinner spService = view.findViewById(R.id.sp_service);

        setupDialogSpinners(spRating, spService);

        AlertDialog alertDialog = builder.setTitle("Review")
                .setView(view)
                .setPositiveButton("Submit", (dialog, which) -> {

                })
                .setNegativeButton("Cancel", (dialog, which) -> notifyUser("Canceled"))
                .create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String details = etDetails.getText().toString();
            if (details.trim().equals("")) {
                notifyUser("Please add details before submitting");
            } else {
                addReview(details);
                alertDialog.dismiss();

            }
        });
    }

    private void addReview(String details) {
        reviewBuilder
                .clientName(user.getDisplayName())
                .date(getTodaysDate())
                .details(details)
                .photoUrl(Objects.requireNonNull(user.getPhotoUrl()).toString())
                .uid(user.getUid());

        Review review = reviewBuilder.build();
        reviewsRef.add(review)
                .addOnSuccessListener(
                        documentReference -> notifyUser("We appreciate your feedback!"))
                .addOnFailureListener(
                        e -> notifyUser("Failure. Check your network connection."));
    }

    private void notifyUser(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String getTodaysDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return sdf.format(date);
    }

    private void setupDialogSpinners(Spinner spRating, Spinner spService) {
        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(
                Objects.requireNonNull(getContext()), R.array.ratings_array, android.R.layout.simple_spinner_item);
        spRating.setAdapter(ratingAdapter);

        ArrayAdapter<CharSequence> serviceAdapter = ArrayAdapter.createFromResource(
                Objects.requireNonNull(getContext()), R.array.ratings_services, android.R.layout.simple_spinner_item);
        spService.setAdapter(serviceAdapter);

        spRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvRating = (TextView)view;
                Log.d("ReviewFragment", "onItemSelected: " + tvRating.getText().toString());
                reviewBuilder.rating(tvRating.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvService = (TextView)view;
                Log.d("ReviewFragment", "onItemSelected: " + tvService.getText().toString());
                reviewBuilder.service(tvService.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void promptForDelete(String uid, String id, int position) {
        AlertDialog.Builder builder;
        if (user.getUid().equals(uid)) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                    .setTitle(REVIEW_DELETE_TITLE)
                    .setMessage(REVIEW_CONFIRM_DELETE)
                    .setPositiveButton(CONFIRM, (dialog, which) -> reviewsRef.document(id).delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            adapter.notifyItemRemoved(position);
                            adapter.notifyDataSetChanged();
                            notifyUser(REVIEW_DELETED);

                        } else {
                            notifyUser(REVIEW_ERROR);
                        }
                    }))
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        adapter.notifyDataSetChanged();
                        notifyUser(REVIEW_DELETE_CANCELED);
                    });
            builder.create().show();
        } else {
            adapter.notifyDataSetChanged();
            notifyUser(REVIEW_NOT_YOURS);
        }
    }
}
