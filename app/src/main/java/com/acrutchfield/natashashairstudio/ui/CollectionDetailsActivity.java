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

public class CollectionDetailsActivity extends AppCompatActivity {

    public static final String PRODUCT_TITLE = "product_title";
    public static final String BASE_REF = "/PRODUCT_COLLECTIONS/hair/";
    String testRef = "/PRODUCT_COLLECTIONS/hair/Beautiful Brazilian Body Wave";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef;

    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);

        Intent intent = getIntent();
        String productTitle = intent.getStringExtra(PRODUCT_TITLE);
        String completeRef = BASE_REF + productTitle;
        Log.d("CollectionDetailsActivity", "onCreate: " + productTitle);
        productRef = db.collection(completeRef);

        setupRecyclerView();
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

    private void setupRecyclerView() {
        Query query = productRef.orderBy("title");

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new ProductAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.rv_products);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }
}
