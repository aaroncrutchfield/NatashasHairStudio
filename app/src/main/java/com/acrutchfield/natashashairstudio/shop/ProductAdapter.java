package com.acrutchfield.natashashairstudio.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.acrutchfield.natashashairstudio.utils.GlideApp;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductHolder> {

    private final ProductInteractionLister listener;
    private final String collectionTitle;
    private final Context context;

    public interface ProductInteractionLister {
        void onProductInteraction(String fileRef);
    }

    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options,
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

        final String COLLECTION_FOLDER = "PRODUCT_COLLECTIONS/hair/" + collectionTitle + "/";
        final FirebaseStorage storage = FirebaseStorage.getInstance();

        final ImageView ivProductImage;
        final TextView tvProductTitle;
        final TextView tvProductPriceRange;

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
            String productPhotoRef = product.getImageUrl();
            StorageReference storageRef = storage.getReference(productPhotoRef);

            GlideApp.with(context)
                    .load(storageRef)
                    .into(ivProductImage);

            itemView.setOnClickListener(v ->
                    listener.onProductInteraction(productRef));
        }
    }


}
