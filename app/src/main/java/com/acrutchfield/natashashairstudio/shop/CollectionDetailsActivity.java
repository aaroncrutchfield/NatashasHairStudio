package com.acrutchfield.natashashairstudio.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CollectionDetailsActivity extends AppCompatActivity implements ProductAdapter.ProductInteractionLister {

    private static final String COLLECTION_TITLE = "collection_title";
    private static final String BASE_REF = "/PRODUCT_COLLECTIONS/hair/";
    private static final String EXTRA_PRODUCT_REF = "productRef";
    private static final String TITLE = "title";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef;

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
        String completeRef = BASE_REF + collectionTitle;
        productRef = db.collection(completeRef);

        setupRecyclerView(collectionTitle);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
        Query query = productRef.orderBy(TITLE);

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new ProductAdapter(options, collectionTitle, this, this);
        RecyclerView recyclerView = findViewById(R.id.rv_products);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onProductInteraction(String productRef) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_REF, productRef);
        startActivity(intent);
    }
}
