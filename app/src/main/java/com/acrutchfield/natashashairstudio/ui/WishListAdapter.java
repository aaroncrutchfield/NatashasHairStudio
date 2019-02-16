package com.acrutchfield.natashashairstudio.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WishListAdapter extends FirestoreRecyclerAdapter<Product, WishListAdapter.WishListHolder> {


    private final WishListInteractionListener listener;

    interface WishListInteractionListener {
        void onWishListInteraction(String fileRef);
    }

    public WishListAdapter(@NonNull FirestoreRecyclerOptions<Product> options, WishListInteractionListener listener) {
        super(options);
        this.listener = listener;
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

        final String WISHLIST_COLLECTION = "/WISHLIST/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/default_list/";

        ImageView ivWishListImage;
        TextView tvWishListCollection;
        TextView tvWishListProduct;
        TextView tvWishListPriceRange;

        public WishListHolder(@NonNull View itemView) {
            super(itemView);

            ivWishListImage = itemView.findViewById(R.id.iv_wish_list);
            tvWishListCollection = itemView.findViewById(R.id.tv_wish_list_collection);
            tvWishListProduct = itemView.findViewById(R.id.tv_wish_list_product);
            tvWishListPriceRange = itemView.findViewById(R.id.tv_wish_list_price_range);
        }

        public void onBindProduct(Product product) {
            tvWishListProduct.setText(product.getTitle());
            tvWishListPriceRange.setText(product.getPriceRange());
            tvWishListCollection.setText();
        }
    }
}
