package com.acrutchfield.natashashairstudio.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private final ProductInteractionLister listener;
    private Context context;

    private List<Product> products;

    interface ProductInteractionLister {
        void onProductInteraction(Product product);
    }

    ProductAdapter(Context context, ProductInteractionLister listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.onBindProduct(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    void updateProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

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

            StorageReference storageRef = storage.getReference(product.getImageUrl());

            GlideApp.with(context)
                    .load(storageRef)
                    .into(ivProductImage);

            itemView.setOnClickListener(v -> listener.onProductInteraction(product));
        }
    }


}
