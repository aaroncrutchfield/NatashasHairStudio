package com.acrutchfield.natashashairstudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.acrutchfield.natashashairstudio.viewmodel.ShopViewModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionDetailsActivity extends AppCompatActivity implements ProductAdapter.ProductInteractionLister {

    public static final String COLLECTION_TITLE = "collection_title";
    public static final String PRODUCT_EXTRA = "product";

    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);

        Toolbar toolbar = findViewById(R.id.toolbar_collection);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String collectionTitle = intent.getStringExtra(COLLECTION_TITLE);
        Log.d("CollectionDetailsActivity", "onCreate: " + collectionTitle);

        setupRecyclerView(collectionTitle);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(String collectionTitle) {
        adapter = new ProductAdapter( this, this);
        RecyclerView recyclerView = findViewById(R.id.rv_products);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        ShopViewModel viewModel = ViewModelProviders.of(this).get(ShopViewModel.class);
        viewModel.getProductsLiveData(collectionTitle).observe(this,
                products -> adapter.updateProducts(products));
    }

    @Override
    public void onProductInteraction(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(PRODUCT_EXTRA, product);
        startActivity(intent);
    }
}
