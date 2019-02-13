package com.acrutchfield.natashashairstudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionDetailsActivity extends AppCompatActivity implements ProductAdapter.ProductInteractionLister {

    public static final String COLLECTION_TITLE = "collection_title";
    public static final String BASE_REF = "/PRODUCT_COLLECTIONS/hair/";
    public static final String EXTRA_PRODUCT_REF = "productRef";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef;

    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);

        Intent intent = getIntent();
        String collectionTitle = intent.getStringExtra(COLLECTION_TITLE);
        String completeRef = BASE_REF + collectionTitle;
        Log.d("CollectionDetailsActivity", "onCreate: " + collectionTitle);
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

    private void setupRecyclerView(String collectionTitle) {
        Query query = productRef.orderBy("title");

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
