package com.acrutchfield.natashashairstudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Review;
import com.acrutchfield.natashashairstudio.utils.GlideApp;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends FirestoreRecyclerAdapter<Review, ReviewAdapter.ReviewHolder> {


    private final FirestoreRecyclerOptions<Review> options;
    private final Context context;

    public ReviewAdapter(@NonNull FirestoreRecyclerOptions<Review> options, Context context) {
        super(options);
        this.options = options;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i, @NonNull Review review) {
        reviewHolder.onBindReview(review, i);
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewHolder(view);
    }

    class ReviewHolder extends RecyclerView.ViewHolder {

        static final String RATING_FORMAT = "%s.0";

        final ImageView ivProfile;
        final TextView tvService;
        final TextView tvRating;
        final TextView tvDetails;
        final TextView tvDate;
        final TextView tvClientName;

        ReviewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvService = itemView.findViewById(R.id.tv_review_service);
            tvRating = itemView.findViewById(R.id.tv_review_rating);
            tvDetails = itemView.findViewById(R.id.tv_review_details);
            tvDate = itemView.findViewById(R.id.tv_review_date);
            tvClientName = itemView.findViewById(R.id.tv_client_name);
        }

        void onBindReview(Review review, int position) {
            tvService.setText(review.getService());
            String rating = String.format(RATING_FORMAT, review.getRating());
            tvRating.setText(rating);
            tvDetails.setText(review.getDetails());
            tvDate.setText(review.getDate());
            tvClientName.setText(review.getClientName());

            // Set id to tag on itemView to assist with delete
            String id = options.getSnapshots().getSnapshot(position).getId();
            itemView.setTag(R.string.id, id);
            itemView.setTag(R.string.uid, review.getUid());

            GlideApp.with(context)
                    .load(review.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfile);
        }
    }
}
