package com.acrutchfield.natashashairstudio.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.ProductCollection;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductCollectionAdapter extends RecyclerView.Adapter<ProductCollectionAdapter.ProductCollectionHolder> {

    private final CollectionInteractionListener listener;
    private final Context context;
    private List<ProductCollection> productCollections = new ArrayList<>();

    interface CollectionInteractionListener {
        void onCollectionInteraction(String productTitle);
    }

    ProductCollectionAdapter(CollectionInteractionListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductCollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_collection, parent, false);

        return new ProductCollectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCollectionHolder holder, int position) {
        holder.onBindCollection(productCollections.get(position));
    }

    void updateProductCollections (List<ProductCollection> productCollections) {
        this.productCollections = productCollections;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (productCollections == null) return 0;
        return productCollections.size();
    }

    class ProductCollectionHolder extends RecyclerView.ViewHolder {

        FirebaseStorage storage = FirebaseStorage.getInstance();

        ImageView ivCollectionImage;
        TextView tvCollectionTitle;


        ProductCollectionHolder(@NonNull View itemView) {
            super(itemView);

            ivCollectionImage = itemView.findViewById(R.id.iv_collection_image);
            tvCollectionTitle = itemView.findViewById(R.id.tv_collection_title);

            itemView.setOnClickListener(v -> {
                String collectionTitle = productCollections.get(getAdapterPosition()).getTitle();
                listener.onCollectionInteraction(collectionTitle);
            });
        }

        private void onBindCollection(ProductCollection collection) {
            String collectionTitle = collection.getTitle();
            tvCollectionTitle.setText(collectionTitle);


            String collectionPhotoRef = collection.getImageUrl();
            StorageReference storageRef = storage.getReference(collectionPhotoRef);

            GlideApp.with(context)
                    .load(storageRef)
                    .into(ivCollectionImage);
        }
    }
}
