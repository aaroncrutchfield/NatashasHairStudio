package com.acrutchfield.natashashairstudio.review;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Review;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class ReviewFragment extends Fragment implements ReviewAdapter.DeletePromptInterface, ReviewContract.ViewInterface {

    private static final String REVIEW = "Review";
    private static final String SUBMIT = "Submit";
    private static final String CANCEL = "Cancel";
    private static final String MESSAGE_CANCELED = "Canceled";
    private static final String ADD_DETAILS = "Please add details before submitting";
    private static final String LOG_IN_FIRST = "You must log in first";
    private Context context;

    private ReviewAdapter adapter;

    private ReviewPresenter presenter;

    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, container, false);

        FloatingActionButton fabAddReview = view.findViewById(R.id.fab_add_review);

        context = getContext();
        presenter = new ReviewPresenter(this);
        setupRecyclerView(view);

        fabAddReview.setOnClickListener(v -> {
            if (presenter.isLoggedIn()) promptForReview();
            else notifyUser(LOG_IN_FIRST);
        });

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
        FirestoreRecyclerOptions<Review> options = new FirestoreRecyclerOptions.Builder<Review>()
                .setQuery(presenter.getQuery(), Review.class)
                .build();

        String uid = presenter.getUid();
        adapter = new ReviewAdapter(options, context, this);
        adapter.setUid(uid);

        RecyclerView recyclerView = view.findViewById(R.id.rv_reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private void promptForReview() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_review, null);

        EditText etDetails = view.findViewById(R.id.et_details);
        Spinner spRating = view.findViewById(R.id.sp_rating);
        Spinner spService = view.findViewById(R.id.sp_service);

        setupDialogSpinners(spRating, spService);

        AlertDialog alertDialog = builder.setTitle(REVIEW)
                .setView(view)
                .setPositiveButton(SUBMIT, (dialog, which) -> {

                })
                .setNegativeButton(CANCEL, (dialog, which) -> notifyUser(MESSAGE_CANCELED))
                .create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String details = etDetails.getText().toString();
            if (details.trim().equals("")) {
                notifyUser(ADD_DETAILS);
            } else {
                presenter.addReview(details);
                alertDialog.dismiss();

            }
        });
    }

    @Override
    public void notifyUser(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void setupDialogSpinners(Spinner spRating, Spinner spService) {
        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(
                Objects.requireNonNull(context), R.array.ratings_array, android.R.layout.simple_spinner_item);
        spRating.setAdapter(ratingAdapter);

        ArrayAdapter<CharSequence> serviceAdapter = ArrayAdapter.createFromResource(
                Objects.requireNonNull(context), R.array.ratings_services, android.R.layout.simple_spinner_item);
        spService.setAdapter(serviceAdapter);

        spRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvRating = (TextView)view;
                presenter.setReviewRating(tvRating.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvService = (TextView)view;
                presenter.setReviewService(tvService.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void promptForDelete(String id, int position) {

        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(dialogView);
        dialog.show();

//        user = auth.getCurrentUser();
//
//        if (user != null) {
//            AlertDialog.Builder builder;
//                builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
//                        .setTitle(REVIEW_DELETE_TITLE)
//                        .setMessage(REVIEW_CONFIRM_DELETE)
//                        .setPositiveButton(CONFIRM, (dialog, which) -> reviewsRef.document(id).delete().addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                adapter.notifyItemRemoved(position);
//                                adapter.notifyDataSetChanged();
//                                notifyUser(REVIEW_DELETED);
//
//                            } else {
//                                notifyUser(REVIEW_ERROR);
//                            }
//                        }))
//                        .setNegativeButton(CANCEL, (dialog, which) -> {
//                            adapter.notifyDataSetChanged();
//                            notifyUser(REVIEW_DELETE_CANCELED);
//                        });
//                builder.create().show();
//        } else {
//            Toast.makeText(getContext(), LOG_IN_FIRST, Toast.LENGTH_SHORT).show();
//            adapter.notifyDataSetChanged();
//        }
    }
}
