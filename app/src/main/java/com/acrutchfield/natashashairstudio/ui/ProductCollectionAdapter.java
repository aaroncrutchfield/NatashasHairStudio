package com.acrutchfield.natashashairstudio.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.ProductCollection;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductCollectionAdapter extends FirestoreRecyclerAdapter<ProductCollection, ProductCollectionAdapter.ProductCollectionHolder> {


    public ProductCollectionAdapter(@NonNull FirestoreRecyclerOptions<ProductCollection> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductCollectionHolder productCollectionHolder, int i, @NonNull ProductCollection collection) {
        productCollectionHolder.onBindCollection(collection);
    }

    @NonNull
    @Override
    public ProductCollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_collection, parent, false);

        return new ProductCollectionHolder(view);
    }

    public class ProductCollectionHolder extends RecyclerView.ViewHolder {

        ImageView ivCollectionImage;
        TextView tvCollectionTitle;

        public ProductCollectionHolder(@NonNull View itemView) {
            super(itemView);

            ivCollectionImage = itemView.findViewById(R.id.iv_collection_image);
            tvCollectionTitle = itemView.findViewById(R.id.tv_collection_title);
        }

        private void onBindCollection(ProductCollection collection) {
            tvCollectionTitle.setText(collection.getTitle());
        }
    }
}
