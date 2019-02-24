package com.acrutchfield.natashashairstudio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Review;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReviewsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ReviewsRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private class ReviewsRemoteViewsFactory implements RemoteViewsFactory {

        private final Context mContext;
        private CollectionReference reviewsRef;

        private List<Review> mReviews = new ArrayList<>();

        ReviewsRemoteViewsFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            reviewsRef = db.collection("/REVIEWS");
            getReviews();
        }

        @Override
        public void onDataSetChanged() {
            getReviews();
        }

        @Override
        public void onDestroy() {
            mReviews.clear();
        }

        @Override
        public int getCount() {
            return mReviews.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Review review = mReviews.get(position);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_review);

            String date = review.getDate();
            String clientName = review.getClientName();
            String rating = String.format("%s.0", review.getRating());
            String service = review.getService();

            views.setTextViewText(R.id.tv_widget_date, date);
            views.setTextViewText(R.id.tv_widget_name, clientName);
            views.setTextViewText(R.id.tv_widget_rating, rating);
            views.setTextViewText(R.id.tv_widget_service, service);

            Bitmap bitmap = null;

            try {
                URL url = new URL(review.getPhotoUrl());
                bitmap = BitmapFactory.decodeStream(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            views.setImageViewBitmap(R.id.iv_widget_profile, bitmap);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        // TODO: 2/24/19 get reviews in order by date
        void getReviews() {
            List<Review> reviews = new ArrayList<>();
            reviewsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document :
                            Objects.requireNonNull(task.getResult())) {
                        reviews.add(document.toObject(Review.class));
                    }
                    mReviews = reviews;
                }
            });
        }
    }
}
