package com.acrutchfield.natashashairstudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.acrutchfield.natashashairstudio.utils.GlideApp;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WishListAdapter extends FirestoreRecyclerAdapter<Product, WishListAdapter.WishListHolder> {


    private final WishListInteractionListener listener;
    private final Context context;

    public interface WishListInteractionListener {
        void onWishListInteraction(String productRef);
    }

    public WishListAdapter(@NonNull FirestoreRecyclerOptions<Product> options,
                           WishListInteractionListener listener,
                           Context context) {
        super(options);
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull WishListHolder wishListHolder, int i, @NonNull Product product) {
        wishListHolder.onBindProduct(product);
    }

    @NonNull
    @Override
    public WishListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wish_list, parent, false);
        return new WishListHolder(view);
    }

    class WishListHolder extends RecyclerView.ViewHolder {

        final String BASE_REF = "PRODUCT_COLLECTIONS/hair/";

        final FirebaseStorage storage = FirebaseStorage.getInstance();

        final ImageView ivWishListImage;
        final TextView tvWishListCollection;
        final TextView tvWishListProduct;
        final TextView tvWishListPriceRange;

        WishListHolder(@NonNull View itemView) {
            super(itemView);

            ivWishListImage = itemView.findViewById(R.id.iv_wish_list);
            tvWishListCollection = itemView.findViewById(R.id.tv_wish_list_collection);
            tvWishListProduct = itemView.findViewById(R.id.tv_wish_list_product);
            tvWishListPriceRange = itemView.findViewById(R.id.tv_wish_list_price_range);
        }

        void onBindProduct(Product product) {
            tvWishListProduct.setText(product.getTitle());
            tvWishListPriceRange.setText(product.getPriceRange());
            tvWishListCollection.setText(product.getCollection());

            String productRef = BASE_REF + product.getCollection() + "/" + product.getTitle();
            String wishlistPhotoRef = product.getImageUrl();
            StorageReference storageRef = storage.getReference(wishlistPhotoRef);

            GlideApp.with(context)
                    .load(storageRef)
                    .into(ivWishListImage);

            itemView.setOnClickListener(v ->
                    listener.onWishListInteraction(productRef));
        }
    }
}