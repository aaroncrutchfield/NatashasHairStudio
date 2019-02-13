package com.acrutchfield.natashashairstudio.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductHolder> {

    private final ProductInteractionLister listener;
    private String collectionTitle;
    private Context context;

    interface ProductInteractionLister {
        void onProductInteraction(String fileRef);
    }

    ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options,
                   String collectionTitle,
                   Context context,
                   ProductInteractionLister listener) {
        super(options);
        this.collectionTitle = collectionTitle;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder productHolder, int i, @NonNull Product product) {
        productHolder.onBindProduct(product);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductHolder(view);
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        final String COLLECTION_FOLDER = "PRODUCT_COLLECTION/hair/" + collectionTitle + "/";
        FirebaseStorage storage = FirebaseStorage.getInstance();

        ImageView ivProductImage;
        TextView tvProductTitle;
        TextView tvProductPriceRange;

        ProductHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductTitle = itemView.findViewById(R.id.tv_product_title);
            tvProductPriceRange = itemView.findViewById(R.id.tv_product_pricerange);
        }

        private void onBindProduct(Product product) {
            String productTitle = product.getTitle();
            tvProductTitle.setText(productTitle);
            tvProductPriceRange.setText(product.getPriceRange());

            String productRef = COLLECTION_FOLDER + productTitle;
            String productPhotoRef = productRef + ".png";
            StorageReference storageRef = storage.getReference(productPhotoRef);

            GlideApp.with(context)
                    .load(storageRef)
                    .into(ivProductImage);

            itemView.setOnClickListener(v -> {
                listener.onProductInteraction(productRef);
            });
        }
    }


}
