package com.acrutchfield.natashashairstudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.ProductCollection;
import com.acrutchfield.natashashairstudio.utils.GlideApp;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductCollectionAdapter extends FirestoreRecyclerAdapter<ProductCollection, ProductCollectionAdapter.ProductCollectionHolder> {

    private final CollectionInteractionListener listener;
    private final Context context;

    public interface CollectionInteractionListener {
        void onCollectionInteraction(String productTitle);
    }

    public ProductCollectionAdapter(@NonNull FirestoreRecyclerOptions<ProductCollection> options,
                                    CollectionInteractionListener listener,
                                    Context context) {
        super(options);
        this.listener = listener;
        this.context = context;
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

    class ProductCollectionHolder extends RecyclerView.ViewHolder {

        FirebaseStorage storage = FirebaseStorage.getInstance();

        ImageView ivCollectionImage;
        TextView tvCollectionTitle;


        ProductCollectionHolder(@NonNull View itemView) {
            super(itemView);

            ivCollectionImage = itemView.findViewById(R.id.iv_collection_image);
            tvCollectionTitle = itemView.findViewById(R.id.tv_collection_title);


        }

        private void onBindCollection(ProductCollection collection) {
            String collectionTitle = collection.getTitle();
            tvCollectionTitle.setText(collectionTitle);


            String collectionPhotoRef = collection.getImageUrl();
            StorageReference storageRef = storage.getReference(collectionPhotoRef);

            GlideApp.with(context)
                    .load(storageRef)
                    .into(ivCollectionImage);

            itemView.setOnClickListener(v ->
                    listener.onCollectionInteraction(collection.getTitle()));
        }
    }
}
